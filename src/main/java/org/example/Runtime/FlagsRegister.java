package org.example.Runtime;

public class FlagsRegister {
    private boolean isEquals;
    private boolean isGreater;
    private boolean isLess;

    public boolean isEquals() {
        return isEquals;
    }

    public boolean isGreater() {
        return isGreater;
    }

    public boolean isLess() {
        return isLess;
    }

    public void setFlags(int value1, int value2) {
        this.isEquals = value1 == value2;
        this.isLess = value1 < value2;
        this.isGreater = value1 > value2;
    }
}
