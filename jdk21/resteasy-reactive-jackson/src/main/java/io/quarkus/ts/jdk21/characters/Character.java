package io.quarkus.ts.jdk21.characters;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "characters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "affiliation", discriminatorType = DiscriminatorType.STRING)
@NamedQuery(name = "Character.findAll", query = """
        SELECT c FROM characters c ORDER BY c.id
        """, hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class Character implements Comparable<Character> {

    @Id
    @SequenceGenerator(name = "characterSequence", sequenceName = "character_id_seq", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "characterSequence")
    private Integer id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 40)
    private String quirk;

    public Character() {
    }

    public Character(String name) {
        this.name = name;
    }

    public Character(String name, String quirk) {
        this.name = name;
        this.quirk = quirk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuirk() {
        return quirk;
    }

    public void setQuirk(String quirk) {
        this.quirk = quirk;
    }

    @Override
    public int compareTo(Character character) {
        return id.compareTo(character.id);
    }
}
