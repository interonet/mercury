package org.interonet.mercury.domain.core.pool;


import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

@Component
public class TerminatedWaitingPool extends SlicePool {
    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.TERMINATED_WAITING && slicePool.add(slice);
    }
}
