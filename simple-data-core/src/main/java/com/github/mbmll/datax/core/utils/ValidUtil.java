package com.github.mbmll.datax.core.utils;


import com.github.mbmll.datax.core.properties.JobProperties;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static com.github.mbmll.datax.core.constants.RuntimeModel.STANDALONE;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/11 00:55:19
 */

public interface ValidUtil {
    /**
     * 验证参数是否合法
     *
     * @param jobProperties jobProperties
     */
    static void validRuntimeMode(JobProperties jobProperties) {
        if (jobProperties.getJobId() == -1L &&
                jobProperties.getRuntimeModel().equals(STANDALONE)) {
            throw new IllegalArgumentException("jobId must be set when running in STANDALONE mode");
        }
    }
}
