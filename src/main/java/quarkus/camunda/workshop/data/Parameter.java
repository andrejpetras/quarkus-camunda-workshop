package quarkus.camunda.workshop.data;

import java.util.Map;

public class Parameter {

    public String name;

    public String value;

    public String description;

    public Map<String, Object> metadata;

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}