package eu.innowise.moviereviewproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(of = "externalId")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_id", unique = true, nullable = false)
    private Long externalId;

    private String name;

    @Column(name = "en_name")
    private String enName;

    private String profession;

    @Column(name = "photo_url")
    private String photoUrl;
}
