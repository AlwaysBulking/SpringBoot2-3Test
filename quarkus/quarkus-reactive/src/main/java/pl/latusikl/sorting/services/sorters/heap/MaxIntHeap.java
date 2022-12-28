package pl.latusikl.sorting.services.sorters.heap;

import java.util.Arrays;

public class MaxIntHeap {

	private int capacity = 20;
	private int size = 0;

	private int[] items;

	public MaxIntHeap() {
		items = new int[capacity];
	}

	public int getHeapSize() {
		return size;
	}

	private int getLeftChildIndex(final int parentIndex) {return 2 * parentIndex + 1;}

	private int getRightChildIndex(final int parentIndex) {return 2 * parentIndex + 2;}

	private int getParentIndex(final int childIndex) {return (childIndex - 1) / 2;}

	private boolean hasLeftChild(final int parentIndex) {return getLeftChildIndex(parentIndex) < size;}

	private boolean hasRightChild(final int parentIndex) {return getRightChildIndex(parentIndex) < size;}

	private boolean hasParent(final int index) {return getParentIndex(index) >= 0;}

	private int leftChild(final int index) {return items[getLeftChildIndex(index)];}

	private int rightChild(final int index) {return items[getRightChildIndex(index)];}

	private int parent(final int index) {return items[getParentIndex(index)];}

	private void swap(final int indexOne, final int indexTwo) {
		final var tmp = items[indexOne];
		items[indexOne] = items[indexTwo];
		items[indexTwo] = tmp;
	}

	private void increaseCapacityIfNeeded() {
		if (size == capacity) {
			items = Arrays.copyOf(items, size * 2);
			capacity *= 2;
		}
	}

	public int pop() {
		if (size == 0) {
			throw new IllegalStateException("Heap is empty.");
		}
		final var itemToPop = items[0];
		items[0] = items[size - 1];
		size--;
		heapifyDown();
		return itemToPop;
	}

	private void heapifyDown() {
		int currentIndex = 0;
		while (hasLeftChild(currentIndex)) {
			final int biggerChildIndex;
			if (hasRightChild(currentIndex) && rightChild(currentIndex) > leftChild(currentIndex)) {
				biggerChildIndex = getRightChildIndex(currentIndex);
			}
			else {
				biggerChildIndex = getLeftChildIndex(currentIndex);
			}

			if (items[currentIndex] > items[biggerChildIndex]) {
				break;
			}

			swap(currentIndex, biggerChildIndex);
			currentIndex = biggerChildIndex;
		}
	}

	public void add(final int item) {
		increaseCapacityIfNeeded();
		items[size] = item;
		size++;
		heapifyUp();
	}

	private void heapifyUp() {
		int currentIndex = size - 1;
		while (hasParent(currentIndex) && parent(currentIndex) < items[currentIndex]) {
			final int parentIndex = getParentIndex(currentIndex);
			swap(parentIndex, currentIndex);
			currentIndex = parentIndex;
		}
	}

}
