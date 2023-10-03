package io.quarkus.ts.jdk21.characters;

import io.quarkus.test.bootstrap.PostgresqlService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.Container;
import io.quarkus.test.services.QuarkusApplication;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;

@QuarkusScenario
public class CharacterResourceIT {
    @Container(image = "${postgresql.latest.image}", port = 5432, expectedLog = "listening on IPv4 address")
    static PostgresqlService postgres = new PostgresqlService();

    @QuarkusApplication
    static final RestService app = new RestService()
            .withProperty("quarkus.datasource.username", postgres.getUser())
            .withProperty("quarkus.datasource.password", postgres.getPassword())
            .withProperty("quarkus.datasource.jdbc.url", postgres::getJdbcUrl)
            .withProperty("quarkus.datasource.reactive.url", postgres::getReactiveUrl);

    @Test
    public void testAllCharacters() {
        given()
                .when()
                .get("characters")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder("\"id\":1", "\"id\":2", "\"id\":3", "\"id\":4"));
    }

    @Test
    public void testCharacterNotFound() {
        given()
                .when()
                .get("characters/3939")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(emptyString());
    }

    @Test
    public void testCharacterIsHero() {
        given()
                .when()
                .get("characters/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("is hero with rank"));
    }

    @Test
    public void testCharacterIsVillain() {
        given()
                .when()
                .get("characters/3")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("is villain with bounty"));
    }

    @Test
    public void testRankDescription() {
        given()
                .when()
                .get("characters/rank/F")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("rank is"));
    }

    @Test
    public void testRankDescriptionNotFound() {
        given()
                .when()
                .get("characters/rank/Q")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(emptyString());
    }

    @Test
    public void testAddHeroAndCheckIfExist() {
        Hero addedHero = given()
                .body(JsonObject.mapFrom(new Hero("Hero", Hero.Rank.F)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("characters/hero")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().body().as(Hero.class);

        given()
                .when()
                .get("characters/" + addedHero.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("is hero with rank"));
    }

    @Test
    public void testAddVillainAndCheckIfExist() {
        Villain addedVillain = given()
                .body(JsonObject.mapFrom(new Villain("Villain", 300000)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("characters/villain")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().body().as(Villain.class);

        given()
                .when()
                .get("characters/" + addedVillain.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("is villain with bounty"));
    }
}
