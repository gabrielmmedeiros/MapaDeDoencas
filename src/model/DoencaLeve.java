package model;

public class DoencaLeve extends Doenca {
    DoencaLeve(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Leve";
    }
}
