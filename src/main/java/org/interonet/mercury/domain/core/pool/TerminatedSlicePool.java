package org.interonet.mercury.domain.core.pool;


import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

@Component
public class TerminatedSlicePool extends SlicePool {
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.TERMINATED && slicePool.add(slice);
    }
}
