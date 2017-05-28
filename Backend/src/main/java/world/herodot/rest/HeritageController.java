package world.herodot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import world.herodot.model.Heritage;
import world.herodot.security.JwtTokenUtil;
import world.herodot.service.HeritageService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@CrossOrigin
@RestController
public class HeritageController {
    private HeritageService heritageService;
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    public HeritageController(HeritageService heritageService, JwtTokenUtil jwtTokenUtil) {
        this.heritageService = heritageService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Endpoint to fetch all Heritage entities. The service returns
     * the collection of Heritage entities as JSON.
     *
     * @return A ResponseEntity containing a Collection of Heritage objects.
     */
    @RequestMapping(value = "/heritage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Heritage>> getHeritages(){
        Collection<Heritage> heritages = heritageService.findAll();
        return new ResponseEntity<>(heritages, HttpStatus.OK);
    }

    /**
     * Endpoint to fetch a single Heritage entity by primary key
     * identifier.
     *
     * If found, the Heritage is returned as JSON with HTTP status 200.
     *
     * If not found, the service returns an empty response body with HTTP status
     * 404.
     *
     * @param id A Long URL path variable containing the Heritage primary key
     *        identifier.
     * @return A ResponseEntity containing a single Heritage object, if found,
     *         and a HTTP status code as described in the method comment.
     */
    @RequestMapping(value = "/heritage/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Heritage> getHeritage(@PathVariable("id") Long id){
        Heritage heritage = heritageService.findOne(id);
        return new ResponseEntity<>(heritage, HttpStatus.OK);
    }

    /**
     * Endpoint to create a single Heritage entity. The HTTP request
     * body is expected to contain a Heritage object in JSON format. The
     * Heritage is persisted in the data repository.
     *
     * If created successfully, the persisted Heritage is returned as JSON with
     * HTTP status 201.
     *
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param heritage The Heritage object to be created.
     * @return A ResponseEntity containing a single Heritage object, if created
     *         successfully, and a HTTP status code as described in the method
     *         comment.
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/heritage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Heritage> createHeritage(HttpServletRequest request, @RequestBody Heritage heritage) {

        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        Heritage savedHeritage = heritageService.create(username, heritage);
        return new ResponseEntity<>(savedHeritage, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update a single Heritage entity. The HTTP request
     * body is expected to contain a Heritage object in JSON format. The
     * Heritage is updated in the data repository.
     *
     * If updated successfully, the persisted Heritage is returned as JSON with
     * HTTP status 200.
     *
     * If not found, the service returns an empty response body and HTTP status
     * 404.
     *
     * If not updated successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param heritage The Heritage object to be updated.
     * @return A ResponseEntity containing a single Heritage object, if updated
     *         successfully, and a HTTP status code as described in the method
     *         comment.
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/heritage", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Heritage> updateHeritage(@RequestBody Heritage heritage) {
        Heritage updatedHeritage = heritageService.update(heritage);

        if (updatedHeritage == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedHeritage, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a single Heritage entity. The HTTP request
     * body is empty. The primary key identifier of the Heritage to be deleted
     * is supplied in the URL as a path variable.
     *
     * If deleted successfully, the service returns an empty response body with
     * HTTP status 204.
     *
     * If not deleted successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param id A Long URL path variable containing the Heritage primary key
     *        identifier.
     * @return A ResponseEntity with an empty response body and a HTTP status
     *         code as described in the method comment.
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/heritage/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Heritage> deleteHeritage(@PathVariable("id") Long id) {
        heritageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
