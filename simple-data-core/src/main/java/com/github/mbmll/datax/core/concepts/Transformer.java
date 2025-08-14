package com.github.mbmll.datax.core.concepts;


import com.github.mbmll.datax.core.exceptions.DataXException;

/**
 * @param <E> the element type
 */
public interface Transformer<E> {
    /**
     * @param e
     *
     * @return
     */
    E transform(E e) throws DataXException;
}
