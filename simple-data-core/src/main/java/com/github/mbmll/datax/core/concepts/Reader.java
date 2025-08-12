package com.github.mbmll.datax.core.concepts;


import com.github.mbmll.datax.core.RowChannel;

public interface Reader<E> {
    void read(RowChannel<E> queue);

}
