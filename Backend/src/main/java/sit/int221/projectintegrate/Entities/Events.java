package sit.int221.projectintegrate.Entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId", nullable = false)
    private Integer id;

    @Column(name = "bookingName", nullable = false, length = 100)
    private String bookingName;

    @Column(name = "bookingEmail", nullable = false, length = 200)
    private String bookingEmail;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "eventNotes", length = 500)
    private String eventNotes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "eventCategoryId")
    private EventCategory eventCategory;

}