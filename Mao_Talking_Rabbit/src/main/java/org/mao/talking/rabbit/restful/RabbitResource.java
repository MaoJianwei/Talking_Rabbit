package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {

    @GET
    @Path("/success")
    public Response successEvent() {

        // add to task Queue

        ObjectNode data = getMapper().createObjectNode()
                .put("success", 1080);

        return ok(data);
    }
}
