package io.quarkus.ts.jdk21.characters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

import static jakarta.persistence.EnumType.STRING;

@Entity
@DiscriminatorValue("hero")
public class Hero extends Character {

    public enum Rank {
        S,
        A,
        B,
        F;
    }

    @Enumerated(STRING)
    private Rank heroRank;

    public Hero() {
        super();
        this.heroRank = Rank.F;
    }

    public Hero(Rank heroRank) {
        super();
        this.heroRank = heroRank;
    }

    public Hero(String name, Rank heroRank) {
        super(name);
        this.heroRank = heroRank;
    }

    public Hero(String name, String quirk, Rank heroRank) {
        super(name, quirk);
        this.heroRank = heroRank;
    }

    public Rank getHeroRank() {
        return heroRank;
    }

    public void setHeroRank(Rank heroRank) {
        this.heroRank = heroRank;
    }
}
