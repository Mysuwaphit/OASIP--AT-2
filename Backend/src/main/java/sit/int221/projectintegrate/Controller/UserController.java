package sit.int221.projectintegrate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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


//    @PostMapping({""})
//    @ResponseStatus(HttpStatus.CREATED)
//    public User create(@Valid @RequestBody User newUser) {
//        return this.repository.saveAndFlush(newUser);
//    }
    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody SimpleUserDTO newUser) {
        return this.userService.addUser(newUser);
    }

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

