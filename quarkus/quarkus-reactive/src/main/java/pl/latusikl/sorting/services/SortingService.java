package pl.latusikl.sorting.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import pl.latusikl.sorting.dto.SortingRequestDto;
import pl.latusikl.sorting.dto.SortingResponseDto;
import pl.latusikl.sorting.services.models.SortingStrategy;
import pl.latusikl.sorting.services.sorters.Sorter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class SortingService {

	private final Map<SortingStrategy, Sorter> sortersMap;

	public SortingService(final Instance<Sorter> sorters) {
		sortersMap = new EnumMap<>(SortingStrategy.class);
		sorters.forEach(sorter -> sortersMap.put(sorter.getSortingStrategy(), sorter));
	}

	public Uni<SortingResponseDto> sortAscending(final SortingRequestDto sortingRequestDto) {
		return sortAscending(sortingRequestDto.getStrategy(), toPrimitive(sortingRequestDto.getData())).map(SortingResponseDto::new);
	}

	public Uni<SortingResponseDto> sortDescending(final SortingRequestDto sortingRequestDto) {
		return sortDescending(sortingRequestDto.getStrategy(), toPrimitive(sortingRequestDto.getData())).map(SortingResponseDto::new);
	}

	private int[] toPrimitive(final Collection<Integer> data) {
		return data.stream()
				   .filter(Objects::nonNull)
				   .mapToInt(Integer::intValue)
				   .toArray();
	}

	private Uni<int[]> sortAscending(final SortingStrategy sortingStrategy, final int[] data) {
		return Uni.createFrom()
				  .item(() -> sortersMap.get(sortingStrategy)
										.sortAscending(data))
				  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}

	private Uni<int[]> sortDescending(final SortingStrategy sortingStrategy, final int[] data) {
		return Uni.createFrom()
				  .item(() -> sortersMap.get(sortingStrategy)
										.sortDescending(data))
				  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}

}
