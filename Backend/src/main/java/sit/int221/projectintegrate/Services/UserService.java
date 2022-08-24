package sit.int221.projectintegrate.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleUserDTO;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private sit.int221.projectintegrate.listMapper listMapper;

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
        return repository.saveAndFlush(addUserList);
    }


}