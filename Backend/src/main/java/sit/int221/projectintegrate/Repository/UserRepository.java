package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.projectintegrate.Entities.User;



public interface UserRepository extends JpaRepository<User,Integer>{
    User findByEmail(String email);

}
