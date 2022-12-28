package pl.latusikl.sorting;

import io.gatling.javaapi.core.ScenarioBuilder;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Properties;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class MediumNumberSetSimulation extends BaseSortingSimulation {

	private static final String FILE_10K = "sorting-10K.csv";
	//@formatter:off
	ScenarioBuilder scenario50KNumbersTest = scenario("Sort medium array using 3 different strategies.").forever()
			.on(exec(postSortingRequest(FILE_10K, STRATEGY_MERGE_SORT))
			.exec(postSortingRequest(FILE_10K,STRATEGY_QUICKSORT))
			.exec(postSortingRequest(FILE_10K,STRATEGY_HEAP_SORT)));
	//@formatter:on
	{
		setUp(scenario50KNumbersTest.injectOpen(atOnceUsers(Properties.mediumSortUsers()))).assertions(global().failedRequests()
																											   .percent()
																											   .lt(Constants.SUCCESS_PERCENTAGE))
																						   .protocols(
																								   HttpProtocolProvider.getBaseProtocolConfig())
																						   .maxDuration(Duration.ofMinutes(
																								   Properties.mediumSortTime()));
	}
}
