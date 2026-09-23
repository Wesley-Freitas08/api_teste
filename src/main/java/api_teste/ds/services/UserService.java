package api_teste.ds.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_teste.ds.models.User;
import api_teste.ds.repositories.TaskRepository;
import api_teste.ds.repositories.UserRepository;

@Service 
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // Injeção via construtor (elimina avisos do @Autowired)
    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    @Transactional 
    public User create(User obj) {
        obj.setId(null);
        obj = this.userRepository.save(obj);

        // Se o usuário já contiver tarefas associadas na criação:
        if (obj.getTasks() != null && !obj.getTasks().isEmpty()) {
            this.taskRepository.saveAll(obj.getTasks());
        }

        return obj;
    }

    @Transactional 
    public User update(User obj) {
        User newObj = findById(obj.getId());

        // Atualize os campos próprios de User (ex: senha)
        newObj.setPassword(obj.getPassword());

        // Salve no repositório de Usuário, não de Task
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir, pois há entidades relacionadas", e);
        }
    }
}