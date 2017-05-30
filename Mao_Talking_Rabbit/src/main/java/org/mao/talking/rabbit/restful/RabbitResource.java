package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {

    @GET
    @Path("/color/{color}")
    @Produces("application/json") // necessary !!!
    public Response testGetEvent(@PathParam("color") String color) {

        if(!checkColorInput(color)){
            return ok(buildResult(1, "ok"));
        }

        color = calculateColor(color);

        // add to task Queue

        return ok(buildResult(200, "ok"));
    }

    private ObjectNode buildResult(int code, String message){ return buildResult(code, message, null); }

    private ObjectNode buildResult(int code, String message, Map<String, String> customFields) {
        ObjectNode data = getMapper().createObjectNode()
                .put("errcode", code)
                .put("errmsg", message);

        customFields.forEach((k, v) -> data.put(k,v));

        return data;
    }

    private String calculateColor(String rawColor) {

    }

    private Boolean checkColorInput(String rawColor) {

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
}
