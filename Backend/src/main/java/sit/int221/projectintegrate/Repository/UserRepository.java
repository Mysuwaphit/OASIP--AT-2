package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.projectintegrate.Entities.User;


public interface UserRepository extends JpaRepository<User,Integer>{

}
