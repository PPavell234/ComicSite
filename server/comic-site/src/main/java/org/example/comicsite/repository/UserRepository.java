package org.example.comicsite.repository;

import org.example.comicsite.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}