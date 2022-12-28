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

public class CreateFetchPatchDeleteSimulation extends BaseDatabaseSimulation {
	private final ScenarioBuilder scenarioCreateFetchPatchNameDelete = scenario("Create, fetch, update name, delete user.").exec(
																																   createUserAction())
																														   .pause(2,
																																  10)
																														   .exec(verifyAllUserDataAction())
																														   .pause(5,
																																  45)
																														   .exec(patchNameUserAction())
																														   .pause(2,
																																  15)
																														   .exec(getUserActionOk().check(
																																   jmesPath(
																																		   UserBodyUtils.USER_NAME_PATH).is(
																																		   session -> session.getString(
																																				   USER_NAME_ATTRIB))))
																														   .pause(2,
																																  20)
																														   .exec(deleteUserAction())
																														   .exec(getUserActionNotFound());

	//@formatter:off
	protected static ChainBuilder patchNameUserAction(){
		return	exec(session -> session.set(USER_NAME_ATTRIB,UserBodyUtils.randomizedName()))
				.exec(http("Patch user name")
							  .patch(session -> session.getString(USER_LINK_ATTRIB))
							  .asJson()
							  .body(StringBody(session -> UserBodyUtils.patchUserBody(session.getString(USER_NAME_ATTRIB))))
							  .check(status().is(ResponseStatus.OK))
				);
	}
	//@formatter:on

	{
		setUp(scenarioCreateFetchPatchNameDelete.injectOpen(atOnceUsers(250), rampUsers(500).during(Duration.ofMinutes(1)),
															constantUsersPerSec(30).during(Duration.ofMinutes(2)))).assertions(
																														   global().failedRequests()
																																   .percent()
																																   .lt(Constants.SUCCESS_PERCENTAGE))
																												   .protocols(
																														   HttpProtocolProvider.getBaseProtocolConfig());
	}
}
