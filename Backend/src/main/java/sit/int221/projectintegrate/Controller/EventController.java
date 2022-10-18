package sit.int221.projectintegrate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.DTO.SimpleEventDTO;
import sit.int221.projectintegrate.Entities.EventCategory;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.Repository.EventCategoryRepository;
import sit.int221.projectintegrate.Repository.EventRepository;
import sit.int221.projectintegrate.Services.EmailSenderService;
import sit.int221.projectintegrate.Services.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private EventCategoryRepository eventCategoryRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    public EventController(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.emailSenderService = emailSenderService;
    }

//    @GetMapping("")
//    public List<SimpleEventDTO> getAllEvent(@Valid HttpServletRequest request) {
//        return eventsService.getAllEvent(request);
//    }

    @GetMapping("")
    public List<SimpleEventDTO> getAllEvent(@Valid HttpServletRequest request) {
        return eventsService.getAllEvent(request);
    }

    @GetMapping({"/{eventId}"})
    public Object getById(@Valid HttpServletRequest request,@PathVariable Integer eventId) {
        return eventsService.getSimpleEventById(request,eventId);
    }

    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public Object create(@Valid HttpServletRequest request,@Valid @RequestBody SimpleEventDTO newEvent) {
        int categoryId = Integer.parseInt(newEvent.getCategory());
        EventCategory eventCategory = eventCategoryRepository.findById(categoryId).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer id "+ categoryId+
                "Does Not Exist !!!"
        ));
        LocalDateTime time = newEvent.getStartTime();
        String formattedDate = time.format(DateTimeFormatter.ofPattern("dd-MMM-yy-hh-mm"));
        String header = "You have made a new appointment for 1 event.";
        String body = "Your appointment has been registered successfully. \n \n" +
                "Details  \n" + "Name : " + newEvent.getBookingName() + "\n" +"Clinic : " + eventCategory.getEventCategoryName() +
                "\n" + "Date : " + formattedDate + "\n" + "Note : " + newEvent.getEventNotes();
        emailSenderService.sendEmail(newEvent.getBookingEmail() , header , body);
        return this.eventsService.addEvent(request,newEvent);
    }

    @DeleteMapping({"/{eventId}"})
    public Object delete(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        return eventsService.deleteEvent(request, eventId);
    }

    @PutMapping({"/{eventId}"})
    public Object update(@Valid HttpServletRequest request, @Valid @RequestBody SimpleEventDTO updateEvent, @PathVariable Integer eventId) {
        return eventsService.updateEvent(request, updateEvent, eventId);
    }


}
