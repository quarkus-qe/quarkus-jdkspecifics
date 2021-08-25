package io.quarkus.ts.jdk17.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.NotFoundException;

import io.quarkus.ts.jdk17.storage.FruitEntity;
import io.vertx.core.json.JsonObject;

public record Fruit(
        Long id,
        @NotBlank @Size(max = 20) String name,
        @NotBlank @Size(max = 20) String description) {

    private final static Long DEFAULT_ID = -1L;

    public Fruit(String name, String description) {
        this(DEFAULT_ID, name, description);
    }

    public Fruit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static List<Fruit> getAll() {
        return FruitEntity.<FruitEntity> find("#Fruits.findAllCustom")
                .stream()
                .map(Fruit::fromEntity)
                .collect(Collectors.toList());
    }

    public static Fruit getLongestDescription() {
        return fromEntity(FruitEntity.getLongestDescription());
    }

    public Fruit save() {
        var fruitEntity = toEntity(this);
        FruitEntity.persist(fruitEntity);
        return fromEntity(fruitEntity);
    }

    public Fruit update(Long id) {
        FruitEntity fruit = FruitEntity.findById(id);
        if (fruit == null) {
            throw new NotFoundException();
        }

        fruit.name = this.name;
        fruit.description = this.description;
        FruitEntity.findById(id, LockModeType.PESSIMISTIC_WRITE).persist();

        return fromEntity(fruit);
    }

    public static boolean deleteById(Long id) {
        return FruitEntity.deleteById(id);
    }

    private static Fruit fromEntity(FruitEntity entity) {
        return new Fruit(entity.id, entity.name, entity.description);
    }

    private static FruitEntity toEntity(Fruit fruit) {
        var entity = new FruitEntity();
        entity.name = fruit.name();
        entity.description = fruit.description();
        return entity;
    }

    public String toJsonEncoded() {
        return toJson().encode();
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
