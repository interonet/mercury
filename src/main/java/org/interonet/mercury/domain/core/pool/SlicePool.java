package org.interonet.mercury.domain.core.pool;


import org.interonet.mercury.domain.core.Slice;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlicePool {
    protected List<Slice> slicePool;

    public SlicePool() {
        slicePool = new ArrayList<>();
    }

    synchronized public boolean submit(Slice slice) {
        return slicePool.add(slice);
    }

    synchronized public List<Slice> consumeAllSlice() {
        List<Slice> list = new ArrayList<>(slicePool);
        slicePool.clear();
        return list;
    }

    synchronized public List<Slice> consumeSliceBeginTimeBeforeThanNow() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = new ArrayList<>();
        for (Slice slice : slicePool) {
            if (slice.getBeginTime().isBefore(now))
                list.add(slice);
        }
        slicePool.removeAll(list);
        return list;
    }

    synchronized public List<Slice> consumeSliceBeginTimeBeforeThan(ZonedDateTime zonedDateTime) {
        List<Slice> list = new ArrayList<>();
        for (Slice slice : slicePool) {
            if (slice.getBeginTime().isBefore(zonedDateTime))
                list.add(slice);
        }
        slicePool.removeAll(list);
        return list;
    }

    synchronized public List<Slice> consumeSliceEndTimeBeforeNow() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = new ArrayList<>();
        for (Slice slice : slicePool) {
            if (slice.getEndTime().isBefore(now))
                list.add(slice);
        }
        slicePool.removeAll(list);
        return list;
    }

    synchronized public List<Slice> consumeSliceEndTimeBefore(ZonedDateTime zonedDateTime) {
        List<Slice> list = new ArrayList<>();
        for (Slice slice : slicePool) {
            if (slice.getEndTime().isBefore(zonedDateTime))
                list.add(slice);
        }
        slicePool.removeAll(list);
        return list;
    }

    synchronized public Slice consumeBySliceId(String sliceId) {
        for (Slice slice : slicePool) {
            if (slice.getId().equals(sliceId))
                return slice;
        }
        return null;
    }

    synchronized public List<Slice> getSlicePoolSnapShot() {
        return new ArrayList<>(slicePool);
    }

}
