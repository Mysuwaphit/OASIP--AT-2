package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.projectintegrate.Entities.EventCategory;
import sit.int221.projectintegrate.Entities.Events;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory,Integer> {

}
