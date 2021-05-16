package net.msrandom.genetics;

// A set of allele enums that contain different amounts of elements
public class TestGenetics {
    // Should use 1 bit
    public enum Allele1 implements Allele {
        A("a");

        private final String allele;

        Allele1(String allele) {
            this.allele = allele;
        }

        @Override
        public String getAllele() {
            return allele;
        }
    }

    // Should use 1 bit
    public enum Allele2 implements Allele {
        B("b"), C("c");

        private final String allele;

        Allele2(String allele) {
            this.allele = allele;
        }

        @Override
        public String getAllele() {
            return allele;
        }
    }

    // Should use 2 bits
    public enum Allele3 implements Allele {
        D("d"), E("e"), F("f");

        private final String allele;

        Allele3(String allele) {
            this.allele = allele;
        }

        @Override
        public String getAllele() {
            return allele;
        }
    }

    // Should use 2 bits
    public enum Allele4 implements Allele {
        G("g"), H("h"), I("i");

        private final String allele;

        Allele4(String allele) {
            this.allele = allele;
        }

        @Override
        public String getAllele() {
            return allele;
        }
    }
}
