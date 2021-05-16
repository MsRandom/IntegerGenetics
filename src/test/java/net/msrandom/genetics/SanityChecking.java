package net.msrandom.genetics;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SanityChecking {
    private static GenotypeHandler handler;
    private static GeneticsRegistry.Gene<TestGenetics.Allele2> allele2;
    private static GeneticsRegistry.Gene<TestGenetics.Allele3> allele3;
    private static GeneticsRegistry.Gene<TestGenetics.Allele4> allele4;

    @BeforeEach
    public void setUp() {
        IntList genetics = new IntArrayList();
        GeneticsRegistry registry = new GeneticsRegistry(genetics::size, () -> genetics.add(0));
        handler = new GenotypeHandler(genetics::getInt, genetics::set);
        allele2 = registry.register(TestGenetics.Allele2.class);
        allele3 = registry.register(TestGenetics.Allele3.class);
        allele4 = registry.register(TestGenetics.Allele4.class);
    }

    @Test
    @DisplayName("Testing the bit size and position of each registered gene")
    public void test() {
        assertEquals(2, allele2.getSize());
        assertEquals(0, allele2.getPosition());
        assertEquals(4, allele3.getSize());
        assertEquals(2, allele3.getPosition());
        assertEquals(4, allele4.getSize());
        assertEquals(6, allele4.getPosition());
    }
}
