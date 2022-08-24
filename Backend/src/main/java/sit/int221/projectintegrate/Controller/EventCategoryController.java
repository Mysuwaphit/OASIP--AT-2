package sit.int221.projectintegrate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleEventCategoryDTO;
import sit.int221.projectintegrate.Entities.EventCategory;
import sit.int221.projectintegrate.Repository.EventCategoryRepository;
import sit.int221.projectintegrate.Services.EventCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/api/eventCategories"})
@CrossOrigin
public class EventCategoryController {
    @Autowired
    private EventCategoryService eventCategoryService;
    @Autowired
    private EventCategoryRepository repository;

    public EventCategoryController() {
    }
    @GetMapping({""})
    public List<SimpleEventCategoryDTO> getEventCategoryAll() {
        return this.eventCategoryService.getAllEventCategory();
    }

//    @GetMapping({""})
//    public List<EventCategory> getEventCategoryAll() {
//        return this.repository.findAll();
//    }

    @GetMapping("/{eventCategoryId}")
    public SimpleEventCategoryDTO getEventCategoryById(@PathVariable Integer eventCategoryId) {
        return eventCategoryService.getEventById(eventCategoryId);
    }

    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public EventCategory create(@Valid @RequestBody EventCategory newEvent) {
        return (EventCategory)this.repository.saveAndFlush(newEvent);
    }

    @DeleteMapping({"/{eventCategoryId}"})
    public void delete(@PathVariable Integer eventCategoryId) {
        this.repository.findById(eventCategoryId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, eventCategoryId + " does not exist !!!");
        });
        this.repository.deleteById(eventCategoryId);
    }
    @PutMapping("/{eventCategoryId}")
    public EventCategory updateEventCategory(@Valid @RequestBody EventCategory updateEventCategories, @PathVariable Integer eventCategoryId) {
        EventCategory eventCategories = repository.findById(eventCategoryId).map(o -> mapEvent(o, updateEventCategories)).orElseGet(() -> {
            updateEventCategories.setId(eventCategoryId);
            return updateEventCategories;
        });
        return repository.saveAndFlush(eventCategories);
    }

    private EventCategory mapEvent(EventCategory existingEventCategories, EventCategory updateEventCategories){
        existingEventCategories.setDuration(updateEventCategories.getDuration());
        existingEventCategories.setEventCategoryDescription(updateEventCategories.getEventCategoryDescription());
        return existingEventCategories;
    }

}

