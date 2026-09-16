package api_teste.ds.services;

//importa List da Biblioteca padrão do java para manipular colecoes de objetos
import java.util.List;
//importa Ópcional, usando para tratar valores que podem nao estar presentes (Evita NullExceptionPointer)
import java.util.Optional;

import api_teste.ds.models.User;
//importa a anotação do Spring para a injeção automatica de dependencias
import org.springframework.beans.factory.annotation.Autowired;
//importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring 
import org.springframework.stereotype.Service;
//importa a anotação para gerenciar transações no banco de dados(garante atomicidade na operação)
import org.springframework.transaction.annotation.Transactional;


//importa o models.Task
import api_teste.ds.models.Task;
//importa a interface do repositório de tarefas para realizar operações de persistência no banco de dados
import api_teste.ds.repositories.TaskRepository;
//importa a interface do repositório de tarefas para realizar operações de persistência no banco de dados
import api_teste.ds.repositories.UserRepository;


//Anotação que indica no Spring que essa classe contem as regras de negocios da entidade user
@Service 
public class UserService {
 
    //Injeta automaticamente a instância do repositório de tarefas para acesso aos dados
    @Autowired 
    private UserRepository userRepository;


    //Injeta automaticamente a instância do repositório de tarefas para acesso aos dados
    @Autowired 
    private TaskRepository taskRepository;
    
public User findById(long Id){

    Optional<User> user = this.userRepository.findById(Id);

        return user.orElseThrow(()-> new RuntimeException(
            "Usuario não encontrado!" + Id +", Tipo:" + User.class.getName()
      ));
}

 //Garante que a criação do registro seja feita em uma transação segura com o banco de dados
 @Transactional 
 public User create(User obj){
    
     //Garante que o ID seja nulo para forçar a criação de um novo registro e evitar sobreposição
     obj.setId(null);

     obj = this.userRepository.save(obj);
 //Persiste a nova tarefa no banco de dados e atualiza o objeto com o ID gerado
 obj = this.taskRepository.saveAll(obj.getClass());

 //Retorna a tarefa recém-criada
 return obj;
}

    @Transactional 
    
    public User update(User obj){

        User newObj = findById(obj.getId());



    }






}