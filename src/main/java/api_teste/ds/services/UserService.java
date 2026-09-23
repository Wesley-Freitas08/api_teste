package api_teste.ds.services; // Define o pacote onde esta classe de serviço fica

import java.util.Optional; // Trata valores que podem existir ou ser nulos de forma segura

import org.springframework.stereotype.Service; // Marca a classe como um serviço gerenciado pelo Spring
import org.springframework.transaction.annotation.Transactional; // Garante que o método execute dentro de uma transação no banco

import api_teste.ds.models.User; // Importa o modelo User que representa a tabela de usuários
import api_teste.ds.repositories.TaskRepository; // Importa o repositório para salvar ou consultar tarefas
import api_teste.ds.repositories.UserRepository; // Importa o repositório para salvar ou consultar usuários

@Service // Indica ao Spring que aqui estão as regras de negócio de usuário
public class UserService {

    private final UserRepository userRepository; // Guarda a referência do repositório de usuários
    private final TaskRepository taskRepository; // Guarda a referência do repositório de tarefas

    public UserService(UserRepository userRepository, TaskRepository taskRepository) { // Injeta as dependências pelo construtor
        this.userRepository = userRepository; // Atribui o repositório de usuários à variável da classe
        this.taskRepository = taskRepository; // Atribui o repositório de tarefas à variável da classe
    }

    public User findById(Long id) { // Busca um usuário pelo ID informado
        Optional<User> user = this.userRepository.findById(id); // Faz a busca no banco retornando um Optional

        return user.orElseThrow(() -> new RuntimeException( // Devolve o usuário ou lança erro se não achar
            "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName() // Mensagem informativa do erro
        ));
    }

    @Transactional // Garante que a criação seja confirmada ou revertida por completo no banco
    public User create(User obj) { // Recebe o novo usuário a ser salvo
        obj.setId(null); // Força o ID como nulo para garantir que o banco crie um novo registro
        obj = this.userRepository.save(obj); // Salva o usuário e recebe o objeto com o ID gerado

        if (obj.getTasks() != null && !obj.getTasks().isEmpty()) { // Verifica se veio alguma tarefa junto na lista
            this.taskRepository.saveAll(obj.getTasks()); // Salva todas as tarefas vinculadas a esse usuário
        }

        return obj; // Retorna o usuário criado com sucesso
    }

    @Transactional // Garante atomicidade na atualização dos dados
    public User update(User obj) { // Recebe os dados alterados do usuário
        User newObj = findById(obj.getId()); // Confere se o usuário realmente existe antes de alterar

        newObj.setPassword(obj.getPassword()); // Atualiza apenas a senha com o novo valor enviado

        return this.userRepository.save(newObj); // Grava a alteração no banco e retorna o usuário atualizado
    }

    public void delete(Long id) { // Remove um usuário pelo ID
        findById(id); // Verifica se o usuário existe (lança erro caso não encontre)
        try { // Tenta executar a exclusão no banco de dados
            this.userRepository.deleteById(id); // Deleta o registro do usuário pelo ID
        } catch (Exception e) { // Captura qualquer falha durante a exclusão
            throw new RuntimeException("Não é possível excluir, pois há entidades relacionadas", e); // Avisa sobre relacionamentos presos
        }
    }
}