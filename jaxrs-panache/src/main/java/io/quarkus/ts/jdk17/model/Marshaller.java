package io.quarkus.ts.jdk17.model;

import io.vertx.core.json.JsonObject;

public sealed interface Marshaller permits FruitMarshaller {

    default String toJsonEncoded() {
        return toJson().encode();
    }

    default JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

}
