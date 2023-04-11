package io.olatoye.githubsocial.repository;

import io.olatoye.githubsocial.domain.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthUserRepository extends MongoRepository<AuthUser, String> {

    Optional<AuthUser> findByEmail(String email);
}
