package fr.xebia.clickcount.web.resource;

import fr.xebia.clickcount.repository.ClickRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


@Path("/")
@Singleton
public class ClickResource {

    @Inject
    private ClickRepository clickRepository;
    @Context
    private ServletContext context;

    @GET
    @Path("click")
    @Produces(MediaType.TEXT_PLAIN)
    public long getCount() {
        return clickRepository.getCount();
    }

    @POST
    @Path("click")
    @Produces(MediaType.TEXT_PLAIN)
    public long incrementCount() {
        return clickRepository.incrementAndGet();
    }

    @GET
    @Path("healthcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public String healthcheck() {
        String result = clickRepository.ping();
        if ("PONG".equals(result)) {
            return "ok";
        }
        return "ko : " + result;
    }

    @GET
    @Path("version")
    @Produces(MediaType.TEXT_PLAIN)
    public String version() {

        try {
            InputStream is = context.getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(is);
            Attributes attributes = manifest.getMainAttributes();
            String version = attributes.getValue("Build-Label");
            return version;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
