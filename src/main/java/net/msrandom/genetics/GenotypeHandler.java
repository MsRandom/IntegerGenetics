package net.msrandom.genetics;

import java.util.HashMap;
import java.util.Map;

public class GenotypeHandler {
    private final Map<GeneticsRegistry.Gene<?>, Locus<?>> geneticCache = new HashMap<>();
    private final GeneticGetter getter;
    private final GeneticSetter setter;

    public GenotypeHandler(GeneticGetter getter, GeneticSetter setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public <E extends Enum<E> & Allele> void set(GeneticsRegistry.Gene<E> type, E left, E right) {
        final int position = type.getPosition();
        final int index = position / 32;
        final int relative = position - index * 32;
        final int size = type.getSize() / 2;

        int storedData = getter.invoke(index) & ~((1 << type.getSize()) - 1 << relative); // Current value of the integer but with the bits we need removed entirely.
        storedData |= left.ordinal() << relative; // OR the left side to it, ironically on the right
        storedData |= right.ordinal() << relative + size; // And then OR the right side, after the left side(size indicates the amount of bits it takes)

        setter.invoke(index, storedData);
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<E> & Allele> Locus<E> get(GeneticsRegistry.Gene<E> type) {
        Locus<E> locus = (Locus<E>) geneticCache.get(type);
        final int position = type.getPosition();
        final int index = position / 32;
        final int relative = position - index * 32;
        final int value = getter.invoke(index) >> relative;
        final int size = type.getSize() / 2;
        final int max = (1 << size) - 1;

        final E[] values = type.getValues();
        final E left = values[value & max];
        final E right = values[(value >> size) & max];
        if (locus == null || locus.getLeft() != left || locus.getRight() != right) {
            locus = new Locus<>(left, right);
            geneticCache.put(type, locus);
        }
        return locus;
    }

    @FunctionalInterface
    public interface GeneticGetter {
        int invoke(int index);
    }

    @FunctionalInterface
    public interface GeneticSetter {
        void invoke(int index, int value);
    }
}
