package world.herodot.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import world.herodot.model.security.Account;
import world.herodot.model.security.Authority;
import world.herodot.model.security.AuthorityName;
import world.herodot.security.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private UserRepository userRepository;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Account createAccount(Account account) {

        Account isUserExist = userRepository.findByUsername(account.getUsername());
        Account isMailExist = userRepository.findByEmail(account.getEmail());

        if(isUserExist != null){
            throw new RuntimeException(String.format("There is already an account with username '%s'. Please login.", account.getUsername()));
        }
        else if(isMailExist != null){
            throw new RuntimeException(String.format("There is already an account with email '%s'. Please login.", account.getEmail()));
        }
        else {
            Account accountToRegister = new Account();

            accountToRegister.setUsername(account.getUsername());
            accountToRegister.setEmail(account.getEmail());

            if(account.getFirstname() != null){
                accountToRegister.setFirstname(account.getFirstname());
            }
            if(account.getLastname() != null){
                accountToRegister.setLastname(account.getLastname());
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            accountToRegister.setPassword(passwordEncoder.encode(account.getPassword()));

            accountToRegister.setEnabled(true);

            Date in = new Date();
            LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            accountToRegister.setLastPasswordResetDate(date);

            Authority authority = new Authority();
            List<Authority> authorityList = new ArrayList<>();
            List<Account> accountList = new ArrayList<>();
            accountList.add(accountToRegister);

            authority.setName(AuthorityName.ROLE_USER);
            authority.setAccounts(accountList);

            authorityList.add(authority);
            accountToRegister.setAuthorities(authorityList);

            userRepository.save(accountToRegister);

            return accountToRegister;
        }
    }
}
