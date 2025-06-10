package model;


public class Relato {
    private int id; // gerado pelo banco
    private final Usuario usuario;
    private final Doenca doenca;
    private final Local local;

    public Relato(Usuario usuario, Doenca doenca, Local local) {
        if (usuario == null || doenca == null || local == null) {
            throw new IllegalArgumentException("Nenhum campo pode ser nulo.");
        }
        this.usuario = usuario;
        this.doenca = doenca;
        this.local = local;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Doenca getDoenca() {
        return doenca;
    }

    public Local getLocal() {
        return local;
    }

    public String toString() {
        return "Relato{" +
                "usuario=" + usuario.getApelido() +
                doenca.getNome() +
                ", local=" + local.getNome() +
                '}';
    }
}
