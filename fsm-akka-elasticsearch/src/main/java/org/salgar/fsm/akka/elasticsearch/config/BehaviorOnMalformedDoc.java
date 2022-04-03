package org.salgar.fsm.akka.elasticsearch.config;

import lombok.Getter;

public enum BehaviorOnMalformedDoc {
    IGNORE(1),
    WARN(2),
    FAIL(3);

    @Getter
    private Integer type;

    BehaviorOnMalformedDoc(Integer type) {
        this.type = type;
    }
}