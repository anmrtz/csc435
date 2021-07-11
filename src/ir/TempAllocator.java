package ir;

import type.Type;

public class TempAllocator {
    private static final int MAX_TEMPS = 65534;

    private int next = 0;

    public TempVar allocate(Type t, TempVar.TempType tType, String label) {
        if (next >= MAX_TEMPS) {
            throw new RuntimeException("Max number of temporary variables exceeded");
        }

        return new TempVar(next++, t, tType, label);
    }

}
