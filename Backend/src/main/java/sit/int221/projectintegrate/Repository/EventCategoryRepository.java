package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.projectintegrate.Entities.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory,Integer> {

}
