//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sit.int221.projectintegrate.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Exception.ValidationHandler;
import sit.int221.projectintegrate.Repository.EventCategoryOwnerRepository;
import sit.int221.projectintegrate.Repository.UserRepository;
import sit.int221.projectintegrate.Services.Storage.StorageService;
import sit.int221.projectintegrate.Util.JwtUtil;
import sit.int221.projectintegrate.listMapper;

import sit.int221.projectintegrate.DTO.SimpleEventDTO;

import sit.int221.projectintegrate.Repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Service
public class EventsService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private listMapper listMapper;
    @Autowired
    private StorageService storageService;

    @Autowired
    private Validator validator;
    private final JwtUtil jwtTokenUtill;
    private final CustomUserDetailsService jwtUserDetailsService;

    public EventsService(JwtUtil jwtTokenUtill, CustomUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtill = jwtTokenUtill;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    public Object getSimpleEventById(HttpServletRequest request,Integer eventId) {
        Optional<User> userOwner = getUserFromRequest(request);
        Events event = (Events)this.eventRepository.findById(eventId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Event id " + eventId + "Does Not Exist !!!");
        });
        if (userOwner.get().getRoles().equals("student")) {
            if (!userOwner.get().getEmail().equals(event.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You are not allowed this event!");
            }

        }

        return modelMapper.map(event, SimpleEventDTO.class);
    }
    public Optional<User> getUserFromRequest(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            String userEmail = jwtTokenUtill.extractUsername(token);
            return  usersRepository.findByEmail(userEmail);
        }
        return null;
    }

//    public List<SimpleEventDTO> getAllEvent() {
//        List<Events> eventList = this.eventRepository.findAll();
//        return this.listMapper.mapList(eventList, SimpleEventDTO.class, this.modelMapper);
//    }

    public List<SimpleEventDTO> getAllEvent(HttpServletRequest request){
        Optional<User> userOwner = getUserFromRequest(request);
//        System.out.println(userOwner.get().getEmail());
        List<Events> eventList = new ArrayList<>();
            if (userOwner.get().getRoles().equals("admin")) {
                System.out.println("Signin admin");
                eventList = eventRepository.findAll();
            } else if (userOwner.get().getRoles().equals("student")) {
                System.out.println("Signin student");
                eventList = eventRepository.findAllByOwner(userOwner.get().getEmail());
            } else if (userOwner.get().getRoles().equals("lecturer")) {
                System.out.println("Signin lecturer");
                List<Integer> categoriesId = eventCategoryOwnerRepository.findAllByUserId(userOwner.get().getId());
                eventList = eventRepository.findAllByEventCategory(categoriesId);
            }

        return listMapper.mapList(eventList, SimpleEventDTO.class, this.modelMapper);
    }

//    public Events save(SimpleEventDTO newEvent) {
//        Events e = (Events)this.modelMapper.map(newEvent, Events.class);
//        return (Events)this.repository.saveAndFlush(e);
//    }
public Object addEvent(HttpServletRequest request,SimpleEventDTO newEvent){
    Optional<User> userOwner = getUserFromRequest(request);
    LocalDateTime currentDateTime;
    currentDateTime = LocalDateTime.now();
    if(userOwner != null) {
        if (userOwner.get().getRoles().equals("student")) {
            if (!userOwner.get().getEmail().equals(newEvent.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.BAD_REQUEST, "The booking email must be the same as student's email!!");
            }
        }
    }

    if (newEvent.getStartTime().isBefore(currentDateTime)){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, newEvent.getStartTime() + "Is a past!!!");
    }
    Events addEventList = modelMapper.map(newEvent, Events.class);
    List<Events> eventList = eventRepository.findEventByEventCategoryIdEquals(addEventList.getEventCategory().getId());
    check(newEvent.getStartTime(), newEvent.getDuration(),eventList );
    return eventRepository.saveAndFlush(addEventList);
}
//    public Object addEvent(HttpServletRequest request, SimpleEventDTO newEvent, MultipartFile file){
//        Set<ConstraintViolation<SimpleEventDTO>> violations = validator.validate(newEvent);
//        if (!violations.isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            for (ConstraintViolation<SimpleEventDTO> constraintViolation : violations) {
//                sb.append(constraintViolation.getMessage());
//            }
//            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
//        }
//
//        Optional<User> userOwner = getUserFromRequest(request);
//        LocalDateTime currentDateTime;
//        currentDateTime = LocalDateTime.now();
//
//
//        if(userOwner != null) {
//            if (userOwner.get().getRoles().equals("student")) {
//                if (!userOwner.get().getEmail().equals(newEvent.getBookingEmail())) {
//                    return ValidationHandler.showError(HttpStatus.BAD_REQUEST, "The booking email must be the same as student's email!!");
//                }
//            }
//        }
//
//        Events addEventList = modelMapper.map(newEvent, Events.class);
//        List<Events> eventList = eventRepository.findEventByEventCategoryIdEquals(addEventList.getEventCategory().getId());
//        check(newEvent.getStartTime(), newEvent.getDuration(),eventList );
//        Events addedEvent = eventRepository.saveAndFlush(addEventList);
//        if(file != null) {
//            if (!file.isEmpty()) {
//                storageService.store(file, addedEvent.getId());
//                return file.getOriginalFilename();
//            }
//        }
//        return addedEvent;
//    }

    public Object deleteEvent(HttpServletRequest request, Integer eventId){
        Optional<User> userOwner = getUserFromRequest(request);
        Events event = eventRepository.findById(eventId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        eventId + " Does Not Exist !!!"));
        if (userOwner.get().getRoles().equals("student")) {
            if (!userOwner.get().getEmail().equals(event.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You are not allowed to delete this event , Please only delete your events.");
            }
        }
        eventRepository.deleteById(eventId);
        storageService.deleteFileById(eventId);
        return event;
    }
