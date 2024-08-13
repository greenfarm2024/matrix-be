package ch.thgroup.matrix.business.repository;

import ch.thgroup.matrix.business.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Find all users.
     *
     * @return List of all users.
     */
    List<UserEntity> findAll();

    /**
     * Find user by id.
     *
     * @param userId User id.
     * @return User entity.
     */
    Optional<UserEntity> findById(Long userId);
}