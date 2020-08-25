package com.sqli.iscm.c19track;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.ContainerResponseFilter;

@EnableAsync
@SpringBootApplication
@ApplicationPath("/c19")
public class C19TrackingApp extends ResourceConfig {

    public C19TrackingApp() {
        packages(C19TrackingApp.class.getPackage().getName());

        register(jacksonJaxbJsonProvider());
        register((ContainerResponseFilter) (requestContext, cres) -> {
            cres.getHeaders().add("Access-Control-Allow-Origin", "*");
            cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
            cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            cres.getHeaders().add("Access-Control-Max-Age", "1209600");
        });
    }

    private JacksonJaxbJsonProvider jacksonJaxbJsonProvider() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        return provider;
    }

    public static void main(String[] args) {
        SpringApplication.run(C19TrackingApp.class, args);
    }

    @Bean
    public TaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(4);
        pool.setThreadGroupName("asyncExecutor");
        return pool;
    }
}
