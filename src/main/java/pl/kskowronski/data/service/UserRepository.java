package pl.kskowronski.data.service;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}