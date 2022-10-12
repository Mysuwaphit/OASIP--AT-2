package sit.int221.projectintegrate.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.projectintegrate.Entities.Events;
import sit.int221.projectintegrate.Entities.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Events,Integer> {
     List<Events> findEventByEventCategoryIdEquals(Integer eventCategoryId);
     @Query("select a from Events a where a.bookingEmail = :ownerEmail order by a.startTime DESC")
     List<Events> findAllByOwner(@Param("ownerEmail") String ownerEmail);
     @Query("select a from Events a where a.id in :idCate")
     List<Events> findAllByEventCategory(@Param("idCate") List<Integer> idCate);
     List<Events> findAll();

}
