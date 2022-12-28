package pl.latusikl.database;

import io.gatling.javaapi.core.ScenarioBuilder;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Properties;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class CreateFetchDeleteSimulation extends BaseDatabaseSimulation {

	private ScenarioBuilder scenarioCreateFetchDelete = scenario("Create fetch and delete user").forever()
																								.on(exec(createUserAction()).exec(
																																	verifyAllUserDataAction())
																															.exec(deleteUserAction())
																															.exec(getUserActionNotFound()));

	{
		setUp(scenarioCreateFetchDelete.injectOpen(atOnceUsers(Properties.createReadDeleteUsers()))).assertions(
																											global().failedRequests()
																													.percent()
																													.lt(Constants.SUCCESS_PERCENTAGE))
																									.protocols(
																											HttpProtocolProvider.getBaseProtocolConfig())
																									.maxDuration(Duration.ofMinutes(
																											Properties.createReadDeleteTime()));
	}
}
