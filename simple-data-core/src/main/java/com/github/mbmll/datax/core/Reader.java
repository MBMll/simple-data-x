package com.github.mbmll.datax.core;


import java.util.function.Consumer;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/12 15:05    
 */

public interface Reader<T> {
    void read(Consumer<T>  consumer);
}
