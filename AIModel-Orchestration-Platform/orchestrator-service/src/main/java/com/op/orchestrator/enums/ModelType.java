package com.op.orchestrator.enums;

public enum ModelType {
    GLM4("glm4"),
    DEEPSEEK("deepseek");

    private final String modelName;

    ModelType(String modelName) {
        this.modelName = modelName;
    }
}
