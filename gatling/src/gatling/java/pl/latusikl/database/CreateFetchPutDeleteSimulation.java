package pl.latusikl.database;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.ResponseStatus;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CreateFetchPutDeleteSimulation extends BaseDatabaseSimulation {
	private final ScenarioBuilder scenarioCreateFetchPutDelete = scenario("Create, fetch, update all data, delete user.").exec(
																																 createUserAction())
																														 .pause(2, 10)
																														 .exec(verifyAllUserDataAction())
																														 .pause(5, 45)
																														 .exec(putUserAction())
																														 .pause(2, 15)
																														 .exec(verifyAllUserDataAction())
																														 .pause(2, 20)
																														 .exec(deleteUserAction())
																														 .exec(getUserActionNotFound());

	//@formatter:off
	protected static ChainBuilder putUserAction(){
		return	exec(session -> session.set(USER_NAME_ATTRIB,UserBodyUtils.randomizedName())
										 .set(USER_SURNAME_ATTRIB, UserBodyUtils.randomizedSurname())
										 .set(USER_PASS_ATTRIB, UserBodyUtils.randomizedPassword()))
				.exec(http("Update all updatable user data")
							  .put(session -> session.getString(USER_LINK_ATTRIB))
							  .asJson()
							  .body(StringBody(session -> UserBodyUtils.putUserBody(session.getString(USER_NAME_ATTRIB),
																					session.getString(USER_SURNAME_ATTRIB),
																					session.getString(USER_PASS_ATTRIB))))
							  .check(status().is(ResponseStatus.OK))
				);
	}
	//@formatter:on
	{
		setUp(scenarioCreateFetchPutDelete.injectOpen(atOnceUsers(250), rampUsers(500).during(Duration.ofMinutes(1)),
													  constantUsersPerSec(30).during(Duration.ofMinutes(2)))).assertions(
																													 global().failedRequests()
																															 .percent()
																															 .lt(Constants.SUCCESS_PERCENTAGE))
																											 .protocols(
																													 HttpProtocolProvider.getBaseProtocolConfig());
	}
}
