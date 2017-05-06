package world.herodot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.herodot.model.Heritage;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {
}
