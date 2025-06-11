package main;
import dao.*;
import model.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // DAOs instanciados
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        DoencaDAO doencaDAO = new DoencaDAOImpl();
        LocalDAO localDAO = new LocalDAOImpl();
        RelatoDAO relatoDAO = new RelatoDAOImpl();

        boolean executando = true;


        while (executando) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Criar novo relato");
            System.out.println("2. Listar todos os relatos");
            System.out.println("3. Filtrar relatos por gravidade");
            System.out.println("4. Filtrar relatos por local");
            System.out.println("5. Ver resumo (+x) por doença e local");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // consumir o enter

            switch (opcao) {
                case 1 -> {
                    System.out.print("Digite seu apelido (deixe vazio para gerar aleatório): ");
                    String apelido = scanner.nextLine();
                    if (apelido.isBlank()) {
                        apelido = "usuario_" + System.currentTimeMillis();
                        System.out.println("Apelido gerado: " + apelido);
                    }

                    Usuario usuario = usuarioDAO.buscarPorApelido(apelido);
                    if (usuario == null) {
                        usuario = Usuario.criar(apelido);
                        usuario = usuarioDAO.inserir(usuario);
                    }

                    System.out.print("Digite o nome da doença: ");
                    String nomeDoenca = scanner.nextLine();

                    System.out.print("Gravidade (LEVE, MODERADA, GRAVE): ");
                    String gravidade = scanner.nextLine().toUpperCase();

                    Doenca doenca;
                    switch (gravidade) {
                        case "LEVE" -> doenca = new DoencaLeve(nomeDoenca);
                        case "MODERADA" -> doenca = new DoencaModerada(nomeDoenca);
                        case "GRAVE" -> doenca = new DoencaGrave(nomeDoenca);
                        default -> {
                            System.out.println("Gravidade inválida.");
                            break;
                        }
                    }

                    Doenca doencaExistente = doencaDAO.buscarPorNome(nomeDoenca);
                    if (doencaExistente == null) {
                        doencaDAO.inserir(doenca);
                        doenca = doencaDAO.buscarPorNome(nomeDoenca);
                    } else {
                        doenca = doencaExistente;
                    }

                    System.out.print("Digite o nome do local: ");
                    String nomeLocal = scanner.nextLine();

                    Local local = localDAO.buscarPorNome(nomeLocal);
                    if (local == null) {
                        local = Local.criar(nomeLocal);
                        local = localDAO.inserir(local);
                    }

                    boolean jaRelatado = relatoDAO.existeRelatoDoUsuario(usuario, doenca, local);
                    if (jaRelatado) {
                        System.out.println("Você já relatou essa doença nesse local.");
                    } else {
                        Relato relato = Relato.criar(usuario, doenca, local);
                        relatoDAO.inserir(relato);
                        System.out.println("Relato registrado com sucesso!");
                    }
                }


                case 2 -> System.out.println("Listar relatos (em breve)");
                case 3 -> System.out.println("Filtrar por gravidade (em breve)");
                case 4 -> System.out.println("Filtrar por local (em breve)");
                case 5 -> System.out.println("Resumo por doença e local (em breve)");
                case 0 -> {
                    System.out.println("Encerrando...");
                    executando = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }
}
