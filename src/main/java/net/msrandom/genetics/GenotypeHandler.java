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
        final int index = type.getPos() / 32;
        final int relative = type.getPos() - (index * 32);
        setter.invoke(index, getter.invoke(index) & ~((1 << type.getSize()) - 1 << relative) | left.ordinal() << relative | right.ordinal() << relative + (type.getSize() >> 1));
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<E> & Allele> Locus<E> get(GeneticsRegistry.Gene<E> type) {
        Locus<E> locus = (Locus<E>) geneticCache.get(type);
        final int index = type.getPos() / 32;
        final int value = getter.invoke(index) >> type.getPos() - index * 32 & (1 << type.getSize()) - 1;
        final int size = type.getSize() >> 1;
        final int most = (1 << size) - 1;

        final E[] values = type.getValues();
        final E left = values[value & most];
        final E right = values[(value >> size) & most];
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
