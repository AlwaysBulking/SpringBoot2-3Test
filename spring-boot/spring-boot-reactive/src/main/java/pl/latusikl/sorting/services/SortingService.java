package pl.latusikl.sorting.services;

import org.springframework.stereotype.Service;
import pl.latusikl.sorting.dto.SortingRequestDto;
import pl.latusikl.sorting.dto.SortingResponseDto;
import pl.latusikl.sorting.services.models.SortingStrategy;
import pl.latusikl.sorting.services.sorters.Sorter;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SortingService {
	private final Map<SortingStrategy, Sorter> sortersMap;

	public SortingService(final List<Sorter> sorters) {
		sortersMap = new EnumMap<>(SortingStrategy.class);
		sorters.forEach(sorter -> sortersMap.put(sorter.getSortingStrategy(), sorter));
	}

	public Mono<SortingResponseDto> sortAscending(final SortingRequestDto sortingRequestDto) {
		return sortAscending(sortingRequestDto.getStrategy(), toPrimitive(sortingRequestDto.getData())).map(SortingResponseDto::new);
	}

	public Mono<SortingResponseDto> sortDescending(final SortingRequestDto sortingRequestDto) {
		return sortDescending(sortingRequestDto.getStrategy(), toPrimitive(sortingRequestDto.getData())).map(SortingResponseDto::new);
	}

	private int[] toPrimitive(final Collection<Integer> data) {
		return data.stream()
				   .filter(Objects::nonNull)
				   .mapToInt(Integer::intValue)
				   .toArray();
	}

	private Mono<int[]> sortAscending(final SortingStrategy sortingStrategy, final int[] data) {
		return Mono.fromCallable(() -> sortersMap.get(sortingStrategy)
												 .sortAscending(data))
				   .subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<int[]> sortDescending(final SortingStrategy sortingStrategy, final int[] data) {
		return Mono.fromCallable(() -> sortersMap.get(sortingStrategy)
												 .sortDescending(data))
				   .subscribeOn(Schedulers.boundedElastic());
	}

}
