package org.interonet.mercury.domain.core.pool;


import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

@Component
public class RunningSlicePool extends SlicePool {
    @Override
    public synchronized boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNING && slicePool.add(slice);
    }
}
