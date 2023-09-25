package io.quarkus.ts.jdk21.jep440;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("jep440")
@ApplicationScoped
public class Jep440Resources {

    /**
     * As JEP 440 introduce records patterns it can make possible to check if record contains specific data types.
     * This can be used for example if record is used as abstract data carrier. This is shown in this method as it only
     * accept the record object containing String and Integer or String and String.
     * <p>
     * For example, we can expect the that client sent request with 2 parameter but client send request with only one parameter
     * so record is created as <Object, null>. Before JEP 440 there was need to manually check if second attribute was null,
     * now we can easily check if the request contains types which are expected.
     *
     * @param recordShowcase Data of posts request
     * @return Response containing string with details of record attributes or unsupported string mention
     */
    @POST
    public Response jep440(RecordShowcase recordShowcase) {
        switch (recordShowcase) {
            case RecordShowcase(String string, Integer integer) -> {
                return Response.ok("This show case record containing string " + string + " and integer " + integer).status(200)
                        .build();
            }
            case RecordShowcase(String string1, String string2) -> {
                return Response.ok("This show case record containing string " + string1 + " and string " + string2).status(200)
                        .build();
            }
            default -> {
                return Response.ok("Unsupported type of record input").status(200).build();
            }
        }
    }
}
