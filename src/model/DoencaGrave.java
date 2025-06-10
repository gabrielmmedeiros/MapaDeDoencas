package model;

public class DoencaGrave extends Doenca {
    public DoencaGrave(String nome) {
        super(nome);
    }

    public String getGrauDeRisco() {
        return "Grave";
    }
}
