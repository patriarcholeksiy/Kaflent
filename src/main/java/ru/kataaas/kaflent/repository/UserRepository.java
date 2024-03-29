package ru.kataaas.kaflent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kataaas.kaflent.entity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT u.id FROM users u WHERE u.username = :username", nativeQuery = true)
    Long findIdByUsername(@Param("username") String username);

    UserEntity findByUsernameOrEmail(String username, String email);

    UserEntity findByUsername(String username);

    List<UserEntity> findAllByIdIsInOrderByUsernameAsc(List<Long> ids);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
