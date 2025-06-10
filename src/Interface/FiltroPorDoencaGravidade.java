package Interface;

import model.Relato;

public class FiltroPorDoencaGravidade implements FiltroDeRelatos {

    public boolean aplicar(Relato relato) {
        return "Grave".equalsIgnoreCase(relato.getDoenca().getGrauDeRisco());
    }
}
