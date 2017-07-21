package com.maojianwei.talking.rabbit.restful;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by mao on 17-5-12.
 */
public class RabbitWebApplication extends ResourceConfig {

    public static final String RESOURCE_PACKAGE = "com.maojianwei.talking.rabbit.restful";


    public RabbitWebApplication() {

        // mark where are the web resources.
        packages(RESOURCE_PACKAGE);

        // register Json Reader and Writer to server core.
        register(JacksonJsonProvider.class);
    }
}
