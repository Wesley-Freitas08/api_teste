package api_teste.ds.configs;

import org.springframework.context.annotation.Configuration; //Importa a anotação do Spring Container
import org.springframework.web.servlet.config.annotation.CorsRegistry; //importa a classe responsável por registrar as regras do CORS
import org.springframework.web.servlet.config.annotation.EnableWebMvc; //Importa a anotação que habilita os recursos do Spring web MVC
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;  //Importa a interfcae de customização do Spring MVC

@Configuration 
@EnableWebMvc

public class WebConfig implements WebMvcConfigurer{ //Classe de configuração que implementa o contrato de customização do Spring

    @Override 
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**");
    }
}