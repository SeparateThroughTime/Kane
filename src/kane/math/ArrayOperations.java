package kane.math;

import java.lang.reflect.Array;

public class ArrayOperations {
	
	/**
	 * Checks if the array contains the object.
	 * @param array
	 * @param object
	 * @return
	 */
	public static boolean contains(Object[] array, Object object) {
		boolean contains = false;
		for (Object arrayElement : array) {
			if(arrayElement.equals(object)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	/**
	 * Adds the object to the array.
	 * For performance it doesnt check the Object type.
	 * @param array
	 * @param object
	 * @return
	 */
	public static <T> T[] add(T[] array, T object) {
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = object;
		return newArray;
	}
}
