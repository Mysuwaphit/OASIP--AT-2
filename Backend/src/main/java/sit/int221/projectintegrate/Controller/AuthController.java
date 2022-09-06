//package sit.int221.projectintegrate.Controller;
//
//
//import de.mkammerer.argon2.Argon2;
//import de.mkammerer.argon2.Argon2Factory;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import sit.int221.projectintegrate.DTO.*;
//import sit.int221.projectintegrate.Entities.User;
//import sit.int221.projectintegrate.Repository.JwtUserRepository;
//import sit.int221.projectintegrate.Services.CustomUserDetailsService;
//import sit.int221.projectintegrate.Util.JwtUtil;
//
//import javax.validation.Valid;
//
//@RestController
//public class AuthController {
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    Argon2PasswordEncoder passwordEncoder;
//
//    @Autowired
//    CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    JwtUserRepository userRepository;
//
////    @GetMapping("/checkUser")
////    public String checkUser(){
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String currentPrincipalName = authentication.getName();
////        return  currentPrincipalName;
////    }
////
////    @PostMapping("/authenticate")
////    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
////        Authentication authenticate = authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getUserpassword())
////        );
////
////        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
////
////        final String jwt = jwtUtil.generateToken(userDetails);
////
////        return ResponseEntity.ok(new AuthenticationResponse(jwt));
////
////    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        User user = userRepository.findByEmail(loginRequest.getEmail());
//        if(user == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found Email!");
//        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getUserpassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
//
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
//
//
//        if(userRepository.findUsersByEmail(signUpRequest.getEmail()) != null) {
//            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
//                    HttpStatus.BAD_REQUEST);
//        }
//        User addUserList = modelMapper.map(signUpRequest, User.class);
//        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id,12,24);
//        String hash = argon2.hash(2,15*1024,1,addUserList.getUserpassword().toCharArray());
//        // Creating user's account
//        User jwtUser = new User();
//        jwtUser.setUsername(signUpRequest.getUsername());
//        jwtUser.setEmail(signUpRequest.getEmail());
//        jwtUser.setUserpassword(hash);
////        jwtUser.setUserpassword(passwordEncoder.encode(signUpRequest.getUserpassword()));
//        jwtUser.setRoles(signUpRequest.getRoles());
//        userRepository.save(jwtUser);
//        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
//    }
//}
