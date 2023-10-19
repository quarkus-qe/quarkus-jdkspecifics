package io.quarkus.ts.jdk21.preview;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.ts.jdk21.preview.records.RecordPreviewShowcase;
import io.quarkus.ts.jdk21.preview.records.UnnamedRecordQute;
import io.quarkus.ts.jdk21.preview.records.WebTemplatePreview;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("preview")
@ApplicationScoped
public class PreviewResources {

    public static final String HEADER = "Header name";
    public static final String CUSTOM_TEXT = "This is a paragraph text. which will be under the header";

    @GET
    @Path("{name}")
    public String getName(String name) {
        return STR."Hello \{name}";
    }

    /**
     * Showcase the templates use case in text block. This can be useful when working in texts using some structure
     *
     * @return Build response as html with status 200
     */
    @GET
    @Path("template")
    @Produces(MediaType.TEXT_HTML)
    public Response getWebTemplate() {
        return Response.ok(STR."""
            <!DOCTYPE html>
            <html>
                <body>
                    <h1>\{HEADER}</h1>
                    <p>\{CUSTOM_TEXT}</p>
                </body>
            </html>""").status(200).build();
    }

    /**
     * Same situation as {@link #getWebTemplate()} but with usage Qute
     *
     * @return The html template build by Qute
     */
    @GET
    @Path("templateQute")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getWebTemplateQute() {
        return new WebTemplatePreview(HEADER, CUSTOM_TEXT);
    }

    /**
     * Showcasing the multiple unnamed patterns (keyword _), String templates and enhanced switch with records.
     * @param recordPreviewShowcase POST request containing values
     * @return Response containing string with details of record attributes or unsupported string mention
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("unnamed/variables")
    public Response unnamedPatterns(RecordPreviewShowcase recordPreviewShowcase) {
        switch (recordPreviewShowcase) {
            case RecordPreviewShowcase(String string1, _, _, String string4) -> {
                return Response.ok(STR."""
                This case matching only if the first and last attribute are strings.
                At the same time we care only about these two values and it's possible to print them like:
                Value 1 \{string1} and value 4 \{string4}""").status(200).build();
            }
            case RecordPreviewShowcase(_, String string, _, Integer integer) -> {
                return Response.ok(STR."""
                This case matching only if the second attribute is strings and last attribute is integer.
                At the same time we care only about these two values and it's possible to print them like:
                Value 2 \{string} and value 4 \{integer}""").status(200).build();
            }
            default -> {
                return Response.ok("Unsupported type of record input").status(200).build();
            }
        }
    }

    /**
     * Showcasing the multiple unnamed patterns (keyword _), String templates and enhanced switch with records.
     * In this use case we don't care about data of record but just data types.
     *
     * @param unnamedRecordQute POST request containing values
     * @return Returning Qute template of record with added data type of second attribute
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("unnamed/qute")
    public TemplateInstance unnamedPatternsAndQute(UnnamedRecordQute unnamedRecordQute) {
        switch (unnamedRecordQute) {
            case UnnamedRecordQute(String _, String _) -> {
                return unnamedRecordQute.data("dataType", "String");
            }
            case UnnamedRecordQute(String _, Integer _) -> {
                return unnamedRecordQute.data("dataType", "Integer");
            }
            default -> {
                throw new BadRequestException();
            }
        }
    }
}
