package api_teste.ds.models; // Define o pacote onde esta classe de modelo fica

import jakarta.persistence.Column; // Mapeia o atributo como uma coluna da tabela no banco
import jakarta.persistence.Entity; // Indica que a classe representa uma tabela gerenciada pelo JPA
import jakarta.persistence.GeneratedValue; // Define a estratégia de geração automática de valores
import jakarta.persistence.GenerationType; // Especifica o tipo de incremento da chave primária (ex: auto_increment)
import jakarta.persistence.Id; // Marca o atributo como a chave primária da tabela
import jakarta.persistence.JoinColumn; // Define a coluna de chave estrangeira que faz a ligação no banco
import jakarta.persistence.ManyToOne; // Define relacionamento de muitos para um (muitas tarefas para um usuário)
import jakarta.persistence.Table; // Configura o nome da tabela física no banco de dados
import jakarta.validation.constraints.NotBlank; // Não permite que o texto seja nulo ou composto só por espaços
import jakarta.validation.constraints.NotNull; // Impede que o valor seja nulo
import jakarta.validation.constraints.Size; // Delimita a quantidade mínima e máxima de caracteres permitidos
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects; // Fornece utilitários para gerar métodos equals e hashCode com segurança

import org.springframework.web.bind.annotation.SessionAttributes;

@Entity // Diz ao JPA que esta classe é uma entidade do banco de dados
@Table(name = Task.TABLE_NAME) // Define o nome da tabela no banco usando a constante TABLE_NAME
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
@EqualsAndHashCode 

public class Task {

    public static final String TABLE_NAME = "task"; // Nome fixo da tabela no banco de dados

    @Id // Marca como chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Faz o banco auto-incrementar o ID a cada novo registro
    @Column(name = "id", unique = true) // Mapeia para a coluna 'id' e garante valor exclusivo
    private Long id; // Código identificador da tarefa

    @ManyToOne // Indica que várias tarefas pertencem a um único usuário
    @JoinColumn(name = "user_id", nullable = false, updatable = false) // Chave estrangeira 'user_id', obrigatória e imutável
    private User user; // Objeto do usuário dono desta tarefa

    @Column(name = "description", length = 255, nullable = false) // Coluna 'description' de até 255 caracteres e não nula
    @NotNull // Garante que a descrição não seja nula nas validações
    @NotBlank // Garante que a descrição não fique vazia nem contenha só espaços
    @Size(min = 1, max = 255) // Exige que o texto tenha entre 1 e 255 caracteres
    private String description; // Texto descritivo da tarefa

   
}