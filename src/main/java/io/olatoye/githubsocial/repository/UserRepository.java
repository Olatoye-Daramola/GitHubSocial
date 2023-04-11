package io.olatoye.githubsocial.repository;

import io.olatoye.githubsocial.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
