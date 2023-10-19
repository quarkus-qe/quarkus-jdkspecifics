package io.quarkus.ts.jdk21.preview;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.QuarkusApplication;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.quarkus.ts.jdk21.preview.PreviewResources.CUSTOM_TEXT;
import static io.quarkus.ts.jdk21.preview.PreviewResources.HEADER;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;

@QuarkusScenario
public class SimplePreviewResourcesIT {

    @QuarkusApplication
    static final RestService app = new RestService();

    @Test
    public void testStringTemplateWithName() {
        String name = "Quarkus";
        app.given()
                .get(STR."/preview/\{name}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is(STR."Hello \{name}"));
    }

    @Test
    public void testStringWebpageTemplate() {
        app.given()
                .get("/preview/template")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(STR."<h1>\{HEADER}</h1>"), containsString(STR."<p>\{CUSTOM_TEXT}</p>"));
    }

    @Test
    public void testStringWebpageTemplateQute() {
        app.given()
                .get("/preview/templateQute")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(stringContainsInOrder(STR."<h1>\{HEADER}</h1>"), containsString(STR."<p>\{CUSTOM_TEXT}</p>"));
    }

}
