package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.entity.Tennis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TennisRepository extends JpaRepository<Tennis,Long> {
}
