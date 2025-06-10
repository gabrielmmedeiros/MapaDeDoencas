package main;
import model.*;
import Interface.IdentificavelPorNome;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // 1. Usuário entra no sistema
        Usuario usuario = Usuario.criar();
        System.out.println("Apelido gerado: " + usuario.getApelido());

        // 2. Lista de doenças disponíveis
        List<Doenca> doencas = List.of(
                new DoencaLeve("Gripe"),
                new DoencaLeve("sirilanca"),
                new DoencaModerada("Dengue"),
                new DoencaGrave("COVID")
        );

        // 3. Lista de locais disponíveis
        List<Local> locais = List.of(
                new Local("Centro"),
                new Local("Zona Sul"),
                new Local("Vila Nova")
        );

        // 4. Exibir doenças por gravidade
        System.out.println("\n--- Doenças Leves ---");
        for (Doenca d : doencas) {
            if (d.getGrauDeRisco().equalsIgnoreCase("Leve")) {
                System.out.println(d.getNome());
            }
        }

        // 5. Criar um relato (usuário pega COVID no Centro)
        Doenca doencaSelecionada = doencas.get(2); // COVID
        Local localSelecionado = locais.get(0);    // Centro
        Relato relato = new Relato(usuario, doencaSelecionada, localSelecionado);
        usuario.adicionarRelato(relato);

        // 6. Exibir relatos do usuário
        System.out.println("\n--- Relatos do usuário ---");
        for (Relato r : usuario.getRelatos()) {
            System.out.println(r);
        }

        // 7. Filtrar relatos por local "Centro"
        System.out.println("\n--- Relatos no Centro ---");
        List<Relato> todosRelatos = usuario.getRelatos(); // se tivesse mais usuários, uniria listas
        for (Relato r : todosRelatos) {
            if (r.getLocal().getNome().equalsIgnoreCase("Centro")) {

            }
        }
    }
}