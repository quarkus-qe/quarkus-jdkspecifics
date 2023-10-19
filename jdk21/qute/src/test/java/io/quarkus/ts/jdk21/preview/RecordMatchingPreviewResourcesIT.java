package io.quarkus.ts.jdk21.preview;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.QuarkusApplication;
import io.quarkus.ts.jdk21.preview.records.RecordPreviewShowcase;
import io.quarkus.ts.jdk21.preview.records.UnnamedRecordQute;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;

@QuarkusScenario
public class RecordMatchingPreviewResourcesIT {
    private static final String STRING_TO_TEST = "test String";
    private static final int INTEGER_TO_TEST = 39;
    @QuarkusApplication
    static final RestService app = new RestService();

    @Test
    public void testRecordWithTwoStringsAndNullValues() {
        given()
                .body(JsonObject.mapFrom(new RecordPreviewShowcase(STRING_TO_TEST, null, null, STRING_TO_TEST)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder("first and last attribute are strings",
                        STR."Value 1 \{STRING_TO_TEST}" , STR."value 4 \{STRING_TO_TEST}"));
    }

    @Test
    public void testRecordWithTwoStringsAndIntegerValues() {
        given()
                .body(JsonObject.mapFrom(new RecordPreviewShowcase(STRING_TO_TEST, INTEGER_TO_TEST, INTEGER_TO_TEST, STRING_TO_TEST)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder("first and last attribute are strings",
                        STR."Value 1 \{STRING_TO_TEST}" , STR."value 4 \{STRING_TO_TEST}"));
    }

    @Test
    public void testRecordWithStringIntegerAndNullValues() {
        given()
                .body(JsonObject.mapFrom(new RecordPreviewShowcase(null, STRING_TO_TEST, null, INTEGER_TO_TEST)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder("second attribute is strings and last attribute is integer",
                        STR."Value 2 \{STRING_TO_TEST}" , STR."value 4 \{INTEGER_TO_TEST}"));
    }

    @Test
    public void testRecordWithTwoStringsAndIntegerValuesNegative() {
        given()
                .body(JsonObject
                        .mapFrom(new RecordPreviewShowcase(INTEGER_TO_TEST, STRING_TO_TEST, INTEGER_TO_TEST, STRING_TO_TEST))
                        .encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Unsupported type of record input"));
    }

    @Test
    public void testRecordWithTwoStringsAndNullValuesNegative() {
        given()
                .body(JsonObject.mapFrom(new RecordPreviewShowcase(null, STRING_TO_TEST, null, STRING_TO_TEST)).encode())
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Unsupported type of record input"));
    }

    @Test
    public void testQuteRecordWithTwoStrings() {
        given()
                .body(STR."{\"value1\":\"\{STRING_TO_TEST}\",\"value2\":\"\{STRING_TO_TEST}\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/qute")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(STR."<p>First value is \{STRING_TO_TEST} and it's String.</p>",
                        STR."<p>Second value is \{STRING_TO_TEST} and it's String</p>"));
    }

    @Test
    public void testQuteRecordWithStringAndInteger() {
        given()
                .body(STR."{\"value1\":\"\{STRING_TO_TEST}\",\"value2\":\{INTEGER_TO_TEST}}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/qute")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(STR."<p>First value is \{STRING_TO_TEST} and it's String.</p>",
                        STR."<p>Second value is \{INTEGER_TO_TEST} and it's Integer</p>"));
    }

    @Test
    public void testQuteRecordWithIntegerAndString() {
        given()
                .body(STR."{\"value1\":\{INTEGER_TO_TEST},\"value2\":\{STRING_TO_TEST}}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/qute")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testQuteRecordWithStringAndNull() {
        given()
                .body(STR."{\"value1\":\"\{STRING_TO_TEST}\",\"value2\":\{null}}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("preview/unnamed/qute")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
