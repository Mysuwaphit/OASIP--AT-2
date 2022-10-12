package sit.int221.projectintegrate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.projectintegrate.Entities.EventcategoryOwner;

import java.util.List;

public interface EventCategoryOwnerRepository extends JpaRepository<EventcategoryOwner, Integer> {

    @Query("select a.eventCategory.id from EventcategoryOwner a where a.user.id = :id ")
    List<Integer>findAllByUserId(@Param("id") int id);
}
