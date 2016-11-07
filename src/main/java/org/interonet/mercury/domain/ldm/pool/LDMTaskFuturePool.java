package org.interonet.mercury.domain.ldm.pool;

import org.interonet.mercury.domain.ldm.task.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class LDMTaskFuturePool {
    Logger logger = LoggerFactory.getLogger(LDMTaskFuturePool.class);
    private List<Future<LDMTaskReturn>> futureTaskList = new ArrayList<>();

    synchronized public boolean submit(Future<LDMTaskReturn> futureTask) {
        return futureTaskList.add(futureTask);
    }

    synchronized public List<Future<LDMTaskReturn>> consumeAllFinishedTask() {
        List<Future<LDMTaskReturn>> list = new ArrayList<>();
        for (Future<LDMTaskReturn> futureTask : futureTaskList) {
            if (futureTask.isDone())
                list.add(futureTask);
        }
        futureTaskList.removeAll(list);
        return list;
    }

    synchronized public int getPoolSize() {
        return futureTaskList.size();
    }
}
