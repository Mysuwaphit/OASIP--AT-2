package sit.int221.projectintegrate.Controller;
import java.util.Map.Entry;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.impl.DefaultClaims;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.*;
import sit.int221.projectintegrate.Entities.Roles;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Exception.ValidationHandler;
import sit.int221.projectintegrate.Repository.JwtUserRepository;
import sit.int221.projectintegrate.Repository.UserRepository;
import sit.int221.projectintegrate.Services.CustomUserDetailsService;
import sit.int221.projectintegrate.Services.UserService;
import sit.int221.projectintegrate.Util.JwtUtil;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/users"})
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository repository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    Argon2PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    JwtUserRepository userRepository;

    public UserController() {
    }

    @GetMapping({""})
    @PreAuthorize("hasRole('admin')")
    public List<User> getUserAll() {
        return this.repository.findAll();
    }


//    @GetMapping("/info")
//    public User getUserDetails(){
//        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return repository.findByEmail(email).get();
//    }

    @GetMapping("/{userId}")
    public SimpleUserDTO getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

//    @PostMapping({""})
//    @ResponseStatus(HttpStatus.CREATED)
//    public User create(@Valid @RequestBody SimpleUserDTO newUser) {
//        return this.userService.addUser(newUser);
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User addUserList = modelMapper.map(signUpRequest, User.class);
        if(userRepository.findUsersByEmail(signUpRequest.getEmail()) != null) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(!userService.checkRole(addUserList.getRoles())) {
            return ValidationHandler.showError(HttpStatus.BAD_REQUEST, "Don't have this role , Please re-enter the role.");
        }
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id,12,24);
        String hash = argon2.hash(2,15*1024,1,addUserList.getUserpassword().toCharArray());
        // Creating user's account
        User jwtUser = new User();
        jwtUser.setUsername(signUpRequest.getUsername());
        jwtUser.setEmail(signUpRequest.getEmail());
        jwtUser.setUserpassword(hash);
        jwtUser.setRoles(signUpRequest.getRoles());
        userRepository.save(jwtUser);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
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

