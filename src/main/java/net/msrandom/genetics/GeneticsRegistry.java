package net.msrandom.genetics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class GeneticsRegistry {
    private final Object2IntMap<Gene<?>> toPosition = new Object2IntOpenHashMap<>();
    private final AtomicInteger current = new AtomicInteger();
    private final IntSupplier geneticCount;
    private final Runnable addKey;

    public GeneticsRegistry(IntSupplier geneticCount, Runnable addKey) {
        this.geneticCount = geneticCount;
        this.addKey = addKey;
    }

    public <T extends Enum<T> & Allele> Gene<T> register(Class<T> geneticClass) {
        Gene<T> gene = new Gene<>(this, geneticClass);
        toPosition.put(gene, current.getAndAdd(gene.size));
        if ((current.get() / 32) > geneticCount.getAsInt() - 1) addKey.run();
        return gene;
    }

    public static final class Gene<T extends Enum<T> & Allele> {
        private final GeneticsRegistry registry;
        private final T[] values;
        private final int size;

        private Gene(GeneticsRegistry registry, Class<T> geneticClass) {
            this.registry = registry;
            values = geneticClass.getEnumConstants();
            if (values.length < 2) {
                throw new IllegalArgumentException("Tried to register " + geneticClass.getName() + ", but it contained less than the required amount of values(expected at least 2, got " + values.length + ").");
            }
            size = (32 - Integer.numberOfLeadingZeros(values.length - 1)) * 2;
        }

        T[] getValues() {
            return values;
        }

        int getSize() {
            return size;
        }

        int getPosition() {
            return registry.toPosition.getInt(this);
        }
    }
}
