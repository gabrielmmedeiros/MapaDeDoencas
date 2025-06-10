package Interface;
import model.Relato;

public class FiltroPorLocal implements FiltroDeRelatos{
    private final String nomeDoLocal;
    public FiltroPorLocal (String nomeDoLocal){
        this.nomeDoLocal=nomeDoLocal.trim();
    }
    public boolean aplicar(Relato relato) {
        return relato.getLocal().getNome().equalsIgnoreCase(nomeDoLocal);
    }
}
