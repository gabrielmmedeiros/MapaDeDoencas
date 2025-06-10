package model;

public class DoencaLeve extends Doenca {
    public DoencaLeve(String nome) {
        super(nome);
    }

    public String getGrauDeRisco() {
        return "Leve";
    }
}
