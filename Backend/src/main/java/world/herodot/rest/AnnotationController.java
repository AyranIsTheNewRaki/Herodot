package world.herodot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.herodot.model.Annotation;
import world.herodot.service.AnnotationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@CrossOrigin
@RestController
public class AnnotationController {

    private AnnotationService annotationService;

    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
     * Endpoint to fetch all Annotation entities by heritage Id. The service returns
     * the collection of Annotation Ids as JSON.
     *
     * @param choId A Long URL path variable containing the Heritage primary key
     *        identifier.
     * @return A ResponseEntity containing a Collection of Annotation Ids.
     */
    @RequestMapping(value = "/annotation/{choId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAnnotations(@PathVariable("choId") Long choId){
        Collection<Annotation> annotations = annotationService.getAnnotations(choId);
        List<String> annoIds = new ArrayList<>();

        annotations.parallelStream().forEach(annotation -> annoIds.add(annotation.getAnnotationId()));
        
        return new ResponseEntity<>(annoIds, HttpStatus.OK);
    }

    /**
     * Endpoint to create a single Annotation entity.
     *
     * If created successfully, the persisted Annotation is returned as JSON with
     * HTTP status 201.
     *
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param choId The Heritage object id.
     * @param annoId The Annotation object id.
     * @return A ResponseEntity containing a single Annotation object, if created
     *         successfully, and a HTTP status code as described in the method
     *         comment.
     */
    @RequestMapping(value = "/saveannotation/{choId}/{annoId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Annotation> createAnnotation(@PathVariable("choId") Long choId, @PathVariable("annoId") String annoId) {

        Annotation savedAnnotation = annotationService.create(choId, annoId);
        return new ResponseEntity<>(savedAnnotation, HttpStatus.CREATED);
    }
}
