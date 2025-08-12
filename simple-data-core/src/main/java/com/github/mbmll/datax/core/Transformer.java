package com.github.mbmll.datax.core;


/**
 * @Author xlc
 * @Description
 * @Date 2025/8/12 15:34    
 */

public interface Transformer<E> {
    E transform(E e);
}
