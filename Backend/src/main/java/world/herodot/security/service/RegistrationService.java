package world.herodot.security.service;

import world.herodot.model.security.Account;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

public interface RegistrationService {
    Account createAccount(Account account);
}
