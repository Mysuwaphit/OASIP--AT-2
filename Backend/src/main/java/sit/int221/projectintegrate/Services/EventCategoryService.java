package sit.int221.projectintegrate.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleEventCategoryDTO;
import sit.int221.projectintegrate.DTO.SimpleEventDTO;
import sit.int221.projectintegrate.Entities.EventCategory;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Exception.ValidationHandler;
import sit.int221.projectintegrate.Repository.EventCategoryOwnerRepository;
import sit.int221.projectintegrate.Repository.EventCategoryRepository;
import sit.int221.projectintegrate.Repository.UserRepository;
import sit.int221.projectintegrate.Util.JwtUtil;
import sit.int221.projectintegrate.listMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository repository;
    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private listMapper listMapper;
    @Autowired
    private UserRepository usersRepository;


    private final JwtUtil jwtTokenUtill;
    private final CustomUserDetailsService jwtUserDetailsService;

    public EventCategoryService(JwtUtil jwtTokenUtill, CustomUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtill = jwtTokenUtill;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public SimpleEventCategoryDTO getEventById(Integer id) {
        EventCategory event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(event, SimpleEventCategoryDTO.class);
    }

    public List<SimpleEventCategoryDTO> getAllEventCategory() {
        List<EventCategory> eventList = this.repository.findAll();
        return this.listMapper.mapList(eventList, SimpleEventCategoryDTO.class, this.modelMapper);
    }


}
