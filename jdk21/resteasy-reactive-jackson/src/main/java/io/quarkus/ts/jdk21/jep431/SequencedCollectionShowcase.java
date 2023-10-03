package io.quarkus.ts.jdk21.jep431;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;

import java.util.SequencedCollection;

@Entity(name = "jep431")
@NamedQuery(name = "SequencedCollectionShowcase.findAll", query = """
        SELECT c FROM jep431 c ORDER BY c.id
        """, hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class SequencedCollectionShowcase {

    @Id
    @SequenceGenerator(name = "jep431Sequence", sequenceName = "jep431_id_seq", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "jep431Sequence")
    private Integer id;

    @ElementCollection
    private SequencedCollection<String> sequencedCollection;

    public SequencedCollectionShowcase() {
    }

    public SequencedCollectionShowcase(SequencedCollection<String> sequencedCollection) {
        this.sequencedCollection = sequencedCollection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SequencedCollection<String> getSequencedCollection() {
        return sequencedCollection;
    }

    public void setSequencedCollection(SequencedCollection<String> sequencedCollection) {
        this.sequencedCollection = sequencedCollection;
    }
}
