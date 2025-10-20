package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;

import java.time.Duration;

public class ExponentialBackoffRetryPolicy {
    private final int maxRetries;
    private final long initialDelayMillis;
    private final double factor;

    public ExponentialBackoffRetryPolicy(int maxRetries, long initialDelayMillis, double factor) {
        if (maxRetries < 0) throw new IllegalArgumentException("maxRetries >= 0 required");
        if (initialDelayMillis < 0) throw new IllegalArgumentException("initialDelayMillis >= 0 required");
        this.maxRetries = maxRetries;
        this.initialDelayMillis = initialDelayMillis;
        this.factor = factor;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Returns the delay for the next retry attempt (in milliseconds).
     * attempt is 1-based (1 = first retry after initial failure).
     */
    public long delayMillisForAttempt(int attempt) {
        if (attempt <= 0) return 0L;
        double delay = initialDelayMillis * Math.pow(factor, attempt - 1);
        long millis = (long) Math.min(delay, (double) Long.MAX_VALUE);
        return millis;
    }

    public Duration delayDurationForAttempt(int attempt) {
        return Duration.ofMillis(delayMillisForAttempt(attempt));
    }
}

