package api_teste.ds.models; // Define o pacote onde o modelo fica

import com.fasterxml.jackson.annotation.JsonProperty; // Controla como o campo é serializado/deserializado em JSON
import jakarta.persistence.Column; // Mapeia os atributos para colunas da tabela
import jakarta.persistence.Entity; // Informa que a classe é uma tabela do banco de dados (JPA)
import jakarta.persistence.GeneratedValue; // Define a estratégia de geração da chave primária
import jakarta.persistence.GenerationType; // Especifica o tipo de geração do ID (ex: IDENTITY)
import jakarta.persistence.Id; // Marca o atributo como chave primária da tabela
import jakarta.persistence.OneToMany; // Define relacionamento de um para muitos com a entidade Task
import jakarta.persistence.Table; // Especifica o nome da tabela no banco
import jakarta.validation.constraints.NotBlank; // Garante que o texto não seja nulo e nem vazio/espaços
import jakarta.validation.constraints.NotNull; // Garante que o valor não seja nulo
import jakarta.validation.constraints.Size; // Define tamanho mínimo e máximo de caracteres
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList; // Estrutura de lista dinâmica
import java.util.List; // Interface de listas em Java
import java.util.Objects; // Métodos utilitários para equals e hashCode

@Entity // Marca a classe como uma entidade gerenciada pelo JPA
@Table(name = User.TABLE_NAME) // Define o nome da tabela física no banco
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
@EqualsAndHashCode 

public class User {

    // =========================================================================
    // Interfaces de Grupo de Validação (usadas nos @Validated do UserController)
    // =========================================================================
    public interface CreateUser {} // Grupo usado para validar dados na rota de criação (POST)
    public interface UpdateUser {} // Grupo usado para validar dados na rota de atualização (PUT)

    public static final String TABLE_NAME = "user"; // Nome constante da tabela no banco de dados

    @Id // Marca como chave primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID como auto-incremento no MySQL
    @Column(name = "id", unique = true) // Configura a coluna 'id' como única
    private Long id; // Identificador único do usuário

    @Column(name = "username", length = 100, nullable = false, unique = true) // Mapeia a coluna 'username'
    @NotNull(groups = CreateUser.class) // Não permite nulo apenas na criação do usuário
    @NotBlank(groups = CreateUser.class) // Não permite vazio apenas na criação do usuário
    @Size(groups = CreateUser.class, min = 2, max = 100) // Exige de 2 a 100 caracteres na criação
    private String username; // Nome de login do usuário

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Recebe no JSON de entrada, mas nunca envia de volta
    @Column(name = "password", length = 60, nullable = false) // Mapeia a coluna 'password' no banco
    @NotNull(groups = {CreateUser.class, UpdateUser.class}) // Obrigatório tanto ao criar quanto ao alterar
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}) // Não permite espaços tanto ao criar quanto ao alterar
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60) // Exige de 8 a 60 caracteres em ambos
    private String password; // Senha criptografada do usuário

    @OneToMany(mappedBy = "user") // Mapeia que um usuário possui várias tarefas (campo 'user' em Task)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Evita loop infinito de recursão na serialização JSON
    private List<Task> tasks = new ArrayList<>(); // Lista de tarefas vinculadas a este usuário

  
}