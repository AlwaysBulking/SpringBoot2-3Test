package pl.latusikl.sorting.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.latusikl.sorting.dto.SortingRequestDto;
import pl.latusikl.sorting.dto.SortingResponseDto;
import pl.latusikl.sorting.services.SortingService;
import pl.latusikl.sorting.services.models.SortingDirection;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/sorters")
@RequiredArgsConstructor
public class SortingController {

	private final SortingService sortingService;

	@PostMapping(path = "/sort", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<SortingResponseDto> sortNumbers(
			@RequestParam(name = "direction", defaultValue = "ASCENDING") final SortingDirection direction,
			@RequestBody @Valid final SortingRequestDto sortingRequestDto) {
		return switch (direction) {
			case ASCENDING -> sortingService.sortAscending(sortingRequestDto);
			case DESCENDING -> sortingService.sortDescending(sortingRequestDto);
		};
	}
}
