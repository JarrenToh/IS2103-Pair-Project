/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

/**
 *
 * @author wjahoward
 */
public class Pair<T, U> {
    private final T t;
    private final U u;

    private Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public static <T, U> Pair<T, U> of(T t, U u) {
        return new Pair<T, U>(t, u);
    }

    public T first() {
        return this.t;
    }

    public U second() {
        return this.u;
    }
}
