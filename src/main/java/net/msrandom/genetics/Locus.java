package net.msrandom.genetics;

import java.util.Objects;

public class Locus<T extends Enum<T> & Allele> {
    private T left;
    private T right;
    private T dominant;

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    public T getDominant() {
        return dominant;
    }

    public Locus<T> setup(T left, T right) {
        this.left = left;
        this.right = right;
        dominant = left.ordinal() < right.ordinal() ? left : right;
        return this;
    }

    @Override
    public String toString() {
        return left.getAllele() + "-" + right.getAllele();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locus<?> locus = (Locus<?>) o;
        return left.equals(locus.left) &&
                right.equals(locus.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
