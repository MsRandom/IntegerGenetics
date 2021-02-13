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
        int index = type.getPos() / 32;
        int relative = type.getPos() - (index * 32);
        setter.invoke(index, getter.invoke(index) & ~((1 << type.getSize()) - 1 << relative) | left.ordinal() << relative | right.ordinal() << relative + (type.getSize() >> 1));
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<E> & Allele> Locus<E> get(GeneticsRegistry.Gene<E> type) {
        Locus<E> locus = (Locus<E>) geneticCache.computeIfAbsent(type, k -> new Locus<>());
        int index = type.getPos() / 32;
        int value = getter.invoke(index) >> type.getPos() - index * 32 & (1 << type.getSize()) - 1;
        int size = type.getSize() >> 1;
        int most = (1 << size) - 1;
        return locus.setup(type.values[value & most], type.values[(value >> size) & most]);
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
