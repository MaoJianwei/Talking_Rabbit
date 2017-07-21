package com.maojianwei.talking.rabbit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.core.Response;

/**
 * Created by mao on 17-5-12.
 */
public abstract class AbstractWebResource {

    private final ObjectMapper mapper = new ObjectMapper();

    protected ObjectMapper getMapper() {
        return mapper;
    }

    protected ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    protected Response ok(Object obj) {
        return Response.ok(obj).build();
    }
}
