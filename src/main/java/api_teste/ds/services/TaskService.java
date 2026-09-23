package api_teste.ds.services; // Define o pacote onde esta classe de serviço de tarefas fica

import java.util.List; // Permite retornar coleções de tarefas
import java.util.Optional; // Trata possíveis retornos nulos de forma segura

import org.springframework.stereotype.Service; // Marca a classe como um serviço gerenciado pelo Spring
import org.springframework.transaction.annotation.Transactional; // Controla operações no banco de forma atômica e segura

import api_teste.ds.models.Task; // Importa a entidade Task (tarefa)
import api_teste.ds.models.User; // Importa a entidade User (usuário dono da tarefa)
import api_teste.ds.repositories.TaskRepository; // Importa a interface que faz operações no banco para Task

@Service // Registra a classe como camada de regras de negócio de tarefas
public class TaskService {
    
    private final TaskRepository taskRepository; // Guarda a referência do repositório de tarefas
    private final UserService userService; // Guarda a referência do serviço de usuários para validações

    public TaskService(TaskRepository taskRepository, UserService userService) { // Construtor que injeta as dependências necessárias
        this.taskRepository = taskRepository; // Salva a instância do repositório na variável da classe
        this.userService = userService; // Salva a instância do serviço de usuário na variável da classe
    }

    public Task findById(Long id) { // Busca uma tarefa individual pelo ID
        Optional<Task> task = this.taskRepository.findById(id); // Faz a pesquisa no banco de dados

        return task.orElseThrow(() -> new RuntimeException( // Devolve a tarefa ou dispara erro caso ela não exista
            "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName() // Mensagem formatada do erro
        ));
    }

    public List<Task> findByUserId(Long userId) { // Busca todas as tarefas associadas a um ID de usuário
        this.userService.findById(userId); // Verifica se o usuário existe (lança erro se não achar)

        return this.taskRepository.findByUser_Id(userId); // Executa a busca no repositório e devolve a lista
    }

    @Transactional // Abre uma transação no banco para garantir que tudo seja salvo com sucesso
    public Task create(Task obj) { // Recebe os dados da nova tarefa a ser criada
        User user = this.userService.findById(obj.getUser().getId()); // Confirma a existência do dono da tarefa

        obj.setId(null); // Define ID como nulo para forçar o banco a gerar um novo código
        obj.setUser(user); // Vincula o usuário completo encontrado à tarefa
        
        return this.taskRepository.save(obj); // Salva no banco de dados e retorna a tarefa recém-criada
    }

    @Transactional // Abre uma transação para atualizar os dados de forma isolada
    public Task update(Task obj) { // Recebe os dados alterados da tarefa
        Task newObj = findById(obj.getId()); // Garante que a tarefa a ser alterada existe no banco
        newObj.setDescription(obj.getDescription()); // Atualiza o texto da descrição com a nova informação

        return this.taskRepository.save(newObj); // Grava a alteração no banco e retorna a tarefa atualizada
    }

    public void delete(Long id) { // Exclui uma tarefa com base no seu ID
        findById(id); // Confere se a tarefa realmente existe antes de tentar deletar
        try { // Inicia o bloco de tentativa de exclusão
            this.taskRepository.deleteById(id); // Deleta o registro pelo ID informado
        } catch (Exception e) { // Trata qualquer falha ou impedimento do banco
            throw new RuntimeException("Não é possível excluir, pois há entidades relacionadas", e); // Erro de integridade relacional
        }
    }
}