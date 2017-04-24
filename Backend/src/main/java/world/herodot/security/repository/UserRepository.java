package world.herodot.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import world.herodot.model.security.Account;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

public interface UserRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
