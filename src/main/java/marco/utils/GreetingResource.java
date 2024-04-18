package marco.utils;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/fakeserver/ping")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        System.out.println("===> PING");
        return "fakeserver";
    }
}
