package world.herodot.service;

import world.herodot.model.Heritage;

import java.util.Collection;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

public interface HeritageService {
    Collection<Heritage> findAll();
    Heritage findOne(Long id);
    Heritage create(String username, Heritage heritage);
    Heritage update(Heritage heritage);
    void delete(Long id);
}
