package io.quarkus.ts.jdk21.jep440;

import io.quarkus.test.bootstrap.PostgresqlService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.Container;
import io.quarkus.test.services.QuarkusApplication;
import io.quarkus.ts.jdk21.characters.Hero;
import io.quarkus.ts.jdk21.characters.Villain;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;

@QuarkusScenario
public class Jep440ResourceIT {
    @Container(image = "${postgresql.latest.image}", port = 5432, expectedLog = "listening on IPv4 address")
    static PostgresqlService postgres = new PostgresqlService();

    @QuarkusApplication
    static final RestService app = new RestService()
            .withProperty("quarkus.datasource.username", postgres.getUser())
            .withProperty("quarkus.datasource.password", postgres.getPassword())
            .withProperty("quarkus.datasource.jdbc.url", postgres::getJdbcUrl)
            .withProperty("quarkus.datasource.reactive.url", postgres::getReactiveUrl);

    @Test
    public void testJep440Success1() {
        String stringToTest = "String";
        int intToTest = 39;
        given()
                .body(JsonObject.mapFrom(new RecordShowcase(stringToTest, intToTest)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep440")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("This show case record containing string "
                        + stringToTest + " and integer " + intToTest));
    }

    @Test
    public void testJep440Success2() {
        String stringToTest = "String";
        given()
                .body(JsonObject.mapFrom(new RecordShowcase(stringToTest, stringToTest)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep440")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("This show case record containing string "
                        + stringToTest + " and string " + stringToTest));
    }

    @Test
    public void testJep440Negative() {
        int intToTest = 39;
        given()
                .body(JsonObject.mapFrom(new RecordShowcase(intToTest, intToTest))
                        .encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep440")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Unsupported type of record input"));
    }

    @Test
    public void testJep440NegativeWithNull() {
        int intToTest = 39;
        given()
                .body(JsonObject.mapFrom(new RecordShowcase(intToTest, null))
                        .encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("jep440")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Unsupported type of record input"));
    }
}
