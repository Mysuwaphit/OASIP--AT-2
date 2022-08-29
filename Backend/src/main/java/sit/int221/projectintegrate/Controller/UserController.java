package sit.int221.projectintegrate.Controller;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.DTO.SimpleUserDTO;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Repository.UserRepository;
import sit.int221.projectintegrate.Services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/api/users"})
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository repository;

    public UserController() {
    }

    @GetMapping({""})
    public List<User> getUserAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{userId}")
    public SimpleUserDTO getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody SimpleUserDTO newUser) {
        return this.userService.addUser(newUser);
    }

    @PostMapping("/match")
    public void login(@Validated @RequestBody SimpleLoginDTO body) {

        if(userService.matcher(body.getEmail(), body.getUserpassword()) == true){
            throw new ResponseStatusException(HttpStatus.OK,"Password Match!");
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password Not Match!");
        }

    }

//    @PostMapping("/login")
//    public String login(@Valid @RequestBody User email,User password) {
//        if(userService.checkEmail(email)) {
//                return "student Exists";
//            }
//        if (userService.checkPassword(password)){
//            return "Incorrect Password";
//             }
//        return null;
//    }

    @DeleteMapping({"/{userId}"})
    public void delete(@PathVariable Integer userId) {
        this.repository.findById(userId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, userId + " does not exist !!!");
        });
        this.repository.deleteById(userId);
    }

    @PutMapping({"/{userId}"})
    public User updateUser(@RequestBody User updateUser, @PathVariable Integer userId) {
        User user = this.repository.findById(userId).map((o) -> {
            return this.mapUser(o, updateUser);
        }).orElseGet(() -> {
            updateUser.setId(userId);
            return updateUser;
        });
        return repository.saveAndFlush(user);
    }

    private User mapUser(User existingUser, User updateUser) {
        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRoles(updateUser.getRoles());
        return existingUser;
    }
}

