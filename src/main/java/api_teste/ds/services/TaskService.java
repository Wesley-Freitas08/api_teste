//Pacote onde esta a classe de serviço do projeto
package api_teste.ds.services;

//importa List da Biblioteca padrão do java para manipular colecoes de objetos
import java.util.List;
//importa Ópcional, usando para tratar valores que podem nao estar presentes (Evita NullExceptionPointer)
import java.util.Optional;


//importa a anotação do Spring para a injeção automatica de dependencias
import org.springframework.beans.factory.annotation.Autowired;
//importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring 
import  org.springframework.stereotype.Service;
//importa a anotação para gerenciar transações no banco de dados(garante atomicidade na operação)
import org.springframework.transaction.annotation.Transactional;

//importa o models.Task
import  api_teste.ds.models.Task;
//importa o models.User
import api_teste.ds.models.User;
import api_teste.ds.repositories.TaskRepository;


@Service 
public class TaskService {
    
    @Autowired 
    private TaskRepository taskRepository;
    @Autowired 
    private UserService userService;

public Task findById(Long Id){ 
    Optional<Task> task = this.taskRepository.findById(Id);

    return task.orElseThrow(()-> new RuntimeException(
        "Tafera não encotrada! Id" + Id + ",Tipo:" + Task.class.getName()
    ));
 }


public List<Task> findByUserId(Long UserId){
    this.userService.findyById(UserId);

   List<Task> tasks = this.taskRepository.findByUserId(UserId);

   return tasks;
}
    @Transactional 
    public Task create(Task obj){

        User user = this.userService.findById(obj.getUser().getId());

        obj.setId(Id:null);

        obj.setUser(user);
        
        obj = this.taskRepository.save(obj);

        return obj;
    }

    @Transactional 
    public Task update(Task obj){

        Task newObj = findById(obj.getId());

        newObj.setDescription(obj.getDescription());

        return  this.taskRepository.save(newObj);

    }


}