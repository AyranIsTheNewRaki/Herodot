package world.herodot.service;

import world.herodot.model.Annotation;

import java.util.Collection;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

public interface AnnotationService {
    Annotation create(Long choId, String annoId);
    Collection<Annotation> getAnnotations(Long choId);
}
