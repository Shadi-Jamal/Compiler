package de.uniwue.jpp.compiler.lowering;

public interface StackSize {
    public static StackSize create() {
        return new Stacksizeimplement();
    }

    public int getCurrentStackSize();
    public int getMaxStackSize();

    public int allocate(int amount);
    public void truncate(int size);
}
