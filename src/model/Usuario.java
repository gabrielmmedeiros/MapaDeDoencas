package model;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
/**
 * Esta classe é responsável por:
 * - Gerar seu apelido de forma aleatória.
 * - Criar instâncias válidas de Usuario.
 * - Armazenar relatos realizados por este usuario
 * - Esta classe **não gerencia outros usuários**.
 * - Ela **não sabe se um apelido já foi usado**.
 * - Garantir a **unicidade do apelido** é responsabilidade da classe GerenciadorDeNick.
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
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        this.id = id;
    }

    /**
     * Construtor privado. Impede criação direta de objetos fora da própria classe.
     * A única forma de criar um Usuario é por meio do metodo estático criar().
     */
    private Usuario(String apelido){
        this.apelido=apelido;
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
     * É responsabilidade GerenciadorDeNick validar isso.
     */
    public static Usuario criar(){//chama o construtor e cria o usuário com nome random
        String apelido = gerarApelidoAleatorio();
        return new Usuario(apelido);
    }
    //Retorna o apelido deste usuário.
    public String getApelido() {
        return apelido;
    }
    /**
     * Adiciona um novo relato à lista deste usuário.
     *
     * Este metodo recebe um objeto `Relato`, que representa a doença relatada e o local onde foi contraída.
     */
    public void adicionarRelato(Relato relato) {
        relatos.add(relato);
    }
    //Retorna uma cópia da lista de relatos feitos por este usuário.
    public List<Relato> getRelatos() {
        return new ArrayList<>(relatos); // para leitura externa
    }
}
