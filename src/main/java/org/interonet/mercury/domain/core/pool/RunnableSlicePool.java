package org.interonet.mercury.domain.core.pool;


import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

@Component
public class RunnableSlicePool extends SlicePool {
    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNABLE && slicePool.add(slice);
    }
}
