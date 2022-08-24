//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sit.int221.projectintegrate.Services;

import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.listMapper;
import sit.int221.projectintegrate.DTO.SimpleEventDTO;

import sit.int221.projectintegrate.Repository.EventRepository;

@Service
public class EventsService {
    @Autowired
    private EventRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private listMapper listMapper;

    public EventsService() {
    }

    public SimpleEventDTO getSimpleEventById(Integer eventId) {
        Events event = (Events)this.repository.findById(eventId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Event id " + eventId + "Does Not Exist !!!");
        });
        return (SimpleEventDTO)this.modelMapper.map(event, SimpleEventDTO.class);
    }

    public List<SimpleEventDTO> getAllEvent() {
        List<Events> eventList = this.repository.findAll();
        return this.listMapper.mapList(eventList, SimpleEventDTO.class, this.modelMapper);
    }

//    public Events save(SimpleEventDTO newEvent) {
//        Events e = (Events)this.modelMapper.map(newEvent, Events.class);
//        return (Events)this.repository.saveAndFlush(e);
//    }
    public Events addEvent(SimpleEventDTO newEvent){
        LocalDateTime currentDateTime;
        currentDateTime = LocalDateTime.now();
        if (newEvent.getStartTime().isBefore(currentDateTime)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, newEvent.getStartTime() + "is a past!!!");
        }
        Events addEventList = modelMapper.map(newEvent, Events.class);
        List<Events> eventList = repository.findEventByEventCategoryIdEquals(addEventList.getEventCategory().getId());
        check(newEvent.getStartTime(), newEvent.getDuration(),eventList );
        return repository.saveAndFlush(addEventList);
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



