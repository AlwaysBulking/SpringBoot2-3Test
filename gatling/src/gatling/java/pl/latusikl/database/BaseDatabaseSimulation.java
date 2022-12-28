package pl.latusikl.database;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Paths;
import pl.latusikl.common.ResponseStatus;

import static io.gatling.javaapi.core.CoreDsl.*;

import static io.gatling.javaapi.http.HttpDsl.*;

public class BaseDatabaseSimulation extends Simulation {

	protected static final String USER_ID_ATTRIB = "userId";
	protected static final String USER_LINK_ATTRIB = "userLink";
	protected static final String USER_NAME_ATTRIB = "userName";
	protected static final String USER_SURNAME_ATTRIB = "userSurname";
	protected static final String USER_PASS_ATTRIB = "userPass";
	protected static final String USER_EMAIL_ATTRIB = "userEmail";

	//@formatter:off
	 protected static ChainBuilder createUserAction() {
		return exec(session -> session.set(USER_NAME_ATTRIB, UserBodyUtils.randomizedName())
									  .set(USER_SURNAME_ATTRIB, UserBodyUtils.randomizedSurname())
									  .set(USER_PASS_ATTRIB, UserBodyUtils.randomizedPassword())
									  .set(USER_EMAIL_ATTRIB, UserBodyUtils.randomizedEmail()))
				.exec(http("Create user")
							  .post(Paths.USERS)
								   .asJson()
								   .body(StringBody(session -> UserBodyUtils.createUserBody(session.getString(USER_NAME_ATTRIB),
																							session.getString(USER_SURNAME_ATTRIB),
																							session.getString(USER_PASS_ATTRIB),
																							session.getString(USER_EMAIL_ATTRIB))))
								   .check(status().is(ResponseStatus.CREATED))
								   .check(jmesPath("link").saveAs(USER_LINK_ATTRIB),
										  jmesPath("id").saveAs(USER_ID_ATTRIB)));
	}
	protected static HttpRequestActionBuilder verifyAllUserDataAction(){
		return getUserActionOk()
				.check(jmesPath(UserBodyUtils.USER_EMAIL_PATH).is(session -> session.getString(USER_EMAIL_ATTRIB)),
					   jmesPath(UserBodyUtils.USER_NAME_PATH).is(session -> session.getString(USER_NAME_ATTRIB)),
					   jmesPath(UserBodyUtils.USER_SURNAME_PATH).is(session -> session.getString(USER_SURNAME_ATTRIB))
				);
	}
	protected static HttpRequestActionBuilder getUserActionOk() {
		return getUserActionInternal()
				.check(status().is(ResponseStatus.OK));
	}

	protected static HttpRequestActionBuilder getUserActionNotFound() {
		return getUserActionInternal()
				.check(status().is(ResponseStatus.NOT_FOUND));
	}

	protected static  HttpRequestActionBuilder getUserActionInternal(){
		return http("Fetch user")
				.get(session -> session.getString(USER_LINK_ATTRIB))
				.asJson();
	}

	protected static HttpRequestActionBuilder deleteUserAction() {
		return http("Delete user").delete(session -> Paths.USERS + "/" + session.getString(USER_ID_ATTRIB))
								  .check(status().is(ResponseStatus.NO_CONTENT));
	}
	//@formatter:on
}
