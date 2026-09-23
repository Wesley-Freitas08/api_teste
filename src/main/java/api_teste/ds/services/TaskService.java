package api_teste.ds.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_teste.ds.models.Task;
import api_teste.ds.models.User;
import api_teste.ds.repositories.TaskRepository;

@Service 
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserService userService;

    // Injeção via construtor (remove os alertas amarelos do @Autowired)
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task findById(Long id) { 
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()
        ));
    }

    public List<Task> findByUserId(Long userId) {
        // Corrigido o erro de digitação (era findyById)
        this.userService.findById(userId);

        return this.taskRepository.findByUser_Id(userId);
    }

    @Transactional 
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());

        obj.setId(null);
        obj.setUser(user);
        
        return this.taskRepository.save(obj);
    }

    @Transactional 
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());

        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir, pois há entidades relacionadas", e);
        }
    }
}