package org.salgar.akka.fsm.api;

import java.util.Map;

public interface UseCaseKeyStrategy {
    String getKey(Map<String, Object> payload);
}