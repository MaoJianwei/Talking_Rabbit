package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;
import org.mao.talking.rabbit.api.RabbitMessage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Queue;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {


    private static final String CONTENT_TYPE_JSON = "application/json";



    private static Queue messageQueue;

    public static void setMessageQueue(Queue queue) {
        messageQueue = queue;
    }


    @GET
    @Path("/color/{color}")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    public Response colorEvent(@PathParam("color") String color) {

        if(!checkColorInput(color)){
            return ok(buildResult(1, "ok"));
        }

        color = calculateColor(color);

        // TODO - add to task Queue

        return ok(buildResult(0, "ok"));
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


    private static final String JSON_MSG_COLOR_BACKGROUND_COLOR = "backgroundColor";
    private static final String JSON_MSG_COLOR_WORD_COLOR = "wordColor";
    private static final String JSON_MSG_COLOR_MESSAGE = "message";


    @POST
    @Path("/message")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    @Consumes(CONTENT_TYPE_JSON) // necessary !!!
    public Response messageColorEvent(ObjectNode rawMsgColor) {

        if(!checkMessageColorInput(rawMsgColor)) {
            return ok(buildResult(2, "json key or value error"));
        }

        String backColor = calculateColor(rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR).asText());
        String wordColor = calculateColor(rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR).asText());
        String message = rawMsgColor.get(JSON_MSG_COLOR_MESSAGE).asText();

        // TODO - add to task Queue

        return ok(buildResult(0, "ok"));
    }

    private boolean checkMessageColorInput(ObjectNode rawMsgColor) {

        if(rawMsgColor.size() != 3 ||
                rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR) == null ||
                rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR) == null ||
                rawMsgColor.get(JSON_MSG_COLOR_MESSAGE) == null) {

            return false;
        }

        if(!checkColorInput(rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR).asText()) ||
                !checkColorInput(rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR).asText())) {
            return false;
        }

        return true;
    }

    @GET
    @Path("clear")
    @Consumes(CONTENT_TYPE_JSON)
    public Response clearEvent() {

        // TODO - add to task Queue

        return ok(buildResult(0, "ok"));
    }
}































