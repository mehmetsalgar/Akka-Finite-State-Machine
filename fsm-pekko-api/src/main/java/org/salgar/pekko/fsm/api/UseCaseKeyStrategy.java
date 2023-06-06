package org.salgar.pekko.fsm.api;

import java.util.Map;

public interface UseCaseKeyStrategy {
    String getKey(Map<String, Object> payload);
}