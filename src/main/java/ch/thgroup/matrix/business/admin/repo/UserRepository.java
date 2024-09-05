package ch.thgroup.matrix.business.admin.repo;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findByUserName(String userName);
}