package pl.latusikl.notes;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import pl.latusikl.common.Constants;
import pl.latusikl.common.Headers;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Paths;
import pl.latusikl.common.ResponseStatus;
import pl.latusikl.common.Utils;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class NotesSimulation extends Simulation {

	private static final String BASE_NOTE_VALUE = "This is sample note value to be sent. ";
	private static final String NOTE_CONTENT_ATTRIB = "noteVal";
	private static final String NOTE_KEY_ATTRIB = "noteKey";
	private static final String NOTE_ID_ATTRIB = "noteId";

	//@formatter:off
	ScenarioBuilder scn = scenario("Create and retrieve a note.").exec(
																		 session -> session.set(NOTE_CONTENT_ATTRIB, BASE_NOTE_VALUE + Utils.randomUUIDAsString()))
																 .exec(http("Create note")
																			   .post(Paths.NOTES)
																						  .asJson()
																						  .body(StringBody("{\"noteValue\": \"#{noteVal}\"}"))
																						  .check(status().is(ResponseStatus.OK))
																						  .check(jmesPath("id").saveAs(NOTE_ID_ATTRIB),
																								 jmesPath("key").saveAs(NOTE_KEY_ATTRIB))
																 ).pause(2, 45)
																 .exec(http("Fetch note")
																			   .get(session -> Paths.NOTES + "/" + session.getString(NOTE_ID_ATTRIB))
																						 .queryParam("noteKey", session -> session.getString(NOTE_KEY_ATTRIB))
																						 .header(Headers.Names.ACCEPT,
																								 Headers.Values.TEXT_PLAIN)
																						 .check(status().is(ResponseStatus.OK))
																						 .check(bodyString().is(session -> session.getString(NOTE_CONTENT_ATTRIB)))
																 );
	//@formatter:on
	{
		setUp(scn.injectOpen(atOnceUsers(250), rampUsers(500).during(Duration.ofMinutes(1)),
							 constantUsersPerSec(30).during(Duration.ofMinutes(2)))).assertions(global().failedRequests()
																										.percent()
																										.lt(Constants.SUCCESS_PERCENTAGE))
																					.protocols(
																							HttpProtocolProvider.getBaseProtocolConfig());
	}

}
