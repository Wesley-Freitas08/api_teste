package api_teste.ds.controllers;

import java.net.URI; //Importa a classe URI para contruir e manipular HTTP de novos recursos
import org.springframework.beans.factory.annotation.Autowired; //Importa a anotação do Spring para a injeção automática de dependencias
import org.springframework.http.ResponseEntity; //Importa a classe ResponseEntity para manipular respostas HTTP
import org.springframework.validation.annotation.Validated; //Importa a anotação para validar os dados de entrada
import org.springframework.web.bind.annotation.DeleteMapping; //Importa a anotação para mapear requisições HTTP DELETE
import org.springframework.web.bind.annotation.GetMapping; //Importa a anotação para mapear requisições HTTP GET
import org.springframework.web.bind.annotation.PathVariable; //Importa a anotação para extrair variáveis de caminho da URL
import org.springframework.web.bind.annotation.PostMapping; //Importa a anotação para mapear requisições HTTP POST
import org.springframework.web.bind.annotation.PutMapping; //Importa a anotação para mapear requisições HTTP PUT
import org.springframework.web.bind.annotation.RequestBody; //converte JSON em OBJ JAVA
import org.springframework.web.bind.annotation.RequestMapping; //Importa a anotação para mapear requisições HTTP para um caminho específico
import org.springframework.web.bind.annotation.RestController; //Importa a anotação para definir a classe como um controlador REST
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; //Importa a classe para construir URIs de recursos

import api_teste.ds.models.User; //Importa a classe User do pacote models
import api_teste.ds.models.User.CreateUser;
import api_teste.ds.models.User.UpdateUser;
import api_teste.ds.services.UserService; //Importa a classe UserService do pacote services

@RestController //Anotação que indica que essa classe é um controlador REST, capaz de receber requisições HTTP e retornar respostas HTTP
@RequestMapping ("/user") //Anotação que define o caminho base para todas as requisições mapeadas nesse controlador
@Validated 

public class UserController {
    
    @Autowired 
    private UserService userService; //Injeta automaticamente a instancia do UserService gerenciado pelo Spring

    @GetMapping ("/{id}") //Anotação que mapeia requisições HTTP GET para o método findById, com um parâmetro de caminho {id}
    public ResponseEntity<User> findById(@PathVariable Long id){ //Método que recebe o id do usuário como parâmetro de caminho
        User obj = this.userService.findById(id); //Chama o método findById do UserService para buscar o usuário no banco de dados
        return ResponseEntity.ok().body(obj); //Retorna uma resposta HTTP 200 OK com o objeto User no corpo da resposta
    }

    @PostMapping 
    public ResponseEntity<Void> create(@Validated (CreateUser.class) @RequestBody User obj){ //Método que recebe um objeto User no corpo da requisição, validado com a anotação @Validated
    this.userService.create(obj); //Chama o método create do UserService para salvar o usuário no banco de dados
    URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri(); //Constrói a URI do novo recurso criado.path("/{id}").buildAndExpand(obj.getId()).toUri(); //Constrói a URI do novo recurso criado
       return ResponseEntity.created(url).build(); //Retorna uma resposta HTTP 201 Created com a URI do novo recurso no cabeçalho Location

    }

    @PutMapping("/{id}") // Mapeia requisições HTTP na rota base "/user/{id}" (atualização do usuario)
  public ResponseEntity<Void> update(@Validated(UpdateUser.class)@RequestBody User obj, @PathVariable Long id){
    obj.setId(id);
    this.userService.update(obj);
    return ResponseEntity.noContent().build(); //Retorna codigo http 204(no content) indicando sucesso
  } 

  @DeleteMapping ("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}


