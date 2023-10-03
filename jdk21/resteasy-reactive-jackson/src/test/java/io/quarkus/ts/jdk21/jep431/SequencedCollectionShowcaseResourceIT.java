package io.quarkus.ts.jdk21.jep431;

import io.quarkus.test.bootstrap.PostgresqlService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.Container;
import io.quarkus.test.services.QuarkusApplication;
import io.quarkus.ts.jdk21.jep440.RecordShowcase;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;

@QuarkusScenario
public class SequencedCollectionShowcaseResourceIT {
    public static final String stringToTest1 = "String1";
    public static final String stringToTest2 = "String2";
    @Container(image = "${postgresql.latest.image}", port = 5432, expectedLog = "listening on IPv4 address")
    static PostgresqlService postgres = new PostgresqlService();

    @QuarkusApplication
    static final RestService app = new RestService()
            .withProperty("quarkus.datasource.username", postgres.getUser())
            .withProperty("quarkus.datasource.password", postgres.getPassword())
            .withProperty("quarkus.datasource.jdbc.url", postgres::getJdbcUrl)
            .withProperty("quarkus.datasource.reactive.url", postgres::getReactiveUrl);

    @BeforeEach
    public void prepare() {
        given()
                .body(stringToTest1)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep431/first")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("Added first"));

        given()
                .body(stringToTest2)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep431/first")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("Added first"));
    }

    @Test
    public void testAddToCollection() {
        String stringToTest3 = "String3";
        String stringToTest4 = "String4";

        given()
                .body(stringToTest3)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep431/last")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("Added last"));

        given()
                .body(stringToTest4)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep431/first")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("Added first"));

        given()
                .when()
                .get("jep431")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(stringToTest4, stringToTest2, stringToTest1, stringToTest3));
    }

    @Test
    public void testReversedCollection() {
        given()
                .when()
                .get("jep431")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(stringToTest1, stringToTest2));

        given()
                .when()
                .get("jep431/reverse")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Reversed"));

        given()
                .when()
                .get("jep431")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(stringToTest2, stringToTest1));
    }
}
