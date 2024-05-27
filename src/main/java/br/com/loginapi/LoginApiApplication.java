package br.com.loginapi;

import br.com.loginapi.repository.MongoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginApiApplication {

    public static void main(String[] args) {
        MongoConfig.conectar();

        SpringApplication.run(LoginApiApplication.class, args);
    }

}
