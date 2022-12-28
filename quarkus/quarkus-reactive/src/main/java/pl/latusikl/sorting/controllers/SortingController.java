package pl.latusikl.sorting.controllers;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import pl.latusikl.sorting.dto.SortingRequestDto;
import pl.latusikl.sorting.dto.SortingResponseDto;
import pl.latusikl.sorting.services.SortingService;
import pl.latusikl.sorting.services.models.SortingDirection;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sorters")
@RequiredArgsConstructor
public class SortingController {

	private final SortingService sortingService;

	@POST
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<SortingResponseDto> sortNumbers(@QueryParam("direction") @DefaultValue("ASCENDING") final SortingDirection direction,
											   @Valid final SortingRequestDto sortingRequestDto) {
		return switch (direction) {
			case ASCENDING -> sortingService.sortAscending(sortingRequestDto);
			case DESCENDING -> sortingService.sortDescending(sortingRequestDto);
		};
	}

}
