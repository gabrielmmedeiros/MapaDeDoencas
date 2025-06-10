package main;

import model.Usuario;
import model.GerenciadorDeNick;

public class Main {
    public static void main(String[] args) {
        GerenciadorDeNick gerenciador = new GerenciadorDeNick();

        for (int i = 1; i <= 1000; i++) {
            Usuario usuario = gerenciador.criarUsuarioUnico();
            System.out.println("Usuário " + i + ": " + usuario.getApelido());
        }

        System.out.println("Total de apelidos únicos: " + gerenciador.quantidade());
    }
}
