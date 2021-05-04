package net.msrandom.genetics;

import java.util.Objects;

public class Locus<T extends Enum<T> & Allele> {
    private final T left;
    private final T right;
    private final T dominant;

    public Locus(T left, T right) {
        this.left = left;
        this.right = right;
        dominant = left.ordinal() < right.ordinal() ? left : right;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    public T getDominant() {
        return dominant;
    }

    public boolean has(T gene) {
        return getLeft() == gene || getRight() == gene;
    }

    public boolean has(T a, T b) {
        return (getLeft() == a && getRight() == b) || (getLeft() == b && getRight() == a);
    }

    @Override
    public String toString() {
        return left.getAllele() + "-" + right.getAllele();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locus<?>)) return false;
        Locus<?> locus = (Locus<?>) o;
        return left.equals(locus.left) &&
                right.equals(locus.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
