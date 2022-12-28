package pl.latusikl.sorting.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.latusikl.sorting.services.models.SortingStrategy;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Introspected
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SortingRequestDto {
	@NotNull
	private final Collection<Integer> data;
	@NotNull
	private final SortingStrategy strategy;
}
