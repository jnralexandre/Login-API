package br.com.loginapi.repository;

import br.com.loginapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
