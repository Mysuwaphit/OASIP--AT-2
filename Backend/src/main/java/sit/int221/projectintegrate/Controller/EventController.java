package sit.int221.projectintegrate.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.projectintegrate.DTO.SimpleEventDTO;
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


@RestController
@RequestMapping({"/api/events"})
@CrossOrigin
public class EventController {
    @Autowired
    private EventsService eventsService;
    @Autowired
    private ModelMapper modelMapper;
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

//    @PostMapping(path = "",consumes = {"multipart/form-data"})
    @PostMapping({""})
    @ResponseStatus(HttpStatus.CREATED)
    public Object create(@Valid HttpServletRequest request, @RequestBody SimpleEventDTO newEvent) {
//    public Object create(@Valid HttpServletRequest request, @RequestParam("event") String event, @RequestParam(name = "file", required = false) MultipartFile file) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
//        SimpleEventDTO newEvent  = objectMapper.readValue(event, SimpleEventDTO.class);

        Events addEventList = modelMapper.map(newEvent, Events.class);
        List<Events> eventList = repository.findEventByEventCategoryIdEquals(addEventList.getEventCategory().getId());
        LocalDateTime time = newEvent.getStartTime();
        String formattedDate = time.format(DateTimeFormatter.ofPattern("dd-MMM-yy-hh-mm"));
        String header = "You have made a new appointment for 1 event.";
        String body = "Your appointment has been registered successfully. \n \n" +
                "Details  \n" + "Name : " + newEvent.getBookingName() + "\n" +"Clinic : " + addEventList.getEventCategory().getEventCategoryName() +
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
//    @PutMapping("/{eventId}")
//    public Object update(@Valid HttpServletRequest request, @RequestParam("event") String event, @RequestParam(name = "file", required = false) MultipartFile file, @PathVariable Integer bookingId) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        SimpleEventUpdateDTO editEvent  = objectMapper.readValue(event, SimpleEventUpdateDTO.class);
//        return eventsService.updateEvent(request, editEvent, file, bookingId);
//    }

}
