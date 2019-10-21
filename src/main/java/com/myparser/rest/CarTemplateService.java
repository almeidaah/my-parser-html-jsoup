package com.myparser.rest;

import com.myparser.parser.TemplateParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * This class represents the CarTemplate Resource)
 */
@Path("/")
public class CarTemplateService {

    @Context
    private HttpServletRequest httpServletRequest;

    TemplateParser templateParser;

    @GET
    @Path("/{param}")
    public Response parseTemplate( @PathParam("param") String template){

        try {

            URL templateFileURL = getClass().getClassLoader().getResource("templates/" + template);

            if(Objects.isNull(templateFileURL)){ throw new FileNotFoundException(); }

            File templateFile = new File(templateFileURL.getFile());
            Document doc = Jsoup.parse(templateFile, StandardCharsets.UTF_8.name(), "http://example.com");
            templateParser = new TemplateParser(doc);
            templateParser.parse(httpServletRequest);

        }catch(IOException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Template not found").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(templateParser.getTemplateRaw().toString()).build();
        }

        return Response.status(Response.Status.OK).entity(templateParser.getTemplateRaw().toString()).build();
    }

}
