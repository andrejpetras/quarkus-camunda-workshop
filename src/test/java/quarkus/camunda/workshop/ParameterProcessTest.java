package quarkus.camunda.workshop;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.ProcessInstanceAssert;
import io.quarkiverse.zeebe.test.InjectZeebeClient;
import io.quarkiverse.zeebe.test.ZeebeTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@DisplayName("Parameter process test")
@QuarkusTestResource(ZeebeTestResource.class)
public class ParameterProcessTest {

    @InjectZeebeClient
    ZeebeClient client;

    private final static String BPM_PROCESS_ID = "parameter-process";

    @Test
    @DisplayName("Description test")
    public void parameterProcessDescriptionTest() {
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
        a.hasVariableWithValue("description", inputName + "/" + inputValue);
    }



}
