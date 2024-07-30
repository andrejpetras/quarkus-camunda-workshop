package quarkus.camunda.workshop.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.Map;

@Path("/metadata")
@RegisterRestClient(configKey = "metadata")
public interface MetadataRestClient {

    @GET
    Map<String, Object> getMetadata();

    @GET
    Uni<Map<String, Object>> getMetadataReactive();
}