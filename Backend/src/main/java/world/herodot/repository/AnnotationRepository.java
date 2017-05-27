package world.herodot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import world.herodot.model.Annotation;

import java.util.List;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long>{

    @Query("SELECT a FROM Annotation a where a.choId = :choId")
    List<Annotation> getAnnotationsByChoId(@Param("choId") Long choId);
}
