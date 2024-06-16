package org.gib.persistence;

import org.gib.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity findByEmail(String email);
}
