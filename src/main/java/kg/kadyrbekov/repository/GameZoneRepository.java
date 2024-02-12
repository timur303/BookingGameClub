package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.entity.GameZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameZoneRepository extends JpaRepository<GameZone,Long> {
}
