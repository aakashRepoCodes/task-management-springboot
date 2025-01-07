package com.task.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Priority {
    @JsonProperty("low") Low,
    @JsonProperty("medium") Medium,
    @JsonProperty("high") High
}
