package ch.thgroup.matrix.business.repository;


import ch.thgroup.matrix.business.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<OurUsers, Integer> {

    Optional<OurUsers> findByEmail(String email);
}
