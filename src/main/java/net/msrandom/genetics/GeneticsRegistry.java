package net.msrandom.genetics;

import java.util.function.IntSupplier;

public class GeneticsRegistry {
    private final IntSupplier geneticCount;
    private final Runnable addKey;
    private int current;

    public GeneticsRegistry(IntSupplier geneticCount, Runnable addKey) {
        this.geneticCount = geneticCount;
        this.addKey = addKey;
    }

    public <T extends Enum<T> & Allele> Gene<T> register(Gene<T> gene) {
        gene.pos = current;
        current += gene.size;
        if ((current / 32) > geneticCount.getAsInt() - 1) addKey.run();
        return gene;
    }

    public static class Gene<T extends Enum<T> & Allele> {
        public final T[] values;
        private final int size;
        private int pos;

        public Gene(Class<T> geneticClass) {
            this.values = geneticClass.getEnumConstants();
            this.size = (31 - Integer.numberOfLeadingZeros(values.length)) * 2;
        }

        int getSize() {
            return size;
        }

        int getPos() {
            return pos;
        }
    }
}
