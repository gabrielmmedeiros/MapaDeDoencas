package model;


public class Relato {
    private int id; // gerado pelo banco
    private final Usuario usuario;
    private final Doenca doenca;
    private final Local local;

    private Relato(Usuario usuario, Doenca doenca, Local local) {
        if (usuario == null || doenca == null || local == null) {
            throw new IllegalArgumentException("Nenhum campo pode ser nulo.");
        }
        this.usuario = usuario;
        this.doenca = doenca;
        this.local = local;
    }
    public static Relato criar(Usuario usuario, Doenca doenca, Local local) {
        return new Relato(usuario, doenca, local);
    }


    public int getId() {
        return id;
    }
    // reconstrução com ID (vindo do banco)
    public static Relato reconstruir(int id, Usuario usuario, Doenca doenca, Local local) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Relato r = new Relato(usuario, doenca, local);
        r.id = id;
        return r;
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
                "usuario='" + usuario.getApelido() + '\'' +
                ", doenca='" + doenca.getNome() + '\'' +
                ", local='" + local.getNome() + '\'' +
                '}';
    }
}
