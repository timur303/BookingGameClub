package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String number);
    Optional<User> findByClubId(Long clubId);



//
//    @Query("SELECT u FROM User u WHERE u.firstName = :username")
//    public User getUserByUsername(@Param("username") String username);

}
