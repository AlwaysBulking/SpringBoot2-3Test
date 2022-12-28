package pl.latusikl.greetings;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Paths;
import pl.latusikl.common.Properties;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.sse;

public class GreetingSseSimulation extends Simulation {

	ScenarioBuilder scn = scenario("GET Greetings as SSE").forever()
														  .on(exec(sse("Greeting SSE").connect(Paths.GREETING_SSE)
																					  .await(Constants.SSE_WAIT_TIME)
																					  .on(sse.checkMessage("Check greeting message")
																							 .matching(substring("value"))
																							 .check(regex(
																									 ".*Hi\\! Nice to see You here\\!.*")))).pause(
																																					Constants.SSE_PAUSE)
																																			.exec(sse(
																																					"Close").close()));

	{
		setUp(scn.injectOpen(atOnceUsers(Properties.sseUsers()))).assertions(global().failedRequests()
																								.percent()
																								.lt(Constants.SUCCESS_PERCENTAGE))
																			.protocols(HttpProtocolProvider.getBaseProtocolConfig())
																			.maxDuration(
																					Duration.ofMinutes(Properties.sseTime()));
	}
}
