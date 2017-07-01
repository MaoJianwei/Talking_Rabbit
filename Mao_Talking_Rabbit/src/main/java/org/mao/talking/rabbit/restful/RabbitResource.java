package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;
import org.mao.talking.rabbit.api.MaoRabbitService;
import org.mao.talking.rabbit.api.RabbitMessage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_STANDBY;

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

    private static final String JSON_MSG_USERNAME = "username";
    private static final String JSON_MSG_PASSWORD = "password";


    private static final String RESPONSE_OK = "ok";
    private static final String RESPONSE_ERROR_UNKNOWN = "Internal message transfer unknown error";
    private static final String RESPONSE_ERROR_COLOR = "Color fields error";
    private static final String RESPONSE_ERROR_JSON = "JSON key or value error";
    private static final String RESPONSE_ERROR_AUTH_FAIL = "Username or Password is wrong";


    private static final int RESPONSE_CODE_OK = 0;
    private static final int RESPONSE_CODE_ERROR_UNKNOWN = -1;
    private static final int RESPONSE_CODE_ERROR_COLOR = 1;
    private static final int RESPONSE_CODE_ERROR_JSON = 2;
    private static final int RESPONSE_CODE_ERROR_AUTH = 3;

    private static final String SHA_ALGORITHM = "SHA-256";
    private static final String SHA_PLACEHOLDER = "0";


    private static Queue messageQueue;

    private static MaoRabbitService maoRabbitService;

    private static Set<String> managerAccounts = new HashSet();

    /**
     * Default username & password.
     *
     * Username: Jianwei Mao
     * Password: www.maojianwei.com
     *
     * Default, calculate SHA-256 with UsernamePassword, then match among the managerAccounts.
     */
    static {
        managerAccounts.add("0fe59970365b5e626de5d8d9fbd9f9bb7d0b66462463dc51761466a50f4cbd7d");
    }


    public static void setMessageQueue(Queue queue) {
        messageQueue = queue;
    }

    public static void setRabbitService(MaoRabbitService rabbitService) {
        maoRabbitService = rabbitService;
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

        JsonNode backgroundJson = rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR);
        JsonNode wordJson = rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR);
        JsonNode messageJson = rawMsgColor.get(JSON_MSG_COLOR_MESSAGE);
        if(backgroundJson == null || wordJson == null || messageJson == null) {
            return ok(buildResult(RESPONSE_CODE_ERROR_JSON, RESPONSE_ERROR_JSON));
        }

        RabbitMessage msgColorShow = RabbitMessage.getRabbitMessage(
                backgroundJson.asText(),
                wordJson.asText(),
                messageJson.asText());

        if(msgColorShow != null) {

            return messageQueue.offer(msgColorShow)
                    ? ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK))
                    : ok(buildResult(RESPONSE_CODE_ERROR_UNKNOWN, RESPONSE_ERROR_UNKNOWN));
        } else {

            return ok(buildResult(RESPONSE_CODE_ERROR_JSON, RESPONSE_ERROR_JSON));
        }
    }

    @GET
    @Path("/clear")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    public Response clearEvent() {

        return messageQueue.offer(RabbitMessage.getRabbitMessage(COLOR_STR_STANDBY))
                ? ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK))
                : ok(buildResult(RESPONSE_CODE_ERROR_UNKNOWN, RESPONSE_ERROR_UNKNOWN));
    }

    @POST
    @Path("/shutdown")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    @Consumes(CONTENT_TYPE_JSON) // necessary !!!
    public Response systemShutdownEvent(ObjectNode namePwd) {

        JsonNode usernameJson = namePwd.get(JSON_MSG_USERNAME);
        JsonNode passwordJson = namePwd.get(JSON_MSG_PASSWORD);
        if(usernameJson == null || passwordJson == null) {
            return ok(buildResult(RESPONSE_CODE_ERROR_JSON, RESPONSE_ERROR_JSON));
        }

        if(authenticate(usernameJson.asText(), passwordJson.asText())) {

            maoRabbitService.shutdownGracefully();

            return ok(buildResult(RESPONSE_CODE_OK, RESPONSE_OK));

        } else {

            return ok(buildResult(RESPONSE_CODE_ERROR_AUTH, RESPONSE_ERROR_AUTH_FAIL));
        }
    }

    private boolean authenticate(String user, String pwd) {

        String shaCode = getShaCode(user + pwd);

        for(String c : managerAccounts) {

            if (c.equals(shaCode)) {

                return true;
            }
        }

        return false;
    }

    private String getShaCode(String str) {

        String strResult = null;

        if (str != null && str.length() > 0)
        {
            try
            {
                MessageDigest messageDigest = MessageDigest.getInstance(SHA_ALGORITHM);
                messageDigest.update(str.getBytes());
                byte byteBuffer[] = messageDigest.digest();

                StringBuilder strHexString = new StringBuilder();
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append(SHA_PLACEHOLDER);
                    }
                    strHexString.append(hex);
                }

                strResult = strHexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
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































