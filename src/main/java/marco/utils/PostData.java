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

    @POST
    @Path("jsondata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces()
    @APIResponse(
        responseCode = "200",
        description = "MyData Created",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema=@Schema(type = SchemaType.OBJECT, implementation = MyData.class))
        )
    public Response postJsonData(MyData data) throws IOException {

        System.out.println("===>> postJsonData() " + data.toString());

        return Response.accepted().entity(data).build();
    }

    @GET
    @Path("jsondata")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200")
    public MyData getJsonData() throws IOException {
        MyData data = new MyData("name", "address", 1);
        System.out.println("===>> getJsonData(): " + data.toString());

        return data;
    }


    @POST
    @Path("textdata")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @APIResponse(
        responseCode = "200",
        description = "Text Created",
        content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema=@Schema(type = SchemaType.OBJECT, implementation = String.class))
        )
    public Response postTextData(String data) throws IOException {

        System.out.println("===>> postTextData() " + data);

        return Response.accepted().entity("RECEIVED: "+data).build();
    }

}
