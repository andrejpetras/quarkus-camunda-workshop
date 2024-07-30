package quarkus.camunda.workshop.workers;

import io.quarkiverse.zeebe.JobWorker;
import io.quarkiverse.zeebe.Variable;
import io.quarkiverse.zeebe.VariablesAsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quarkus.camunda.workshop.data.Parameter;

public class ParameterJobWorker {

    private static final Logger log = LoggerFactory.getLogger(ParameterJobWorker.class);

    @JobWorker(type = "create-parameter")
    public Parameter createParameter(@Variable("inputName") String name, @Variable String inputValue) {
        Parameter parameter = new Parameter();
        parameter.name = name;
        parameter.value = inputValue;
        log.info("Create parameter {}", parameter);
        return parameter;
    }

    @JobWorker(type = "update-description")
    public Parameter createParameter(@VariablesAsType Parameter parameter) {
        parameter.description = parameter.name + "/" + parameter.value;
        log.info("Update description {}", parameter);
        return parameter;
    }

    @JobWorker(type = "println-parameter")
    public void printlnParameter(@VariablesAsType Parameter parameter) {
        log.info("Println parameter {}", parameter);
    }
}
