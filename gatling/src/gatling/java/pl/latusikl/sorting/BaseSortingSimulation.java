package pl.latusikl.sorting;

import io.gatling.javaapi.core.*;
import pl.latusikl.common.Constants;
import pl.latusikl.common.HttpProtocolProvider;
import pl.latusikl.common.Paths;
import pl.latusikl.common.ResponseStatus;

import static io.gatling.javaapi.core.CoreDsl.*;

import static io.gatling.javaapi.http.HttpDsl.*;

public abstract class BaseSortingSimulation extends Simulation {
	protected static final String STRATEGY_ATTRIB = "strategy";
	protected static final String STRATEGY_MERGE_SORT = "MERGE_SORT";
	protected static final String STRATEGY_QUICKSORT = "QUICKSORT";
	protected static final String STRATEGY_HEAP_SORT = "HEAP_SORT";
	protected static final String EXPECTED_RES_ATTRIB = "expectedResult";

	//@formatter:off
	protected ChainBuilder postSortingRequest(final String fileName, final String strategy) {
		return exec(feed(csv(fileName).batch(20).random()))
				.exec(session -> session.set(STRATEGY_ATTRIB,strategy))
				.exec(http("Request sorting")
							  .post(Paths.SORTING)
							  .queryParam("direction","#{directionRandom}")
							  .asJson()
							  .body(StringBody("""
											   {
											   "strategy": "#{strategy}",
											   "data": #{data}
											   }
											   """))
							  .check(status().is(ResponseStatus.OK))
							  .check(jmesPath("sortedData").is(session -> session.getString(EXPECTED_RES_ATTRIB)))
				);
	}
	//@formatter:on
}
