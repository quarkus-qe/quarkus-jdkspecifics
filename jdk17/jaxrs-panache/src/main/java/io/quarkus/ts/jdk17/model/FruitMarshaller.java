package io.quarkus.ts.jdk17.model;

import io.quarkus.ts.jdk17.storage.FruitEntity;

public sealed interface FruitMarshaller extends Marshaller permits Fruit {

    static Fruit fromEntity(FruitEntity entity) {
        return new Fruit(entity.id, entity.name, entity.description);
    }

    static FruitEntity toEntity(Fruit fruit) {
        var entity = new FruitEntity();
        entity.name = fruit.name();
        entity.description = fruit.description();
        return entity;
    }
}
