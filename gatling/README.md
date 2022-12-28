# Gatling tests

This directory contains project with Gatling tests that should be executed against test application.

## How to run given scenario?

1. `SingleGreeting`

```shell
./gradlew gatlingRun-pl.latusikl.greetings.SingleGreetingSimulation -DbaseUrl=http://<IP_ADDRES>:8082 -DsgUsr=<NUMBER_OF_USERS> -DsgTime=<TEST_DURATION_MIN>
```

2. `GreetingSse`

```shell
./gradlew gatlingRun-pl.latusikl.greetings.GreetingSseSimulation -DbaseUrl=http://<IP_ADDRES>:8082 -DsseUsr=<NUMBER_OF_USERS> -DsseTime=<NUMBER_OF_USERS>
```

3. `CreateFetchDelete`
```shell
gradle gatlingRun-pl.latusikl.database.CreateFetchDeleteSimulation -DbaseUrl=http://<IP_ADDRES>:8082 -DdbcrdUsr=<NUMBER_OF_USERS> -DdbcrdTime=<NUMBER_OF_USERS>
```

4. `MediumNumberSet`
```shell
gradle gatlingRun-pl.latusikl.sorting.MediumNumberSetSimulation -DbaseUrl=http://<IP_ADDRES>:8082 -DsortmUsr=<NUMBER_OF_USERS> -DsortmTime=<NUMBER_OF_USERS>
```

5. `SmallNumberSet`
```shell
gradle gatlingRun-pl.latusikl.sorting.SmallNumberSetSimulation -DbaseUrl=http://<IP_ADDRES>:8082
```

6. `Notes`
```shell
gradle gatlingRun-pl.latusikl.notes.NotesSimulation -DbaseUrl=http://<IP_ADDRES>:8082
```

7. `CreateFetchPatchDelete`
```shell
gradle gatlingRun-pl.latusikl.database.CreateFetchPatchDeleteSimulation -DbaseUrl=http://<IP_ADDRES>:8082
```

8. `CreateFetchPutDelete` (Optional)
```shell
gradle gatlingRun-pl.latusikl.database.CreateFetchPutDeleteSimulation -DbaseUrl=http://<IP_ADDRES>:8082
```

