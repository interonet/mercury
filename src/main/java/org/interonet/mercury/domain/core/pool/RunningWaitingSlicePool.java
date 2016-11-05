package org.interonet.mercury.domain.core.pool;

import org.interonet.mercury.domain.core.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RunningWaitingSlicePool extends SlicePool {
    private static Logger logger = LoggerFactory.getLogger(RunningWaitingSlicePool.class);

    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNING_WAITING && slicePool.add(slice);
    }
}
