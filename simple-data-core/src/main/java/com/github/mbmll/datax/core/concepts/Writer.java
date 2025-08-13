package com.github.mbmll.datax.core.concepts;


import com.github.mbmll.datax.core.RowChannel;
import com.github.mbmll.datax.core.exceptions.ClosedException;

public interface Writer<E> {
    void write(RowChannel<E> queue) throws ClosedException;

}
