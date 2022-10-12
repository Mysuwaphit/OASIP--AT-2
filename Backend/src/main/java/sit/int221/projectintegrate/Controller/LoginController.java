package sit.int221.projectintegrate.Controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.AuthenticationResponse;
import sit.int221.projectintegrate.DTO.LoginRequest;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Repository.JwtUserRepository;
import sit.int221.projectintegrate.Repository.UserRepository;
import sit.int221.projectintegrate.Services.CustomUserDetailsService;
import sit.int221.projectintegrate.Services.UserService;
import sit.int221.projectintegrate.Util.JwtUtil;

import javax.validation.Valid;
@RestController
@RequestMapping({"/api"})
@CrossOrigin
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    Argon2PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtUserRepository userRepository;

//    @PostMapping("/match")
//    @PreAuthorize("hasRole('admin')")
//    public void login(@Validated @RequestBody SimpleLoginDTO body) {
//
//        if(userService.matcher(body.getEmail(), body.getUserpassword()) == true){
//            throw new ResponseStatusException(HttpStatus.OK,"Password Match!");
//        }else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password Not Match!");
//        }
//
//    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        String roles = user.getRoles();
        String email = user.getEmail();
        System.out.println(roles);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found Email!");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getUserpassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt,roles,email));
    }
}
