package io.olatoye.githubsocial.repository;

import io.olatoye.githubsocial.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByGithubUserId(long userId);
}
