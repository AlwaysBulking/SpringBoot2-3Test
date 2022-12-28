package pl.latusikl.sorting.services.sorters;

import pl.latusikl.sorting.services.models.SortingStrategy;

public interface Sorter {

	SortingStrategy getSortingStrategy();

	int[] sortAscending(final int[] array);

	int[] sortDescending(final int[] array);
}
