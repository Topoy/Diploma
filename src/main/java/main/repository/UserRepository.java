package main.repository;

import main.model.Post;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    @Query(value = "SELECT email FROM users", nativeQuery = true)
    List<String> getAllEmails();

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT name FROM users", nativeQuery = true)
    List<User> getAllNames();
}
