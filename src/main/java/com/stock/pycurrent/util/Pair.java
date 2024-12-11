package com.stock.pycurrent.util;


import org.springframework.lang.Nullable;

/**
 * 引入一个简简单单的Pair, 用于返回值返回两个元素.
 * from Twitter Common
 */
public record Pair<A, B>(@Nullable A first, @Nullable B second) {

    /**
     * Creates a new pair.
     *
     * @param first  The first value.
     * @param second The second value.
     */
    public Pair {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pair other = (Pair) obj;
        if (first == null) {
            if (other.first != null) {
                return false;
            }
        } else if (!first.equals(other.first)) {
            return false;
        }
        if (second == null) {
            if (other.second != null) {
                return false;
            }
        } else if (!second.equals(other.second)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Pair
     */
    public static <A, B> Pair<A, B> of(@Nullable A a, @Nullable B b) {
        return new Pair<A, B>(a, b);
    }
}
