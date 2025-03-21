package io.quarkus.ts.jdk21.characters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("characters")
@ApplicationScoped
public class CharacterResource {
    @Inject
    EntityManager entityManager;

    @GET
    public List<Character> get() {
        return entityManager.createNamedQuery("Character.findAll", Character.class).getResultList();
    }

    /**
     * Showcasing the enhancement switch as it can detect null, so no if statement before the switch statement.
     * Also this showcase that now switch statement can select by class/data type.
     * <p>
     * Prior to JDK 21 it would be
     * {@snippet :
     * Character entity = entityManager.find(Character.class, id);
     * if (entity == null) {
     *     // do something
     * }
     * switch (entity) {
     *     // switch cases
     * }
     * }
     *
     * @param id Id of hero
     * @return Error or type of character
     */
    @GET
    @Path("{id}")
    public Response whoIsThey(Integer id) {
        Character entity = entityManager.find(Character.class, id);
        switch (entity) {
            case null -> throw new WebApplicationException("Character with id of " + id + " is unknown.", 404);
            case Hero h -> {
                return Response.ok(h.getName() + " is hero with rank " + h.getHeroRank()).status(200).build();
            }
            case Villain v -> {
                return Response.ok(v.getName() + " is villain with bounty " + v.getBounty()).status(200).build();
            }
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        }
    }

    /**
     * This method showcase the usage os when statement in switch.
     * When is replacement for if in switch statement. The if can be still used but inside the case scope.
     * This show two different work logic using switch with enum. Both have same result but when using when the code readability
     * with this simple example is worse.
     *
     * @param rankString Which rank can be described
     * @return Description of the rank if rank exist otherwise return error string
     */
    @GET
    @Path("rank/{rankValue}")
    public Response heroRankExplanation(@PathParam("rankValue") String rankString) {
        Hero.Rank rank;
        try {
            rank = Hero.Rank.valueOf(rankString);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException("Can't get any information about this rank.", 404);
        }
        switch (rank) {
            case Hero.Rank r when r == Hero.Rank.S -> {
                return Response.ok("S rank is highest rank which can hero achieve. Only few have achieve this.").status(200)
                        .build();
            }
            case Hero.Rank r when r == Hero.Rank.A -> {
                return Response.ok("A rank is for experience heroes which had done good work.").status(200).build();
            }
            case Hero.Rank.B -> {
                return Response.ok("B rank is for new heroes which have done few jobs.").status(200).build();
            }
            case Hero.Rank.F -> {
                return Response.ok("F rank is inexperience heroes which have started.").status(200).build();
            }
            default -> throw new IllegalStateException("This should never happen.");
        }
    }

    @POST
    @Transactional
    @Path("hero")
    public Response createHero(Hero hero) {
        entityManager.persist(hero);
        return Response.ok(hero).status(201).build();
    }

    @POST
    @Transactional
    @Path("villain")
    public Response createVillain(Villain villain) {
        entityManager.persist(villain);
        return Response.ok(villain).status(201).build();
    }
}
