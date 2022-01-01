package kane.math;

import java.lang.reflect.Array;

public class ArrayOperations {

	/**
	 * Checks if the array contains the object.
	 * 
	 * @param array
	 * @param object
	 * @return
	 */
	public static boolean contains(Object[] array, Object object) {
		boolean contains = false;
		for (Object arrayElement : array) {
			if (arrayElement.equals(object)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * Adds the object to the array. For performance it doesnt check the Object
	 * type.
	 * 
	 * @param array
	 * @param object
	 * @return
	 */
	public static <T> T[] add(T[] array, T object) {
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = object;
		return newArray;
	}

	/**
	 * Cuts an array on specified index
	 * 
	 * @param <T>
	 * @param array
	 * @param numSlots
	 * @return
	 */
	public static <T> T[] cutArray(T[] array, int numSlots) {
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), numSlots);
		System.arraycopy(array, 0, newArray, 0, numSlots);
		return newArray;
	}

	/**
	 * removes every element of array equal to object. The gaps will be remove an
	 * the end values will be null. The array length ist static and will not be
	 * altered.
	 * 
	 * @param <T>
	 * @param array
	 * @param object
	 * @param numElementsToCheck - the number of Elements to check for identical
	 *                           objects. set to array.length if the whole array
	 *                           should be checked.
	 * @return - the number of entries that where deleted.
	 */
	public static <T> int remObjectStatic(T[] array, T object, int numElementsToCheck) {
		int numRemovals = 0;
		for (int i = 0; i < numElementsToCheck - numRemovals; i++) {
			T element = array[i];
			if (object.equals(element)) {
				for (int j = i; j < numElementsToCheck - numRemovals - 1; j++) {
					array[j] = array[j + 1];
				}
				array[numElementsToCheck - numRemovals - 1] = null;
				numRemovals++;
			}
		}
		return numRemovals;
	}
}
