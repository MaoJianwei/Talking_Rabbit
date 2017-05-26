package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mao on 17-5-12.
 */
public class RabbitWebApplication extends ResourceConfig {

    public static final String RESOURCE_PACKAGE = "org.mao.talking.rabbit.restful";


    public RabbitWebApplication() {

        // mark where are the web resources.
        packages(RESOURCE_PACKAGE);

        // register Json Reader and Writer to server core.
        register(JacksonJsonProvider.class);
    }
}