//    public Object updateEvent(HttpServletRequest request,@RequestBody Events updateEvent, @PathVariable Integer eventId) {
//        Optional<User> userOwner = getUserFromRequest(request);
//        Events event = eventRepository.findById(eventId).map((o) -> {
//            return this.mapEvent(o, updateEvent);
//        }).orElseGet(() -> {
//            updateEvent.setId(eventId);
//            return updateEvent;
//        });
//        if (userOwner.get().getRoles().equals("student")) {
//            if (!userOwner.get().getEmail().equals(event.getBookingEmail())) {
//                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You not have permission this event");
//            }
//        }
//        eventRepository.saveAndFlush(event);
//        return updateEvent;
//    }
//
//    private Events mapEvent(Events existingEvent, Events updateEvent) {
//        existingEvent.setStartTime(updateEvent.getStartTime());
//        existingEvent.setEventNotes(updateEvent.getEventNotes());
//        return existingEvent;
//    }
    public Object updateEvent(HttpServletRequest request, SimpleEventDTO updateEvent, Integer bookingId){
        Optional<User> userOwner = getUserFromRequest(request);
        Events event = eventRepository.findById(bookingId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        bookingId + " Does Not Exist !!!"));

        if (userOwner.get().getRoles().equals("student")) {
            if (!userOwner.get().getEmail().equals(event.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You are not allowed to edit this event , Please only edit your events.");
            }

        }

        Events updateEventList = modelMapper.map(updateEvent, Events.class);
        List<Events> eventList = eventRepository.findEventByEventCategoryIdEquals(updateEventList.getEventCategory().getId());
        Events update = eventRepository.findById(bookingId).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND, " Event id "+ updateEventList.getId()+
                "Does Not Exist !!!"
        ));
        eventList.remove(update);

        event.setStartTime(updateEvent.getStartTime());
        event.setEventNotes(updateEvent.getEventNotes());
        event.setEventCategory(updateEvent.getEventCategory());
        eventRepository.saveAndFlush(event);
        return updateEvent;
    }

    public void check(LocalDateTime updateDateTime, Integer newDuration, List<Events> eventList) {
        LocalDateTime newStartTime = updateDateTime;
        LocalDateTime newEndTime = findEndDate(newStartTime, newDuration);
        for (Events event : eventList) {
            LocalDateTime startTime = event.getStartTime();
            LocalDateTime endTime = findEndDate(event.getStartTime(), event.getDuration());
            System.out.println("Input");
            if (newStartTime.isEqual(startTime) ||
                    newStartTime.isBefore(startTime) && newEndTime.isAfter(startTime) ||
                    newStartTime.isBefore(endTime) && newEndTime.isAfter(endTime) ||
                    newStartTime.isBefore(startTime) && newEndTime.isAfter(endTime) ||
                    newStartTime.isAfter(startTime) && newEndTime.isBefore(endTime)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Overlapping time! Please enter your time again.");
            }
        }
    }

    private LocalDateTime findEndDate(LocalDateTime eventStartTime, Integer duration) {
        LocalDateTime getEventEndTime = eventStartTime.plusMinutes(duration);
        return getEventEndTime;
    }
}



