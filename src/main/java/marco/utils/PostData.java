package marco.utils;

import java.io.IOException;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@RequestScoped
@Path("api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostData {

    // BAW compatible
    @POST
    @Path("jsondata")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJsonData(MyData data) throws IOException {
        if ( data != null ) {
            System.out.println("===>> postJsonData() " + data.toString());
        } else {
            System.out.println("===>> postJsonData() data is null");
        }
        return Response.status(200).build();
    }

    // BAW compatible
    @GET
    @Path("jsondata")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200")
    public MyData getJsonData() throws IOException {
        MyData data = new MyData("name", "address", 1);
        System.out.println("===>> getJsonData(): " + data.toString());
        return data;
    }


    // BAW compatible
    @POST
    @Path("textdata")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postTextData(String data) throws IOException {
        System.out.println("===>> postTextData() [" + data + "]");
        return Response.status(200).build();
    }

}
