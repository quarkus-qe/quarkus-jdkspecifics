package io.quarkus.ts.http.graphql;

public record Person(String name, Person friend) {

    public Person(String name) {
        this(name, null);
    }

    public Person(String name, Person friend) {
        this.name = name;
        this.friend = friend;
    }
}
