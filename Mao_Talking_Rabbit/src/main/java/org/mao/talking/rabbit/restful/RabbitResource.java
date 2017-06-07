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

import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_GREEN;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_RED;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_STANDBY;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_YELLOW;
import static org.mao.talking.rabbit.api.RabbitMessage.CUSTOM_COLOR_STR_REGEX;
import static org.mao.talking.rabbit.api.RabbitMessage.checkColor;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {


    private static final String CONTENT_TYPE_JSON = "application/json";


    private static final String JSON_MSG_COLOR_RESP_CODE = "errcode";
    private static final String JSON_MSG_COLOR_RESP_MSG = "errmsg";

    private static final String JSON_MSG_COLOR_BACKGROUND_COLOR = "backgroundColor";
    private static final String JSON_MSG_COLOR_WORD_COLOR = "wordColor";
    private static final String JSON_MSG_COLOR_MESSAGE = "message";

    private static final String RESPONSE_OK = "ok";
    private static final String RESPONSE_ERROR_UNKNOWN = "Internal message transfer unknown error";
    private static final String RESPONSE_ERROR_COLOR = "Color fields error";
    private static final String RESPONSE_ERROR_JSON = "JSON key or value error";

    private static final int RESPONSE_CODE_OK = 0;
    private static final int RESPONSE_CODE_ERROR_UNKNOWN = -1;
    private static final int RESPONSE_CODE_ERROR_COLOR = 1;
    private static final int RESPONSE_CODE_ERROR_JSON = 2;



    private static Queue messageQueue;

    public static void setMessageQueue(Queue queue) {
        messageQueue = queue;
    }

    @GET
    @Path("/color/{color}")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    public Response colorEvent(@PathParam("color") String color) {

        RabbitMessage pureColorShow = RabbitMessage.getRabbitMessage(color);

        if(pureColorShow != null) {

            return messageQueue.offer(pureColorShow)
                    ? ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK))
                    : ok(buildResult(RESPONSE_CODE_ERROR_UNKNOWN, RESPONSE_ERROR_UNKNOWN));

        } else {

            return ok(buildResult(RESPONSE_CODE_ERROR_COLOR, RESPONSE_ERROR_COLOR));
        }

    }

    @POST
    @Path("/message")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    @Consumes(CONTENT_TYPE_JSON) // necessary !!!
    public Response messageColorEvent(ObjectNode rawMsgColor) {

        RabbitMessage msgColorShow = RabbitMessage.getRabbitMessage(
                rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR).asText(),
                rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR).asText(),
                rawMsgColor.get(JSON_MSG_COLOR_MESSAGE).asText());

        if(msgColorShow != null) {

            return messageQueue.offer(msgColorShow)
                    ? ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK))
                    : ok(buildResult(RESPONSE_CODE_ERROR_UNKNOWN, RESPONSE_ERROR_UNKNOWN));
        } else {

            return ok(buildResult(RESPONSE_CODE_ERROR_JSON, RESPONSE_ERROR_JSON));
        }
    }

    @GET
    @Path("clear")
    @Produces(CONTENT_TYPE_JSON)
    public Response clearEvent() {

        return messageQueue.offer(RabbitMessage.getRabbitMessage(COLOR_STR_STANDBY))
                ? ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK))
                : ok(buildResult(RESPONSE_CODE_ERROR_UNKNOWN, RESPONSE_ERROR_UNKNOWN));
    }


    private ObjectNode buildResult(int code, String message) {
        return buildResult(code, message, null);
    }

    private ObjectNode buildResult(int code, String message, Map<String, String> customFields) {
        ObjectNode data = getMapper().createObjectNode()
                .put(JSON_MSG_COLOR_RESP_CODE, code)
                .put(JSON_MSG_COLOR_RESP_MSG, message);

        if(customFields != null) {
            customFields.forEach((k, v) -> data.put(k, v));
        }

        return data;
    }
}































