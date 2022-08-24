package sit.int221.projectintegrate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleEventDTO;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.Repository.EventRepository;
import sit.int221.projectintegrate.Services.EventsService;
import javax.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping({"/api/events"})
@CrossOrigin
public class EventController {
    @Autowired
    private EventsService eventsService;
    @Autowired
    private EventRepository repository;
    @Autowired
    public EventController(EventRepository repository) {
        this.repository = repository;
    }

//    @GetMapping({""})
//    public List<SimpleEventDTO> getAllEvent() {
//        return this.eventsService.getAllEvent();
//    }

    @GetMapping({""})
    public List<Events> getEventAll() {
        return this.repository.findAll();
    }

    @GetMapping({"/{eventId}"})
    public SimpleEventDTO getEventById(@PathVariable Integer eventId) {
        return eventsService.getSimpleEventById(eventId);
    }

    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public Events create(@Valid @RequestBody SimpleEventDTO newEvent) {
        return this.eventsService.addEvent(newEvent);
    }

    @DeleteMapping({"/{eventId}"})
    public void delete(@PathVariable Integer eventId) {
        this.repository.findById(eventId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, eventId + " does not exist !!!");
        });
        this.repository.deleteById(eventId);
    }

    @PutMapping({"/{eventId}"})
    public Events updateEvent(@RequestBody Events updateEvent, @PathVariable Integer eventId) {
        Events event = (Events)this.repository.findById(eventId).map((o) -> {
            return this.mapEvent(o, updateEvent);
        }).orElseGet(() -> {
            updateEvent.setId(eventId);
            return updateEvent;
        });
        return (Events)this.repository.saveAndFlush(event);
    }

    private Events mapEvent(Events existingEvent, Events updateEvent) {
        existingEvent.setStartTime(updateEvent.getStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
        return existingEvent;
    }
}
