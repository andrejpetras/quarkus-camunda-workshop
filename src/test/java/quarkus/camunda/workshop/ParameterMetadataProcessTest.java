package quarkus.camunda.workshop;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.ProcessInstanceAssert;
import io.quarkiverse.mockserver.test.InjectMockServerClient;
import io.quarkiverse.mockserver.test.MockServerTestResource;
import io.quarkiverse.zeebe.test.InjectZeebeClient;
import io.quarkiverse.zeebe.test.ZeebeTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.model.JsonBody;

import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@DisplayName("Parameter metadata process test")
@QuarkusTestResource(ZeebeTestResource.class)
@QuarkusTestResource(MockServerTestResource.class)
public class ParameterMetadataProcessTest {

    @InjectZeebeClient
    ZeebeClient client;

    @InjectMockServerClient
    MockServerClient mockServerClient;

    private final static String BPM_PROCESS_ID = "parameter-process";

    @Test
    @DisplayName("Metadata test")
    public void parameterProcessDescriptionTest() {
        Map<String, Object> data = new HashMap<>(Map.of(
                "key-A", "value-A",
                "key-B", 1));

        mockServerClient.when(request().withPath("/metadata").withMethod("GET")
                        , Times.unlimited()
                        , TimeToLive.unlimited()
                        , 100)
                .respond(
                        httpRequest -> response().withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(JsonBody.json(data))
                );
        String inputName = "testName";
        String inputValue = "testValue";

        ProcessInstanceEvent event = client
                .newCreateInstanceCommand()
                .bpmnProcessId(BPM_PROCESS_ID)
                .latestVersion()
                .variables(Map.of("inputName", inputName, "inputValue", inputValue))
                .send().join();

        Assertions.assertEquals(BPM_PROCESS_ID, event.getBpmnProcessId());

        ProcessInstanceAssert a = BpmnAssert.assertThat(event);
        await().atMost(7, SECONDS).untilAsserted(a::isCompleted);
        a.hasVariableWithValue("name", inputName);
        a.hasVariableWithValue("value", inputValue);

        a.hasVariableWithValue("metadata", data);
    }
}
