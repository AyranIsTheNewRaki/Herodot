package world.herodot.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import world.herodot.model.security.Account;
import world.herodot.security.service.RegistrationService;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@RestController
public class RegistrationRestController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationRestController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerNewUser(@RequestBody Account account) throws IllegalAccessException {
        Account savedAccount =  registrationService.createAccount(account);
        return ResponseEntity.ok(savedAccount);
    }
}
