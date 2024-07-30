
# quarkus-camunda-workshop

## Start process instances

Open `Zeebe - Dashboard` and create new process instance

1. http://localhost:8080/q/dev-ui/io.quarkiverse.zeebe.quarkus-zeebe/dashboard
2. Open process definition `parameter-process`
3. Open tab `instances` and create new instance.

```shell
{"inputName":"name1","inputValue":"value1"}
``` 

## Start process from console

curl http://localhost:8080/parameter -X POST -H 'Content-Type: application/json' -d '{"inputName":"name1","inputValue":"value1"}'
