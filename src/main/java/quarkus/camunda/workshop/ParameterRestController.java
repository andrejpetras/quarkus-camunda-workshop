package quarkus.camunda.workshop;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/parameter")
public class ParameterRestController {

    @Inject
    ZeebeClient zeebeClient;

    @POST
    public CompletionStage<ProcessInstanceEvent> createProcessInstace(CreateProcessInstanceRequest request) {
        return zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("parameter-process")
                .latestVersion()
                .variables(request)
                .send()
                .thenApply(processInstanceEvent -> processInstanceEvent);
    }

    @POST
    @Path("block")
    public Response createProcessInstanceBlock(CreateProcessInstanceRequest request) {
        ProcessInstanceEvent event = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("parameter-process")
                .latestVersion()
                .variables(request)
                .send()
                .join();
        return Response.ok(event).build();
    }

    public record CreateProcessInstanceRequest(String inputName, String inputValue) {};
}
