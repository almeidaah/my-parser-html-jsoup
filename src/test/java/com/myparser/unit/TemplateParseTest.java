package com.myparser.unit;

import com.myparser.rest.CarTemplateService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class TemplateParseTest extends JerseyTest {

    private static File templateFile;

    @BeforeClass
    public static void setup(){
        Path filePath = Paths.get("src", "test", "resources", "index.tpl");
        if(filePath.toFile().exists()){
            templateFile = filePath.toFile();
        }
    }

    @Override
    public Application configure() {
        return new ResourceConfig(CarTemplateService.class);
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {

        return new TestContainerFactory() {
            @Override
            public TestContainer create(final URI baseUri, final DeploymentContext application) throws IllegalArgumentException {
                return new TestContainer() {
                    private HttpServer server;

                    @Override
                    public ClientConfig getClientConfig() {
                        return null;
                    }

                    @Override
                    public URI getBaseUri() {
                        return baseUri;
                    }

                    @Override
                    public void start() {
                        try {
                            this.server = GrizzlyWebContainerFactory.create(
                                    baseUri, Collections.singletonMap("jersey.config.server.provider.packages", "com.myparser.rest")
                            );
                        } catch (ProcessingException e) {
                            throw new TestContainerException(e);
                        } catch (IOException e) {
                            throw new TestContainerException(e);
                        }
                    }

                    @Override
                    public void stop() {
                        this.server.stop();
                    }
                };

            }
        };
    }

    @Test
    public void testLocalTemplateFileExists(){
        Assert.assertNotNull(templateFile);
    }

    @Test
    public void testRestTemplateNotFound(){
        Response response = target("/index99.tpl").request().get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testTemplateParseSucessfully(){
        Response response = target("/index.tpl").queryParam("id", "2").request().get();
        Assert.assertNotNull(response.readEntity(String.class));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testEmptyBrandCar(){
        Response response = target("/index.tpl").queryParam("id", "999").request().get();
        String result = response.readEntity(String.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("Empty Brand"));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testTemplateReturnSmart(){
        Response response = target("/index.tpl").queryParam("id", "1").request().get();
        String result = response.readEntity(String.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("Smart"));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testTemplateCarReturnThreeModels(){
        Response response = target("/index.tpl").queryParam("id", "2").request().get();
        String result = response.readEntity(String.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains(" <div>\n" +
                "    Model : Model 0\n" +
                "   </div>\n" +
                "   <div>\n" +
                "    Model : Model 1\n" +
                "   </div>\n" +
                "   <div>\n" +
                "    Model : Model 2\n" +
                "   </div>"));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }





}
