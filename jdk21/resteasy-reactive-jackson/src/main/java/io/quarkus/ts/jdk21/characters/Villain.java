package io.quarkus.ts.jdk21.characters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("villain")
public class Villain extends Character {

    private long bounty;

    public Villain() {
        super();
    }

    public Villain(String name, long bounty) {
        super(name);
        this.bounty = bounty;
    }

    public long getBounty() {
        return bounty;
    }

    public void setBounty(long bounty) {
        this.bounty = bounty;
    }
}
