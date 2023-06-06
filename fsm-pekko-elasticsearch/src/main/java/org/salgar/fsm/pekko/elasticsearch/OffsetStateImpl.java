package org.salgar.fsm.pekko.elasticsearch;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OffsetStateImpl implements OffsetState {
    private final String offset;
    private volatile boolean processed;

    @Override
    public void markProcessed() {
        this.processed = true;
    }

    @Override
    public boolean isProcessed() {
        return processed;
    }

    @Override
    public String offset() {
        return offset;
    }
}
