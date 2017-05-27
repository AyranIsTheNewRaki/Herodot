package world.herodot.model;

import javax.persistence.*;

/**
 * Bogazici University - Spring'17
 * Herodot - SWE 574 Project
 * https://github.com/AyranIsTheNewRaki/Herodot
 */

@Entity
@Table(name = "annotation")
public class Annotation {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "choId")
    private Long choId;

    @Column(name = "annoId")
    private String annotationId;

    public Long getChoId() {
        return choId;
    }

    public void setChoId(Long choId) {
        this.choId = choId;
    }

    public String getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
