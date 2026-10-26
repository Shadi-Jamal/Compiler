package de.uniwue.jpp.compiler.lowering;

public class Stacksizeimplement implements StackSize{

    private int currentSize = 0;
    private int max = 0;

    //Stack<>
    @Override
    public int getCurrentStackSize() {
        return currentSize;
    }

    @Override
    public int getMaxStackSize() {
        return max;
    }

    @Override
    public int allocate(int amount) {
        int previousSize = currentSize;
        currentSize += amount;
        if (currentSize > max) {
            max = currentSize;
        }
        return previousSize;
    }

    @Override
    public void truncate(int size) {
        currentSize = size;
    }
    public static StackSize create(){
        return new Stacksizeimplement();
    }
}
