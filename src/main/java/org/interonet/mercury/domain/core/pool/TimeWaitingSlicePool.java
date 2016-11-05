package org.interonet.mercury.domain.core.pool;

import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

@Component
public class TimeWaitingSlicePool extends SlicePool {
    public TimeWaitingSlicePool() {
    }

    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.TIME_WAITING && slicePool.add(slice);
    }


}
