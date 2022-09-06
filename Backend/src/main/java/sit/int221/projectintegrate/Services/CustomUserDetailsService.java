package sit.int221.projectintegrate.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.projectintegrate.DTO.SimpleLoginDTO;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Repository.JwtUserRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    JwtUserRepository jwtUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User jwtUser = jwtUserRepository.findUsersByEmail(email);
        if (jwtUser == null) {
            throw new UsernameNotFoundException("email Not found" + email);
        }
        return new org.springframework.security.core.userdetails.User(jwtUser.getEmail(), jwtUser.getUserpassword(), new ArrayList<>());
    }
}