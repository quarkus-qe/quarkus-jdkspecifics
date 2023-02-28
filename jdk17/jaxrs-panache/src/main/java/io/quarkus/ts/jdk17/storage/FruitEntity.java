package io.quarkus.ts.jdk17.storage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "fruit")
@NamedQuery(name = "Fruits.findAllCustom", query = """
        SELECT f FROM fruit f
        ORDER BY f.name DESC
        """, hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class FruitEntity extends PanacheEntity {
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String description;

    public static FruitEntity getLongestDescription() {
        return FruitEntity.getEntityManager().createQuery("""
                      SELECT f
                      FROM fruit f
                      ORDER BY char_length(f.description) DESC
                """, FruitEntity.class)
                .setMaxResults(1)
                .getSingleResult();
    }
}
