package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.entity.Billiards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BilliardsRepository extends JpaRepository<Billiards,Long> {
}
