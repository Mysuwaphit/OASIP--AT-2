package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.DTO.SimpleUserDTO;
import sit.int221.projectintegrate.Entities.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer>{
//    User findByEmail(String email);
    Optional<User> findByEmail(String email);

//    List<User> findAll();
//
//    User findByEmail(String email);
//
//    SimpleUserDTO findUserByEmail(String email);
}
