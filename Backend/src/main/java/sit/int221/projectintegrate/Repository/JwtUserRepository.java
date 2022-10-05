package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.Entities.User;

import java.util.List;

public interface JwtUserRepository extends JpaRepository<User,Integer> {
    User findUsersByEmail(String email);
    List<User> findAll();
    User findByEmail(String email);
}
