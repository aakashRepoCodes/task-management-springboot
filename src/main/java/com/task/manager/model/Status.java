package com.task.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("todo")TODO,
    @JsonProperty("in_progress") IN_PROGRESS,
    @JsonProperty("completed") COMPLETED
}
