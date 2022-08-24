package sit.int221.projectintegrate.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.projectintegrate.Entities.Events;

import java.util.List;

public interface EventRepository extends JpaRepository<Events,Integer> {
     List<Events> findEventByEventCategoryIdEquals(Integer eventCategoryId);

}
