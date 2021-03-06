package io.quarkus.ts.http.graphql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@GraphQLApi
public class PersonsEndpoint {
    private final List<Person> philosophers = new ArrayList<>();

    public PersonsEndpoint() {

        Person aristotle = new Person("Aristotle", new Person("Plato"));
        Person plato = new Person(aristotle.friend().name(), aristotle);

        philosophers.addAll(Arrays.asList(plato, aristotle));
    }

    @Query("philosophers")
    @Description("Get a couple of Greek philosophers")
    public List<Person> getPhilosophers() {
        return philosophers;
    }

    @Query("friend")
    public Person getPhilosopher(@Name("name") String name) {
        for (Person philosopher : philosophers) {
            if (philosopher.name().equals(name)) {
                return philosopher.friend();
            }
        }
        throw new NoSuchElementException(name);
    }

    @Query("friend_r")
    public Uni<Person> getPhilosopherReactively(@Name("name") String name) {
        return Multi.createFrom().iterable(philosophers)
                .filter(person -> person.name().equals(name))
                .map(Person::friend)
                .toUni();
    }

    @Mutation("create")
    public Person createPhilosopher(@Name("name") String name) {
        Person philosopher = new Person(name);
        philosophers.add(philosopher);
        return philosopher;
    }
}
