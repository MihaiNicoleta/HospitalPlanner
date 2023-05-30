package proiect.demo.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;
import proiect.demo.Domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);

    boolean existsByEmail(String email);

}