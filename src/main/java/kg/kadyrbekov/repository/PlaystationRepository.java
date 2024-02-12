package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.entity.Playstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaystationRepository extends JpaRepository<Playstation,Long> {
}
