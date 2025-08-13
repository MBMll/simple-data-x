package com.github.mbmll.datax.core.concepts;


/**
 * @param <E> the element type
 */
public interface Transformer<E> {
    /**
     * @param e
     *
     * @return
     */
    E transform(E e);
}
