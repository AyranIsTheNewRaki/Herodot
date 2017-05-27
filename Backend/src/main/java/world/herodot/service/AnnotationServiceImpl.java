package world.herodot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import world.herodot.model.Annotation;
import world.herodot.repository.AnnotationRepository;

import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Service
public class AnnotationServiceImpl implements AnnotationService {

    private AnnotationRepository annotationRepository;

    @Autowired
    public AnnotationServiceImpl(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Annotation create(Long choId, String annoId) {

        Annotation annotation = new Annotation();
        annotation.setChoId(choId);
        annotation.setAnnotationId(annoId);

        return annotationRepository.save(annotation);
    }

    @Override
    public Collection<Annotation> getAnnotations(Long choId) {
        Collection<Annotation> annotations = annotationRepository.getAnnotationsByChoId(choId);

        if(annotations == null){
            throw new NoResultException("Requested entity not found.");
        }

        return annotations;
    }
}
