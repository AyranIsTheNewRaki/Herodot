package world.herodot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import world.herodot.model.Heritage;
import world.herodot.model.security.Account;
import world.herodot.repository.HeritageRepository;
import world.herodot.security.repository.UserRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Service
public class HeritageServiceImpl implements HeritageService{

    private HeritageRepository heritageRepository;
    private UserRepository userRepository;

    @Autowired
    public HeritageServiceImpl(HeritageRepository heritageRepository, UserRepository userRepository) {
        this.heritageRepository = heritageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Heritage> findAll() {
        return heritageRepository.findAll();
    }

    @Override
    public Heritage findOne(Long id) {
        Heritage heritage = heritageRepository.findOne(id);

        if(heritage == null){
            throw new NoResultException("Requested entity not found.");
        }

        return heritage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Heritage create(String username, Heritage heritage) {

        if (heritage.getId() != null) {
            throw new EntityExistsException("The id attribute must be null to persist a new entity.");
        }

        Account user = userRepository.findByUsername(username);

        heritage.setUserId(user.getId());
        heritage.setUsername(username);
        return heritageRepository.save(heritage);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Heritage update(Heritage heritage) {
        Heritage heritageToUpdate = findOne(heritage.getId());

        if (heritageToUpdate == null){
            throw new NoResultException("Requested entity not found.");
        }

        heritageToUpdate.setCategory(heritage.getCategory());
        heritageToUpdate.setDescription(heritage.getDescription());
        heritageToUpdate.setTitle(heritage.getTitle());
        heritageToUpdate.setTimeLocations(heritage.getTimeLocations());

        return heritageRepository.save(heritageToUpdate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void delete(Long id) {
        heritageRepository.delete(id);
    }
}
