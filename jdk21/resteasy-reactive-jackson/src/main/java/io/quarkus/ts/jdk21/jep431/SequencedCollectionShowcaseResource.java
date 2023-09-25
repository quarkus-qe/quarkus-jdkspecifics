package io.quarkus.ts.jdk21.jep431;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("jep431")
public class SequencedCollectionShowcaseResource {
    @Inject
    EntityManager entityManager;

    @GET
    public List<SequencedCollectionShowcaseResource> get() {
        return entityManager
                .createNamedQuery("SequencedCollectionShowcase.findAll", SequencedCollectionShowcaseResource.class)
                .getResultList();
    }

    @GET
    @Transactional
    @Path("reverse")
    public Response getReversed() {
        SequencedCollectionShowcase entity = entityManager.find(SequencedCollectionShowcase.class, 1);
        entity.setSequencedCollection(entity.getSequencedCollection().reversed());
        entityManager.persist(entity);
        return Response.ok("Reversed").status(200).build();
    }

    @POST
    @Transactional
    @Path("first")
    public Response addToStartOfCollection(String string) {
        SequencedCollectionShowcase entity = entityManager.find(SequencedCollectionShowcase.class, 1);
        entity.getSequencedCollection().addFirst(string);
        entityManager.persist(entity);
        return Response.ok("Added first").status(201).build();
    }

    @POST
    @Transactional
    @Path("last")
    public Response addToEndOfCollection(String string) {
        SequencedCollectionShowcase entity = entityManager.find(SequencedCollectionShowcase.class, 1);
        entity.getSequencedCollection().addLast(string);
        entityManager.persist(entity);
        return Response.ok("Added last").status(201).build();
    }
}
