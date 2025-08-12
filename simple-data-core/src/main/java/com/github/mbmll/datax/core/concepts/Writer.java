package com.github.mbmll.datax.core.concepts;


import com.github.mbmll.datax.core.RowChannel;

public interface Writer<E> {
    void write(RowChannel<E> queue);

}
