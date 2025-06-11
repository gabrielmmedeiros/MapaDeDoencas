package model;

import java.util.Objects; // Importar para Objects.hash e Objects.equals
import java.util.Date;
public class Relato {
    private int id; // gerado pelo banco
    private final Usuario usuario;
    private final Doenca doenca;
    private final Local local;
    private final Date data;

    // Construtor privado, seguindo o padrão
    private Relato(Usuario usuario, Doenca doenca, Local local, Date data) {
        if (usuario == null || doenca == null || local == null || data==null) {
            throw new IllegalArgumentException("Usuário, Doença e Local não podem ser nulos para um Relato.");
        }
        this.usuario = usuario;
        this.doenca = doenca;
        this.local = local;
        this.data = new Date(data.getTime());

    }

    // Metodo fábrica para criar um novo Relato (sem ID)
    public static Relato criar(Usuario usuario, Doenca doenca, Local local, Date data) {
        return new Relato(usuario, doenca, local, data);
    }

    // Metodo fábrica para reconstruir do banco (com ID)
    public static Relato reconstruir(int id, Usuario usuario, Doenca doenca, Local local, Date data) { // Adicionar data
        if (id <= 0) throw new IllegalArgumentException("ID inválido para reconstrução do Relato.");
        Relato r = new Relato(usuario, doenca, local, data);
        r.atribuirId(id);
        return r;
    }
    public int getId() {
        return id;
    }

    public Date getData() {
        // Retorna uma cópia defensiva porque java.util.Date é mutável
        return (this.data!= null? new Date(this.data.getTime()) : null);
    }
    void atribuirId(int id) { // package-private
        if (this.id > 0 && this.id!= id) { // Se já tem um ID positivo e o novo ID é diferente
            throw new IllegalStateException("Este objeto Relato já possui um ID (" + this.id + ") e não pode ser alterado para um ID diferente (" + id + ").");
        }
        if (this.id == 0 && id <= 0) { // Se ainda não tem ID (this.id == 0), o novo ID deve ser positivo
            throw new IllegalArgumentException("ID inválido (" + id + ") para um novo objeto Relato que ainda não possui ID.");
        }
        if (this.id > 0 && id <= 0) { // Não pode invalidar um ID existente
            throw new IllegalArgumentException("Não é possível atribuir um ID inválido (" + id + ") a um objeto Relato que já possui um ID válido (" + this.id + ").");
        }
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

    // equals e hashCode são CRUCIAIS para que Usuario.adicionarRelato (com verificação de duplicatas)
    // e Usuario.removerRelato funcionem corretamente.
    // Um relato é considerado igual a outro se tiver o mesmo usuário, doença e local.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relato relato)) return false;
        return Objects.equals(usuario, relato.usuario) &&
                Objects.equals(doenca, relato.doenca) &&
                Objects.equals(local, relato.local) &&
                Objects.equals(data, relato.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, doenca, local, data);
    }

    @Override
    public String toString() {
        return "Relato{" +
                "id=" + id +
                ", usuario='" + (usuario!= null? usuario.getApelido() : "null") + '\'' +
                ", doenca='" + (doenca!= null? doenca.getNome() : "null") + '\'' +
                ", local='" + (local!= null? local.getNome() : "null") + '\'' +
                ", data=" + data +
                '}';
    }
}