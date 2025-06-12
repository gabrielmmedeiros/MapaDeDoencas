package main;

import dao.*;
import model.*;
import util.ConnectionFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe principal da aplicação para criação, consulta e exclusão de relatos.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inicialização de DAOs
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        DoencaDAO doencaDAO = new DoencaDAOImpl();
        LocalDAO localDAO = new LocalDAOImpl();
        RelatoDAO relatoDAO = new RelatoDAOImpl();

        // Geração de apelido anônimo e persistência
        GerenciadorDeApelido gerenciador = new GerenciadorDeApelido();
        Usuario usuarioTemp = gerenciador.criarUsuarioUnico();
        Usuario usuario = usuarioDAO.criar(usuarioTemp);
        final int usuarioId = usuario.getId(); // para uso em lambdas
        System.out.println("Bem-vindo! Seu apelido é: " + usuario.getApelido());

        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Criar relato");
            System.out.println("2. Buscar relatos por local e gravidade");
            System.out.println("3. Listar meus relatos");
            System.out.println("4. Excluir relato");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> {
                    // Criar relato
                    System.out.println("Filtrar doenças por gravidade:");
                    System.out.println("1 - Leve\n2 - Moderada\n3 - Grave\n4 - Todas");
                    System.out.print("Opção: ");
                    String filtro = scanner.nextLine().trim();
                    List<Doenca> doencas;
                    switch (filtro) {
                        case "1" -> doencas = doencaDAO.buscarPorGravidade("Leve");
                        case "2" -> doencas = doencaDAO.buscarPorGravidade("Moderada");
                        case "3" -> doencas = doencaDAO.buscarPorGravidade("Grave");
                        default -> doencas = doencaDAO.listarTodos();
                    }
                    if (doencas.isEmpty()) {
                        System.out.println("Nenhuma doença encontrada para esse filtro.");
                        break;
                    }
                    System.out.println("Doenças disponíveis:");
                    doencas.forEach(d -> System.out.printf("%d - %s (%s)%n", d.getId(), d.getNome(), d.getGrauDeRisco()));
                    System.out.print("Informe o ID da doença: ");
                    int idDoenca = Integer.parseInt(scanner.nextLine().trim());
                    Doenca doenca = doencaDAO.buscarPorId(idDoenca);
                    if (doenca == null) {
                        System.out.println("Doença inválida.");
                        break;
                    }

                    System.out.println("Locais disponíveis:");
                    List<Local> locais = localDAO.listarTodos();
                    locais.forEach(l -> System.out.printf("%d - %s%n", l.getId(), l.getNome()));
                    System.out.print("Informe o ID do local: ");
                    int idLocal = Integer.parseInt(scanner.nextLine().trim());
                    Local local = localDAO.buscarPorId(idLocal);
                    if (local == null) {
                        System.out.println("Local inválido.");
                        break;
                    }

                    Relato relato = Relato.criar(usuario, doenca, local, new Date());
                    relato = relatoDAO.criar(relato);
                    usuario.adicionarRelato(relato);
                    System.out.println("Relato criado: ");
                    System.out.println(formatRelato(relato));
                }
                case "2" -> {
                    // Busca por local e gravidade
                    System.out.println("Filtrar por gravidade:");
                    System.out.println("1 - Leve\n2 - Moderada\n3 - Grave\n4 - Todas");
                    System.out.print("Escolha: ");
                    String fg = scanner.nextLine().trim();
                    final String gravidadeFilt = switch (fg) {
                        case "1" -> "Leve";
                        case "2" -> "Moderada";
                        case "3" -> "Grave";
                        default -> null;
                    };

                    System.out.println("Locais disponíveis:");
                    List<Local> todosLocais = localDAO.listarTodos();
                    todosLocais.forEach(l -> System.out.printf("%d - %s%n", l.getId(), l.getNome()));
                    System.out.print("Informe o ID do local: ");
                    int consultaLocalId = Integer.parseInt(scanner.nextLine().trim());
                    Local consultaLocal = localDAO.buscarPorId(consultaLocalId);
                    if (consultaLocal == null) {
                        System.out.println("Local inválido.");
                        break;
                    }

                    Map<Doenca, Integer> contagens = relatoDAO.contarPorDoencaPorLocal(consultaLocalId);
                    if (gravidadeFilt != null) {
                        contagens.entrySet().removeIf(e -> !e.getKey().getGrauDeRisco().equals(gravidadeFilt));
                    }

                    System.out.println("Relatos em " + consultaLocal.getNome() + ":");
                    contagens.forEach((d, c) -> {
                        if (c == 1) System.out.println(d.getNome());
                        else System.out.println(d.getNome() + " +" + (c - 1));
                    });
                }
                case "3" -> {
                    // Listar relatos do usuário
                    List<Relato> meus = relatoDAO.listarTodos().stream()
                            .filter(r -> r.getUsuario().getId() == usuarioId)
                            .collect(Collectors.toList());
                    if (meus.isEmpty()) {
                        System.out.println("Você não criou nenhum relato.");
                    } else {
                        System.out.println("Seus relatos:");
                        meus.forEach(r -> System.out.println(formatRelato(r)));
                    }
                }
                case "4" -> {
                    // Excluir relato do usuário
                    System.out.print("Informe o ID do relato a excluir: ");
                    int idRem = Integer.parseInt(scanner.nextLine().trim());
                    Relato alvo = relatoDAO.buscarPorId(idRem);
                    if (alvo == null || alvo.getUsuario().getId() != usuarioId) {
                        System.out.println("Relato não encontrado ou não é seu.");
                    } else {
                        boolean ok = relatoDAO.deletar(idRem);
                        if (ok) {
                            usuario.removerRelato(alvo);
                            System.out.println("Relato " + idRem + " excluído.");
                        } else {
                            System.out.println("Falha ao excluir relato.");
                        }
                    }
                }
                case "5" -> {
                    sair = true;
                    System.out.println("Encerrando aplicação. Até mais!");
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
        scanner.close();
    }

    /**
     * Formata um relato para exibição: apelido - doença(sintomas) - local
     */
    private static String formatRelato(Relato r) {
        String sintomas = r.getDoenca().getSintomas().stream()
                .map(Sintoma::getNome)
                .collect(Collectors.joining(", "));
        return String.format("%s - %s (%s) - %s",
                r.getUsuario().getApelido(),
                r.getDoenca().getNome(),
                sintomas,
                r.getLocal().getNome());
    }
}
