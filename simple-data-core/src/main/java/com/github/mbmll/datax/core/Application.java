package com.github.mbmll.datax.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mbmll.datax.core.properties.JobProperties;
import com.github.mbmll.datax.core.utils.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/10 23:45:07
 */

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            run(new ObjectMapper().readValue(args[0], JobProperties.class));
        } catch (Exception e) {
            log.error("DataX process exit with exception, stack trace is ", e);
            System.exit(-1);
        }
        System.exit(0);
    }

    private static void run(JobProperties jobProperties) {
        ValidUtil.validRuntimeMode(jobProperties);
    }

}
