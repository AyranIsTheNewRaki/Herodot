package world.herodot.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@CrossOrigin
@RestController
public class GreetingRestService {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getHomepage() {
        return "Herodot API - <a href='https://github.com/AyranIsTheNewRaki/Herodot' target='_blank'>https://github.com/AyranIsTheNewRaki/Herodot</a>";
    }
}
