package com.github.mbmll.datax.core.properties;


import static com.github.mbmll.datax.core.constants.RuntimeModel.STANDALONE;

public class JobProperties {
    private Long jobId = -1L;
    private String runtimeModel = STANDALONE;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getRuntimeModel() {
        return runtimeModel;
    }

    public void setRuntimeModel(String runtimeModel) {
        this.runtimeModel = runtimeModel;
    }
}
