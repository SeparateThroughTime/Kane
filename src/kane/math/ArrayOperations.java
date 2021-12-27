package kane.math;

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
	public static Object[] add(Object[] array, Object object) {
		Object[] newArray = new Object[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			newArray[i] = array[i];
		}
		newArray[array.length] = object;
		return newArray;
	}
}
