package sit.int221.projectintegrate.Services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.DTO.SimpleUserDTO;
import sit.int221.projectintegrate.Entities.Roles;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Exception.ValidationHandler;
import sit.int221.projectintegrate.Repository.UserRepository;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    private Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();

    public SimpleUserDTO getUserById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(user, SimpleUserDTO.class);
    }

    public User addUser(SimpleUserDTO newUser){
        User addUserList = modelMapper.map(newUser, User.class);
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id,12,24);
        String hash = argon2.hash(2,15*1024,1,addUserList.getUserpassword().toCharArray());
        addUserList.setUserpassword(hash);
        return repository.saveAndFlush(addUserList);
    }

//    public boolean matcher(String email,String password) {
//        User userRes = repository.findByEmail(email);
//        User user = userRes.getId();
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found Email!");
//        } else {
//            if (encoder.matches(password, user.getUserpassword()) == true) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
    public boolean checkRole(String newRole) {
        for (Roles role : Roles.values()){
//            System.out.println(role.toString());
            if(role.toString().equals(newRole) || newRole==null || newRole=="") {
                return true;
            }
        }
        return false;
    }




//    public boolean checkEmail(User user)  {
//        if(checkIfEmailExist(user.getEmail())){
//            throw new ResponseStatusException(HttpStatus.OK);
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public boolean checkPassword(User user)  {
//        if(checkIfPasswordExist(user.getUserpassword())){
//            throw new ResponseStatusException(HttpStatus.OK);
//        }else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    public boolean checkIfEmailExist(String email) {
//        return repository.findUserByEmail(email) !=null ? false : true;
//    }
//
//    public boolean checkIfPasswordExist(String password) {
//        return repository.findUserByPassword(password) !=null ? false : true;
//    }

}