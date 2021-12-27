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
}
