package pl.latusikl.sorting.services.sorters;

import pl.latusikl.sorting.services.models.SortingStrategy;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Quicksort implements Sorter {

	@Override
	public SortingStrategy getSortingStrategy() {
		return SortingStrategy.QUICKSORT;
	}

	@Override
	public int[] sortAscending(final int[] array) {
		return sort(array, false);
	}

	@Override
	public int[] sortDescending(final int[] array) {
		return sort(array, true);
	}

	private int[] sort(final int[] array, final boolean isDescending) {
		quickSort(array, 0, array.length - 1, isDescending);
		return array;
	}

	private void quickSort(final int[] array, final int start, final int end, final boolean isDescending) {
		if (start > end) {
			return;
		}
		final int partitionIndex = partition(array, start, end, isDescending);

		quickSort(array, start, partitionIndex - 1, isDescending);
		quickSort(array, partitionIndex + 1, end, isDescending);
	}

	private int partition(final int[] array, final int start, final int end, final boolean isDescending) {
		final int pivot = array[end];
		int i = start - 1;

		for (int j = start; j < end; j++) {
			if (isDescending ? array[j] > pivot : array[j] < pivot) {
				i++;
				swap(array, i, j);
			}
		}
		final int finalPivotIndex = i + 1;
		swap(array, finalPivotIndex, end);
		return finalPivotIndex;

	}

	private void swap(final int[] array, final int i, final int j) {
		if (i != j) {
			final int tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

}
