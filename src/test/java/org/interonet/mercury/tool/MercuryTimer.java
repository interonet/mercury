package org.interonet.mercury.tool;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MercuryTimer {
    private int totalSecond;

    private List<TimerEntry> callBackList = new ArrayList<>();

    public MercuryTimer(int totalSeconds) {
        this.totalSecond = totalSeconds;
    }

    public void addListener(int delay, MercuryTimerCallBack callback) throws Exception {
        if (delay > totalSecond) throw new Exception("delay is later than total");
        callBackList.add(new TimerEntry(delay, callback));
    }

    public void start() throws InterruptedException {
        Instant start = Instant.now();
        while (Instant.now().isBefore(start.plusSeconds(totalSecond))) {
            for (TimerEntry timerEntry : callBackList) {
                if (timerEntry.notCalled && Instant.now().isAfter(start.plusSeconds(timerEntry.delay))) {
                    timerEntry.callBack.run();
                    timerEntry.notCalled = false;
                }
            }
            Thread.sleep(200);
        }


    }

    static class TimerEntry {
        public int delay;
        public MercuryTimerCallBack callBack;
        public boolean notCalled = true;

        public TimerEntry(int delay, MercuryTimerCallBack callBack) {
            this.delay = delay;
            this.callBack = callBack;
        }
    }
}

