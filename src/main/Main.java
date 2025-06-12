package main;

import dao.*;
import model.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

/**
 * Classe principal da aplicação para criação, consulta, exclusão de relatos e conta de usuário,
 * com validações de entrada, data exibida e agrupamento de relatos repetidos por doença e local.
 */
public class Main {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        DoencaDAO doencaDAO = new DoencaDAOImpl();
        LocalDAO localDAO = new LocalDAOImpl();
        RelatoDAO relatoDAO = new RelatoDAOImpl();

        GerenciadorDeApelido gerenciador = new GerenciadorDeApelido();
        Usuario usuarioTemp = gerenciador.criarUsuarioUnico();
        Usuario usuario = usuarioDAO.criar(usuarioTemp);
        final int usuarioId = usuario.getId();
        System.out.println("Bem-vindo! Seu apelido é: " + usuario.getApelido());
        System.out.println("Relatos já cadastrados no sistema:");
        List<Relato> todos = relatoDAO.listarTodos();
        if (todos.isEmpty()) {
            System.out.println("  (ainda não há relatos)");
        } else {
            todos.forEach(r -> System.out.println(formatRelato(r)));
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Criar relato");
            System.out.println("2. Buscar relatos por local e gravidade");
            System.out.println("3. Listar meus relatos");
            System.out.println("4. Editar relato");
            System.out.println("5. Excluir relato");
            System.out.println("6. Excluir conta de usuário");
            System.out.println("7. Sair");
            int opcao = readValidOption(scanner, "Escolha uma opção (1-7): ", 1, 7);

            switch (opcao) {
                case 1 -> criarRelato(scanner, doencaDAO, localDAO, relatoDAO, usuario);
                case 2 -> buscarPorLocalEGravidade(scanner, localDAO, relatoDAO);
                case 3 -> listarMeusRelatos(relatoDAO, usuarioId);
                case 4 -> editarRelato(scanner, doencaDAO, localDAO, relatoDAO, usuario);
                case 5 -> excluirRelato(scanner, relatoDAO, usuario);
                case 6 -> {
                    if (readConfirmation(scanner, "Deseja realmente excluir sua conta e todos os relatos? (S/N): ")) {
                        if (usuarioDAO.deletar(usuarioId)) {
                            System.out.println("Conta e todos os seus relatos foram excluídos.");
                        } else {
                            System.out.println("Falha ao excluir conta.");
                        }
                        executando = false;
                    }
                }
                case 7 -> {
                    executando = false;
                    System.out.println("Encerrando aplicação. Até mais!");
                }
            }
        }
        scanner.close();
    }

    private static void criarRelato(Scanner scanner, DoencaDAO doencaDAO,
                                    LocalDAO localDAO, RelatoDAO relatoDAO, Usuario usuario) {
        System.out.println("\n-- Criar Relato --");
        int filtro = readValidOption(scanner, "Filtrar doenças por gravidade (1-Leve,2-Moderada,3-Grave,4-Todas): ", 1, 4);
        List<Doenca> doencas = switch (filtro) {
            case 1 -> doencaDAO.buscarPorGravidade("Leve");
            case 2 -> doencaDAO.buscarPorGravidade("Moderada");
            case 3 -> doencaDAO.buscarPorGravidade("Grave");
            default -> doencaDAO.listarTodos();
        };
        if (doencas.isEmpty()) { System.out.println("Nenhuma doença disponível para esse filtro."); return; }
        doencas.forEach(d -> System.out.printf("%d - %s (%s)%n", d.getId(), d.getNome(), d.getGrauDeRisco()));
        Set<Integer> idsDoenca = doencas.stream().map(Doenca::getId).collect(Collectors.toSet());
        int idDoenca = readValidId(scanner, "Informe o ID da doença: ", idsDoenca::contains, "ID inválido.");
        Doenca doenca = doencaDAO.buscarPorId(idDoenca);

        List<Local> locais = localDAO.listarTodos();
        locais.forEach(l -> System.out.printf("%d - %s%n", l.getId(), l.getNome()));
        Set<Integer> idsLocal = locais.stream().map(Local::getId).collect(Collectors.toSet());
        int idLocal = readValidId(scanner, "Informe o ID do local: ", idsLocal::contains, "ID inválido.");
        Local local = localDAO.buscarPorId(idLocal);

        Relato r = Relato.criar(usuario, doenca, local, new Date());
        r = relatoDAO.criar(r);
        usuario.adicionarRelato(r);
        System.out.println("Relato criado: " + formatRelato(r));
    }

    private static void buscarPorLocalEGravidade(Scanner scanner, LocalDAO localDAO, RelatoDAO relatoDAO) {
        System.out.println("\n-- Buscar Relatos por Local e Gravidade --");
        int fg = readValidOption(scanner, "Filtrar gravidade (1-Leve,2-Moderada,3-Grave,4-Todas): ", 1, 4);
        String gravidade = switch (fg) {
            case 1 -> "Leve"; case 2 -> "Moderada"; case 3 -> "Grave"; default -> null;
        };
        List<Local> locais = localDAO.listarTodos();
        locais.forEach(l -> System.out.printf("%d - %s%n", l.getId(), l.getNome()));
        Set<Integer> idsLocal = locais.stream().map(Local::getId).collect(Collectors.toSet());
        int idLocal = readValidId(scanner, "Informe o ID do local: ", idsLocal::contains, "ID inválido.");
        Local local = localDAO.buscarPorId(idLocal);
        Map<Doenca, Integer> cont = relatoDAO.contarPorDoencaPorLocal(idLocal);
        if (gravidade != null) cont.entrySet().removeIf(e -> !e.getKey().getGrauDeRisco().equals(gravidade));
        System.out.println("Relatos em " + local.getNome() + ":");
        cont.forEach((d, c) -> System.out.println(d.getNome() + (c>1?" +"+(c-1):"")));
    }

    private static void listarMeusRelatos(RelatoDAO relatoDAO, int usuarioId) {
        System.out.println("\n-- Meus Relatos --");
        List<Relato> meus = relatoDAO.listarTodos().stream()
                .filter(r -> r.getUsuario().getId() == usuarioId)
                .collect(Collectors.toList());
        if (meus.isEmpty()) System.out.println("Você não criou nenhum relato.");
        else meus.forEach(r -> System.out.println(formatRelato(r)));
    }

    private static void editarRelato(Scanner scanner, DoencaDAO doencaDAO,
                                     LocalDAO localDAO, RelatoDAO relatoDAO, Usuario usuario) {
        System.out.println("\n-- Editar Relato --");
        List<Relato> meus = relatoDAO.listarTodos().stream()
                .filter(r -> r.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
        if (meus.isEmpty()) { System.out.println("Nenhum relato para editar."); return; }
        meus.forEach(r -> System.out.println(formatRelato(r)));
        Set<Integer> ids = meus.stream().map(Relato::getId).collect(Collectors.toSet());
        int idRel = readValidId(scanner, "Informe o ID do relato a editar: ", ids::contains, "ID inválido.");
        Relato old = relatoDAO.buscarPorId(idRel);
        System.out.println("Editar: 1-Doença  2-Local  3-Ambos");
        int choice = readValidOption(scanner, "Opção (1-3): ", 1, 3);

        Doenca newD = old.getDoenca();
        Local newL = old.getLocal();
        if (choice == 1 || choice == 3) {
            List<Doenca> allD = doencaDAO.listarTodos();
            allD.forEach(d -> System.out.printf("%d - %s%n", d.getId(), d.getNome()));
            Set<Integer> dids = allD.stream().map(Doenca::getId).collect(Collectors.toSet());
            int nd = readValidId(scanner, "Novo ID de doença: ", dids::contains, "ID inválido.");
            newD = doencaDAO.buscarPorId(nd);
        }
        if (choice == 2 || choice == 3) {
            List<Local> allL = localDAO.listarTodos();
            allL.forEach(l -> System.out.printf("%d - %s%n", l.getId(), l.getNome()));
            Set<Integer> lids = allL.stream().map(Local::getId).collect(Collectors.toSet());
            int nl = readValidId(scanner, "Novo ID de local: ", lids::contains, "ID inválido.");
            newL = localDAO.buscarPorId(nl);
        }
        // Reconstrói e atualiza
        Relato updated = Relato.reconstruir(old.getId(), usuario, newD, newL, old.getData());
        relatoDAO.atualizar(updated);
        System.out.println("Relato atualizado: " + formatRelato(updated));
    }

    private static void excluirRelato(Scanner scanner, RelatoDAO relatoDAO, Usuario usuario) {
        System.out.println("\n-- Excluir Relato --");
        List<Relato> meus = relatoDAO.listarTodos().stream()
                .filter(r -> r.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
        if (meus.isEmpty()) { System.out.println("Nenhum relato para excluir."); return; }
        meus.forEach(r -> System.out.println(formatRelato(r)));
        Set<Integer> ids = meus.stream().map(Relato::getId).collect(Collectors.toSet());
        int idRem = readValidId(scanner, "Informe o ID do relato a excluir: ", ids::contains, "ID inválido.");
        if (relatoDAO.deletar(idRem)) System.out.println("Relato " + idRem + " excluído.");
        else System.out.println("Falha ao excluir relato.");
    }

    private static int readValidOption(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String in = scanner.nextLine().trim();
            if (in.matches("\\d+")) {
                int val = Integer.parseInt(in);
                if (val >= min && val <= max) return val;
            }
            System.out.printf("Entrada inválida. Digite um número entre %d e %d.%n", min, max);
        }
    }

    private static int readValidId(Scanner scanner, String prompt, IntPredicate validator, String errMsg) {
        while (true) {
            System.out.print(prompt);
            String in = scanner.nextLine().trim();
            if (in.matches("\\d+")) {
                int val = Integer.parseInt(in);
                if (validator.test(val)) return val;
            }
            System.out.println(errMsg);
        }
    }

    private static boolean readConfirmation(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String r = scanner.nextLine().trim().toUpperCase();
            if (r.equals("S")) return true;
            if (r.equals("N")) return false;
            System.out.println("Resposta inválida. Digite 'S' ou 'N'.");
        }
    }

    /**
     * Formata um relato para exibição: [ID] apelido - doença (sintomas) - local - data
     */
    private static String formatRelato(Relato r) {
        String sintomas = r.getDoenca().getSintomas().stream()
                .map(Sintoma::getNome)
                .collect(Collectors.joining(", "));
        String dateStr = DATE_FORMAT.format(r.getData());
        return String.format("[%d] %s - %s (%s) - %s - %s",
                r.getId(), r.getUsuario().getApelido(), r.getDoenca().getNome(),
                sintomas, r.getLocal().getNome(), dateStr);
    }
}

