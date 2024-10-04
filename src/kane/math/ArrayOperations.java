package kane.math;

import java.lang.reflect.Array;

public class ArrayOperations{

    public static boolean contains(Object[] array, Object object){
        boolean contains = false;
        for (Object arrayElement : array){
            if (arrayElement.equals(object)){
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static <T> T[] add(T[] array, T object){
        @SuppressWarnings("unchecked") T[] newArray =
                (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = object;
        return newArray;
    }

    public static <T> T[] cutArray(T[] array, int numSlots){
        @SuppressWarnings("unchecked") T[] newArray =
                (T[]) Array.newInstance(array.getClass().getComponentType(), numSlots);
        System.arraycopy(array, 0, newArray, 0, numSlots);
        return newArray;
    }

    public static <T> int remObjectStatic(T[] array, T object, int numElementsToCheck){
        int numRemovals = 0;
        for (int i = 0; i < numElementsToCheck - numRemovals; i++){
            T element = array[i];
            if (object.equals(element)){
                numRemovals++;
                for (int j = i; j < numElementsToCheck - numRemovals; j++){
                    array[j] = array[j + 1];
                }
                array[numElementsToCheck - numRemovals] = null;

            }
        }
        return numRemovals;
    }

    public static <T> void remSingleObjectStatic(T[] array, int index, int numElementsToCheck){
        for (int i = index; i < numElementsToCheck - 1; i++){
            array[i] = array[i + 1];
        }
        array[numElementsToCheck - 1] = null;
    }

    public static <T> T[] remEmpty(T[] array){
        int elementCounter = 0;
        for (int i = 0; i < array.length; i++){
            if (array[i] != null){
                elementCounter++;
            }
        }

        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), elementCounter);
        elementCounter = 0;
        for(int i = 0; i < array.length; i++){
            if (array[i] != null){
                newArray[elementCounter++] = array[i];
            }
        }

        return newArray;
    }
}
