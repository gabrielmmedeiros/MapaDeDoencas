package model;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Date;
/**
 * Esta classe é responsável por:
 * - Gerar seu apelido de forma aleatória.
 * - Criar instâncias válidas de Usuario.
 * - Armazenar relatos realizados por este usuario
 * - Esta classe **nao gerencia outros usuarios**.
 * - Ela **nao sabe se um apelido já foi usado**.
 * - Garantir a **unicidade do apelido** é responsabilidade da classe GerenciadorDeApelido.
 */

public class Usuario {
    private int id;// sera preenchido pelo banco via DAO

    private final List<Relato> relatos = new ArrayList<>();

    private final String apelido;

    private static final String[] ADJETIVOS = {"Curioso","Solitario","Assanhado","Furioso"};

    private static final String[] SUBSTANTIVOS ={"Barco","Padaria","Galo","Tigrao"};


     //Retorna o ID do usuário (definido após inserção no banco de dados).
    public int getId() {
        return id;
    }

    //Define o ID do usuário. Usado apenas pelo DAO após inserir no banco.


    /**
     * Construtor privado. Impede criação direta de objetos fora da própria classe.
     * A única forma de criar um Usuario é por meio do metodo estático criar().
     */
    private Usuario(String apelido) {
        if (apelido == null || apelido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apelido inválido.");
        }
        this.apelido = apelido.trim();
    }
    //Gera um apelido aleatório
    private static String gerarApelidoAleatorio(){
        Random rand = new Random();
        String adj = ADJETIVOS[rand.nextInt(ADJETIVOS.length)];
        String sub = SUBSTANTIVOS[rand.nextInt(SUBSTANTIVOS.length)];
        int numero = rand.nextInt(100)+1;
        return  sub + adj + "_" + numero;
    }
    /**
     * Cria uma nova instância de Usuario com apelido gerado aleatoriamente pelo gerarApelidoAleatorio.
     *
     * Atenção: este metodo **não garante** que o apelido seja único.
     * É responsabilidade GerenciadorDeApelido validar isso.
     */
    public static Usuario criar(){//chama o construtor e cria o usuário com nome random
        String apelido = gerarApelidoAleatorio();
        return new Usuario(apelido);
    }
    //Retorna o apelido deste usuário.
    public String getApelido() {
        return apelido;
    }

    void atribuirId(int id) {
        if (this.id > 0 && this.id!= id) { // Permite reatribuir o mesmo ID, mas não um diferente se já tiver um > 0
            throw new IllegalStateException("Este objeto já possui um ID atribuído anteriormente e diferente do fornecido.");
        }
        if (this.id == 0 && id <= 0) { // Se ainda não tem ID, o novo ID deve ser positivo
            throw new IllegalArgumentException("ID inválido para um novo objeto.");
        }
        if (id <= 0 && this.id > 0) { // Não pode setar para um ID inválido se já tinha um válido
            throw new IllegalArgumentException("Não é possível atribuir um ID inválido a um objeto que já possui ID.");
        }
        this.id = id;
    }
    //Para o DAO conseguir puxar o nome criado para o banco
    public static Usuario reconstruir(int id, String apelido) {
        Usuario u = new Usuario(apelido);
        u.atribuirId(id);
        return u;
    }
    /**
     * Adiciona um novo relato à lista deste usuário.
     *
     * Este metodo recebe um objeto `Relato`, que representa a doença relatada e o local onde foi contraída.
     */
    public void adicionarRelato(Relato relato) {
        Objects.requireNonNull(relato, "Relato não pode ser nulo");
        if (!this.relatos.contains(relato)) { // Verifica se o relato NÃO está na lista
            this.relatos.add(relato);        // Adiciona SOMENTE SE não estiver na lista
        }
    }
    /**
     * Cria e adiciona um novo relato à lista deste usuário, se ainda não existir um idêntico.
     * Este metodo é uma SOBRECARGA de adicionarRelato(Relato relato).
     *
     * @param doenca A doença a ser relatada (não pode ser nula).
     * @param local O local onde a doença foi contraída (não pode ser nulo).
     * @param data A data do relato (não pode ser nula).
     */
    public void adicionarRelato(Doenca doenca, Local local, Date data) {
        Objects.requireNonNull(doenca, "Doença não pode ser nula para adicionar relato.");
        Objects.requireNonNull(local, "Local não pode ser nulo para adicionar relato.");
        Objects.requireNonNull(data, "Data não pode ser nula para adicionar relato.");

        // Cria o objeto Relato usando o metodo fábrica da classe Relato.
        // Passa 'this' como o usuário atual.
        Relato novoRelato = Relato.criar(this, doenca, local, data);

        // Reutiliza a lógica do metodo original para adicionar e verificar duplicatas.
        this.adicionarRelato(novoRelato);
    }

    public boolean removerRelato(Relato relato) {
        Objects.requireNonNull(relato, "Relato a ser removido não pode ser nulo.");
        boolean removido = this.relatos.remove(relato);
        return this.relatos.remove(relato);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario that)) return false; // Pattern matching

        // Se ambos os objetos têm IDs válidos (presumivelmente do banco), compare por ID.
        if (this.id > 0 && that.id > 0) {
            return this.id == that.id;
        }
        // Se um ou ambos não têm ID (são objetos novos em memória), compare por apelido.
        // Isso assume que o apelido é único para objetos não persistidos (GerenciadorDeApelido).
        return Objects.equals(this.apelido, that.apelido);
    }

    @Override
    public int hashCode() {
        // Consistente com equals: se tem ID, o hashcode é baseado nele.
        if (this.id > 0) {
            return Objects.hash(this.id);
        }
        // Senão, baseado no apelido.
        return Objects.hash(this.apelido);
    }
}
