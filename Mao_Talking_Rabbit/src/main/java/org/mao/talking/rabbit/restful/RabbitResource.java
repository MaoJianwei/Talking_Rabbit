package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {

    @GET
    @Path("/testGet")
    @Produces("application/json") // necessary !!!
    public Response testGetEvent() {

        // add to task Queue

        ObjectNode data = getMapper().createObjectNode()
                .put("success", 1080);

        return ok(data);
    }

    @PUT
    @Path("/testPut")
    @Produces("application/json") // necessary !!!
    @Consumes("application/json") // necessary !!!
    public Response testPutEvent(ObjectNode body) {

        // add to task Queue

        ObjectNode data = getMapper().createObjectNode()
                .put("success", 1080);

        if(body != null) {
            data.put("body", body.toString());
        }

        return ok(data);
    }

    @POST
    @Path("/testPost")
    @Produces("application/json") // necessary !!!
    @Consumes("application/json") // necessary !!!
    public Response testPostEvent(ObjectNode body) {

        // add to task Queue

        ObjectNode data = getMapper().createObjectNode()
                .put("success", 1080);

        if(body != null) {
            data.put("body", body.toString());
        }

        return ok(data);
    }

    @DELETE
    @Path("/testDelete")
    @Produces("application/json") // necessary !!!
    //@Consumes("application/json") // DELETE can not consume data !!!
    public Response testDeleteEvent() {

        // add to task Queue

        ObjectNode data = getMapper().createObjectNode()
                .put("success", 1080);

        return ok(data);
    }
}
