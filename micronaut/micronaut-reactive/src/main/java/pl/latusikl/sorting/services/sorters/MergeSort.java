package pl.latusikl.sorting.services.sorters;

import jakarta.inject.Singleton;
import pl.latusikl.sorting.services.models.SortingStrategy;

@Singleton
public class MergeSort implements Sorter {

	@Override
	public SortingStrategy getSortingStrategy() {
		return SortingStrategy.MERGE_SORT;
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
		final int[] tmpArray = new int[array.length];
		mergeSort(array, 0, array.length - 1, tmpArray, isDescending);
		return array;
	}

	private void mergeSort(final int[] arr, final int leftStart, final int rightEnd, final int[] tmpArr, final boolean isDescending) {
		if (leftStart >= rightEnd) {
			return;
		}
		final int middle = (rightEnd + leftStart) / 2;
		mergeSort(arr, leftStart, middle, tmpArr, isDescending);
		mergeSort(arr, middle + 1, rightEnd, tmpArr, isDescending);
		merge(arr, leftStart, rightEnd, tmpArr, isDescending);
	}

	private void merge(final int[] arr, final int leftStart, final int rightEnd, final int[] tmpArr, final boolean isDescending) {
		final int leftEnd = (rightEnd + leftStart) / 2;
		final int rightStart = leftEnd + 1;
		final int size = rightEnd - leftStart + 1;

		int left = leftStart;
		int right = rightStart;

		int tmpArrayIndex = leftStart;

		while (left <= leftEnd && right <= rightEnd) {
			if (isDescending ? arr[left] > arr[right] : arr[left] < arr[right]) {
				tmpArr[tmpArrayIndex] = arr[left];
				left++;
			}
			else {
				tmpArr[tmpArrayIndex] = arr[right];
				right++;
			}
			tmpArrayIndex++;
		}

		int restStartIndex;
		final int restEndIndex;

		if (left <= leftEnd) {
			restStartIndex = left;
			restEndIndex = leftEnd;
		}
		else {
			restStartIndex = right;
			restEndIndex = rightEnd;
		}

		while (restStartIndex <= restEndIndex) {
			tmpArr[tmpArrayIndex] = arr[restStartIndex];
			restStartIndex++;
			tmpArrayIndex++;
		}
		System.arraycopy(tmpArr, leftStart, arr, leftStart, size);
	}

}
