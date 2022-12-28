package pl.latusikl.sorting;

import io.gatling.javaapi.core.ScenarioBuilder;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class SmallNumberSetSimulation extends BaseSortingSimulation {

	private static final String FILE_2K = "sorting-2K.csv";

	//@formatter:off
	ScenarioBuilder scenario500KNumbersTest = scenario("Sort large array using 3 different strategies.")
			.exec(postSortingRequest(FILE_2K, STRATEGY_MERGE_SORT))
			.exec(postSortingRequest(FILE_2K,STRATEGY_QUICKSORT))
			.exec(postSortingRequest(FILE_2K,STRATEGY_HEAP_SORT));
	//@formatter:on
	{
		setUp(scenario500KNumbersTest.injectOpen(atOnceUsers(250), rampUsers(500).during(Duration.ofMinutes(1)),
												 constantUsersPerSec(30).during(Duration.ofMinutes(2)))).assertions(
																												 global().failedRequests()
																														 .percent()
																														 .lt(Constants.SUCCESS_PERCENTAGE))
																										 .protocols(
																												 HttpProtocolProvider.getBaseProtocolConfig());
	}

}
