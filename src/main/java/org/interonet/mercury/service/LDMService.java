package org.interonet.mercury.service;

import org.interonet.mercury.domain.ldm.LDMCalls;
import org.interonet.mercury.domain.ldm.call.LDMTaskCallFactory;
import org.interonet.mercury.domain.ldm.call.LDMTaskStartCall;
import org.interonet.mercury.domain.ldm.call.LDMTaskStopCall;
import org.interonet.mercury.domain.ldm.task.LDMStartTask;
import org.interonet.mercury.domain.ldm.task.LDMStopTask;
import org.interonet.mercury.domain.ldm.task.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Service
public class LDMService {
    Logger logger = LoggerFactory.getLogger(LDMService.class);
    private LDMCalls ldmCalls;
    private ExecutorService threadPool;

    public LDMService() {
    }

    public LDMService(URL url, int timeout, boolean debug) {
        threadPool = Executors.newCachedThreadPool(new LDMTaskCallFactory());
        ldmCalls = new LDMCalls(url, timeout, debug);
    }

    public FutureTask<LDMTaskReturn> submit(LDMStartTask ldmStartTask) {
        LDMTaskStartCall ldmTaskStartCall = new LDMTaskStartCall(ldmStartTask, ldmCalls);
        FutureTask<LDMTaskReturn> futureTask = new FutureTask<>(ldmTaskStartCall);
        threadPool.submit(futureTask);
        return futureTask;

    }

    public FutureTask<LDMTaskReturn> submit(LDMStopTask ldmStopTask) {
        LDMTaskStopCall ldmTaskStopCall = new LDMTaskStopCall(ldmStopTask, ldmCalls);
        FutureTask<LDMTaskReturn> futureTask = new FutureTask<>(ldmTaskStopCall);
        threadPool.submit(futureTask);
        return futureTask;
    }
}
