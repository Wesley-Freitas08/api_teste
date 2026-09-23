package api_teste.ds.controllers; // Define o pacote onde esta classe fica

import java.net.URI; // Monta a URL de resposta quando um recurso é criado
import java.util.List; // Permite trabalhar com listas de objetos

import org.springframework.http.ResponseEntity; // Monta a resposta HTTP completa (status, dados e cabeçalhos)
import org.springframework.validation.annotation.Validated; // Ativa as validações nos parâmetros dos métodos
import org.springframework.web.bind.annotation.DeleteMapping; // Escuta requisições do tipo DELETE (excluir)
import org.springframework.web.bind.annotation.GetMapping; // Escuta requisições do tipo GET (buscar dados)
import org.springframework.web.bind.annotation.PathVariable; // Pega valores que são passados direto na URL
import org.springframework.web.bind.annotation.PostMapping; // Escuta requisições do tipo POST (criar dados)
import org.springframework.web.bind.annotation.PutMapping; // Escuta requisições do tipo PUT (atualizar dados)
import org.springframework.web.bind.annotation.RequestBody; // Converte o JSON enviado na requisição em objeto Java
import org.springframework.web.bind.annotation.RequestMapping; // Define o caminho base de rota da classe
import org.springframework.web.bind.annotation.RestController; // Avisa ao Spring que a classe responde chamadas REST/JSON
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // Pega a URL atual para gerar o link do novo registro

import jakarta.validation.Valid; // Dispara as regras de validação anotadas no modelo Task
import api_teste.ds.models.Task; // Importa a entidade Task
import api_teste.ds.services.TaskService; // Importa a classe de serviço com as regras de negócio

@RestController // Diz que a classe devolve dados (JSON) e não páginas HTML
@RequestMapping("/task") // Rota principal: tudo aqui começará com /task
@Validated // Ativa a verificação de validações no controller
public class TaskController {
    
    private final TaskService taskService; // Guarda a referência do serviço de tarefas

    public TaskController(TaskService taskService) { // Construtor para injetar a dependência automaticamente
        this.taskService = taskService; // Salva o serviço recebido na variável da classe
    }

    @GetMapping("/{id}") // Mapeia GET /task/{id} para buscar por ID
    public ResponseEntity<Task> findById(@PathVariable Long id) { // Extrai o id da URL
        Task obj = this.taskService.findById(id); // Chama o serviço para buscar a tarefa no banco
        return ResponseEntity.ok().body(obj); // Retorna HTTP 200 (Sucesso) com a tarefa encontrada
    }

    @GetMapping("/user/{userId}") // Mapeia GET /task/user/{userId} para listar tarefas de um usuário
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId) { // Extrai o userId da URL
        List<Task> objs = this.taskService.findByUserId(userId); // Busca todas as tarefas daquele usuário
        return ResponseEntity.ok().body(objs); // Retorna HTTP 200 (Sucesso) com a lista de tarefas
    }

    @PostMapping // Mapeia POST /task para criar uma nova tarefa
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) { // Valida e recebe a tarefa em JSON
        this.taskService.create(obj); // Salva a tarefa no banco usando o serviço
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest() // Inicia a montagem da URL da nova tarefa
                .path("/{id}").buildAndExpand(obj.getId()).toUri(); // Adiciona o ID gerado ao final do link
        return ResponseEntity.created(uri).build(); // Retorna HTTP 201 (Criado) com o link no cabeçalho Location
    }

    @PutMapping("/{id}") // Mapeia PUT /task/{id} para atualizar uma tarefa existente
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id) { // Recebe dados e ID
        obj.setId(id); // Garante que o objeto use o mesmo ID passado na URL
        this.taskService.update(obj); // Executa a atualização através do serviço
        return ResponseEntity.noContent().build(); // Retorna HTTP 204 (Sem conteúdo, alterado com sucesso)
    }

    @DeleteMapping("/{id}") // Mapeia DELETE /task/{id} para deletar um registro
    public ResponseEntity<Void> delete(@PathVariable Long id) { // Pega o ID da tarefa a ser excluída
        this.taskService.delete(id); // Chama o método de exclusão no serviço
        return ResponseEntity.noContent().build(); // Retorna HTTP 204 (Sem conteúdo, excluído com sucesso)
    }
}