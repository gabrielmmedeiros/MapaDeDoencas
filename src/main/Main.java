package main;
import model.*;
import Interface.IdentificavelPorNome;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Relato> todosRelatos = new ArrayList<>();

        // 1. Cria usuário
        Usuario usuario = Usuario.criar();
        System.out.println("Bem-vindo! Seu apelido é: " + usuario.getApelido());

        // 2. Lista de doenças e locais simulados
        List<Doenca> doencas = List.of(
                new DoencaLeve("Gripe"),
                new DoencaModerada("Dengue"),
                new DoencaGrave("COVID")
        );

        List<Local> locais = List.of(
                new Local("Centro"),
                new Local("Zona Sul"),
                new Local("Vila Nova")
        );

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Fazer um novo relato");
            System.out.println("2. Ver meus relatos");
            System.out.println("3. Buscar relatos por local");
            System.out.println("4. Buscar relatos por gravidade");
            System.out.println("5. Ver todos os relatos agrupados");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> {
                    // Escolher local
                    System.out.println("\nEscolha um local:");
                    for (int i = 0; i < locais.size(); i++) {
                        System.out.println(i + 1 + " - " + locais.get(i).getNome());
                    }
                    int localIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    Local localEscolhido = locais.get(localIndex);

                    // Escolher doença
                    System.out.println("\nEscolha uma doença:");
                    for (int i = 0; i < doencas.size(); i++) {
                        System.out.println(i + 1 + " - " + doencas.get(i).getNome() + " [" + doencas.get(i).getGrauDeRisco() + "]");
                    }
                    int doencaIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    Doenca doencaEscolhida = doencas.get(doencaIndex);

                    // Criar relato
                    Relato relato = new Relato(usuario, doencaEscolhida, localEscolhido);
                    usuario.adicionarRelato(relato);
                    todosRelatos.add(relato);
                    System.out.println("✅ Relato adicionado com sucesso!");
                }

                case 2 -> {
                    System.out.println("\n--- Seus relatos ---");
                    if (usuario.getRelatos().isEmpty()) {
                        System.out.println("Você ainda não fez nenhum relato.");
                    } else {
                        usuario.getRelatos().forEach(System.out::println);
                    }
                }

                case 3 -> {
                    System.out.println("\nDigite o nome do local:");
                    String nomeLocal = scanner.nextLine();
                    System.out.println("\n--- Relatos em " + nomeLocal + " ---");
                    todosRelatos.stream()
                            .filter(r -> r.getLocal().getNome().equalsIgnoreCase(nomeLocal))
                            .forEach(System.out::println);
                }

                case 4 -> {
                    System.out.println("\nDigite a gravidade (Leve, Moderada, Grave):");
                    String gravidade = scanner.nextLine();
                    System.out.println("\n--- Relatos com gravidade " + gravidade + " ---");
                    todosRelatos.stream()
                            .filter(r -> r.getDoenca().getGrauDeRisco().equalsIgnoreCase(gravidade))
                            .forEach(System.out::println);
                }

                case 5 -> {
                    System.out.println("\n--- Relatos agrupados (Doença + Local) ---");
                    Map<String, Integer> agrupados = new HashMap<>();
                    for (Relato r : todosRelatos) {
                        String chave = r.getDoenca().getNome() + " (" + r.getLocal().getNome() + ")";
                        agrupados.put(chave, agrupados.getOrDefault(chave, 0) + 1);
                    }
                    agrupados.forEach((k, v) -> System.out.println(k + " +" + v));
                }

                case 0 -> {
                    System.out.println("Até logo, " + usuario.getApelido() + "!");
                    return;
                }

                default -> System.out.println("Opção inválida!");
            }
        }
    }
}