package sit.int221.projectintegrate.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleEventCategoryDTO;
import sit.int221.projectintegrate.Entities.EventCategory;
import sit.int221.projectintegrate.Repository.EventCategoryRepository;
import sit.int221.projectintegrate.listMapper;

import java.util.List;


@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private listMapper listMapper;

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
