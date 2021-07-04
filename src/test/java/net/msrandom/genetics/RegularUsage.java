package net.msrandom.genetics;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegularUsage {
    private static GeneticsRegistry registry;
    private static GenotypeHandler handler;
    private static GeneticsRegistry.Gene<TestGenetics.Allele2> geneExample;
    private static GeneticsRegistry.Gene<TestGenetics.Allele2> geneExample2;

    @BeforeAll
    public static void setup() {
        IntList genetics = new IntArrayList();
        registry = new GeneticsRegistry(genetics::size, () -> genetics.add(0));
        handler = new GenotypeHandler(genetics::getInt, genetics::set);
        geneExample = registry.register(TestGenetics.Allele2.class);

        //For testing using more than one integer's bits can handle(15 + 1 for geneExample2, multiplied by 2 == 32).
        for (int i = 0; i < 15; i++) {
            registry.register(TestGenetics.Allele2.class);
        }
        geneExample2 = registry.register(TestGenetics.Allele2.class);
    }

    @Test
    @DisplayName("Setting and getting a gene")
    public void test() {
        //Defaults
        Locus<TestGenetics.Allele2> locus = handler.get(geneExample);
        assertSame(locus.getLeft(), locus.getRight()); //Both should be the default value, as they were not set.
        assertEquals(TestGenetics.Allele2.B, locus.getDominant()); //The dominant is the highest on the enum, and because the left and right sides are equal, that would be the default A.

        //Setting geneExample to "b-a"
        handler.set(geneExample, TestGenetics.Allele2.C, TestGenetics.Allele2.B);
        locus = handler.get(geneExample);
        assertEquals(TestGenetics.Allele2.C, locus.getLeft());
        assertEquals(TestGenetics.Allele2.B, locus.getRight());
        assertEquals(TestGenetics.Allele2.B, locus.getDominant());
        assertEquals("c-b", locus.toString());

        assertTrue(locus.has(TestGenetics.Allele2.B), "Locus should have contained allele 'b' but it did not.");
        assertTrue(locus.has(TestGenetics.Allele2.B, TestGenetics.Allele2.C), "Locus should have contained alleles 'b' and 'c' but it did not.");

        //Registering a type that doesn't match the specifications
        boolean caughtError = false;
        try {
            registry.register(TestGenetics.Allele1.class);
        } catch (IllegalArgumentException exception) {
            caughtError = true;
        }

        assertTrue(caughtError, "Expected " + TestGenetics.Allele1.class.getName() + " to fail when registering, but it did not.");

        //Testing using too many bits(which overflows to the next integer in the array)
        handler.set(geneExample2, TestGenetics.Allele2.C, TestGenetics.Allele2.B);
        Locus<TestGenetics.Allele2> locus2 = handler.get(geneExample2);
        assertEquals(locus2.getLeft(), TestGenetics.Allele2.C);
        assertEquals(locus2.getRight(), TestGenetics.Allele2.B);

        //Testing .equals and .hashCode
        assertEquals(locus, locus2, "Locus objects that should be equal returned false on .equals");
        assertEquals(locus.hashCode(), locus2.hashCode(), "Hash code for matching Locus objects did not match.");
    }
}
