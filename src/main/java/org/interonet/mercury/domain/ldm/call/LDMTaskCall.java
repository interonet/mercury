package org.interonet.mercury.domain.ldm.call;


import org.interonet.mercury.domain.ldm.LDMCalls;

public abstract class LDMTaskCall {
    public LDMCalls ldmCalls;

    public LDMTaskCall(LDMCalls ldmCalls) {
        this.ldmCalls = ldmCalls;
    }
}
