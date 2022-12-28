package pl.latusikl.sorting.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import pl.latusikl.sorting.dto.SortingRequestDto;
import pl.latusikl.sorting.dto.SortingResponseDto;
import pl.latusikl.sorting.services.SortingService;
import pl.latusikl.sorting.services.models.SortingDirection;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Validated
@Controller(value = "/sorters")
@RequiredArgsConstructor
public class SortingController {

	private final SortingService sortingService;

	@Post(uri = "/sort", produces = MediaType.APPLICATION_JSON)
	public Mono<SortingResponseDto> sortNumbers(
			@QueryValue(value = "direction", defaultValue = "ASCENDING") final SortingDirection direction,
			@Valid @Body final SortingRequestDto sortingRequestDto) {
		return switch (direction) {
			case ASCENDING -> sortingService.sortAscending(sortingRequestDto);
			case DESCENDING -> sortingService.sortDescending(sortingRequestDto);
		};
	}

}
