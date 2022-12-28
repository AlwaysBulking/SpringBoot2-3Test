package pl.latusikl.greetings;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Paths;
import pl.latusikl.common.Properties;
import pl.latusikl.common.ResponseStatus;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SingleGreetingSimulation extends Simulation {

	private static final String GREETING_RESPONSE = "Hi! Nice to see You here!";
	ScenarioBuilder scn = scenario("Single Greeting").forever()
													 .on(exec(http("GET Greeting").get(Paths.GREETING_SINGLE)
																				  .check(status().is(ResponseStatus.OK))
																				  .check(jmesPath("value").is(GREETING_RESPONSE))));

	{
		setUp(scn.injectOpen(atOnceUsers(Properties.singleGreetingUsers()))).assertions(global().failedRequests()
																								.percent()
																								.lt(Constants.SUCCESS_PERCENTAGE))
																			.protocols(HttpProtocolProvider.getBaseProtocolConfig())
																			.maxDuration(
																					Duration.ofMinutes(Properties.singleGreetingTime()));
	}
}
