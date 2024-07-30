package quarkus.camunda.workshop.workers;

import io.quarkiverse.zeebe.JobWorker;
import io.quarkiverse.zeebe.VariablesAsType;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quarkus.camunda.workshop.client.MetadataRestClient;
import quarkus.camunda.workshop.data.Parameter;

import jakarta.inject.Inject;
import java.util.Map;

public class ParameterMetadataJobWorker {

    private static final Logger log = LoggerFactory.getLogger(ParameterMetadataJobWorker.class);

    @Inject
    @RestClient
    MetadataRestClient restClient;

    @JobWorker(type = "update-metadata")
    public Parameter updateMetadata(@VariablesAsType Parameter parameter) {
        Map<String, Object> metadata = restClient.getMetadata();
        log.info("Update metadata {}", metadata);
        parameter.metadata = metadata;
        return parameter;
    }

    @JobWorker(type = "update-metadata-reactive")
    public Uni<Parameter> updateParameterReactive(@VariablesAsType Parameter parameter) {
        log.info("Update metadata reactive {}", parameter);
        return restClient.getMetadataReactive().onItem().transform(map -> {
            parameter.metadata = map;
            return parameter;
        });
    }
}
