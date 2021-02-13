import net.msrandom.genetics.Allele;
import net.msrandom.genetics.GeneticsRegistry;
import net.msrandom.genetics.GenotypeHandler;
import net.msrandom.genetics.Locus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneticTest {
    static List<Integer> genetics;
    static GeneticsRegistry registry;
    static GenotypeHandler handler;
    static GeneticsRegistry.Gene<AlleleExample> geneExample;
    static GeneticsRegistry.Gene<AlleleExample> geneExample2;

    @BeforeEach
    public void setUp() {
        genetics = new ArrayList<>();
        registry = new GeneticsRegistry(genetics::size, () -> genetics.add(0));
        handler = new GenotypeHandler(genetics::get, genetics::set);
        GeneticsRegistry.Gene<AlleleExample> gene = new GeneticsRegistry.Gene<>(AlleleExample.class);
        geneExample = registry.register(gene);
        for (int i = 0; i < 31; i++) {
            registry.register(gene);
        }
        geneExample2 = registry.register(gene);

    }

    @Test
    @DisplayName("Setting a gene")
    public void test() {
        //Defaults
        Locus<AlleleExample> locus = handler.get(geneExample);
        assertEquals(locus.getLeft(), locus.getRight()); //Both should be the default value, as they were not set.
        assertEquals(locus.getDominant(), AlleleExample.A); //The dominant is the highest on the enum, and because the left and right sides are equal, that would be the default A.

        //Setting geneExample to "b-a"
        handler.set(geneExample, AlleleExample.B, AlleleExample.A);
        locus = handler.get(geneExample);
        assertEquals(locus.getLeft(), AlleleExample.B);
        assertEquals(locus.getRight(), AlleleExample.A);
        assertEquals(locus.getDominant(), AlleleExample.A);
        assertEquals(locus.toString(), "b-a");

        handler.set(geneExample2, AlleleExample.B, AlleleExample.A);
        locus = handler.get(geneExample2);
        assertEquals(locus.getLeft(), AlleleExample.B);
        assertEquals(locus.getRight(), AlleleExample.A);
    }

    private enum AlleleExample implements Allele {
        A("a"), B("b");

        private final String allele;

        AlleleExample(String allele) {
            this.allele = allele;
        }

        @Override
        public String getAllele() {
            return allele;
        }
    }
}
