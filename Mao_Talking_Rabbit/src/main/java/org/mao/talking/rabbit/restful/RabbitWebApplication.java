package org.mao.talking.rabbit.restful;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mao on 17-5-12.
 */
public class RabbitWebApplication extends Application {

    /**
     * Returns the aggregate set of resources, writers and mappers combined
     * with a default set of such web entities.
     *
     * @return combined set of web entities
     */
    @Override
    public Set<Class<?>> getClasses() {
        return getClasses(RabbitResource.class);
    }

    /**
     * Returns the aggregate set of resources, writers and mappers combined
     * with a default set of such web entities.
     *
     * @param classes set of resources, writers and mappers
     * @return combined set of web entities
     */
    protected Set<Class<?>> getClasses(Class<?>... classes) {

        // Talking Rabbit is a simple application.
        // So maybe we should not introduce too many third-party lib dependencies?

//        ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
//        builder.add(ServiceNotFoundMapper.class,
//                EntityNotFoundMapper.class,
//                NotFoundMapper.class,
//                ServerErrorMapper.class,
//                BadRequestMapper.class,
//                WebApplicationExceptionMapper.class,
//                IllegalArgumentExceptionMapper.class,
//                IllegalStateExceptionMapper.class,
//                JsonBodyWriter.class);


        Set<Class<?>> classSet = new HashSet<Class<?>>();

        // extend here - add requisite writers and mappers to the set

        for (Class c : classes) {
            classSet.add(c);
        }

        return classSet;
    }
}
