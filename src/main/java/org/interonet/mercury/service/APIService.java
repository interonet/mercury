package org.interonet.mercury.service;

import org.interonet.mercury.domain.auth.User;
import org.interonet.mercury.domain.core.Slice;
import org.interonet.mercury.domain.core.datetime.SliceDuration;
import org.interonet.mercury.export.ExportSlice;
import org.interonet.mercury.export.ExportSubmitSlice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Service
public class APIService {
    private static final Logger logger = LoggerFactory.getLogger(APIService.class);

    private AuthService authService;
    private CoreService coreService;

    @Autowired
    public APIService(AuthService authService, CoreService coreService) {
        this.authService = authService;
        this.coreService = coreService;
    }

    public ExportSlice submitSlice(String authToken, ExportSubmitSlice exportSubmitSlice) throws Exception {
        if (!authService.isTokenExisted(authToken)) {
            logger.info("authentication failed");
            throw new Exception("authToken = [" + authToken + "], sliceStr = [" + exportSubmitSlice + "]");
        }
        Slice slice = new Slice(exportSubmitSlice);
        slice.setUsername(authService.getUserByToken(authToken).getUsername());
        slice.setStatus(Slice.SliceStatus.NEW);
        Slice submitSlice = coreService.submitSlice(slice);
        return new ExportSlice(submitSlice);
    }

    public List<ExportSlice> getUserSlicePool(String token) throws Exception {
        if (token == null) return null;
        if (!authService.isTokenExisted(token)) {
            logger.info("authentication failed");
            throw new Exception("authToken = [" + token + "]");
        }
        // TODO: 4/25/16 it should be some bug if the token refresh when happen here.
        User user = authService.getUserByToken(token);
        if (user == null) return null;
        List<Slice> slicePool = getUserSlicePool(user);
        if (slicePool == null) return null;

        List<ExportSlice> exportSlicePool = new ArrayList<>(slicePool.size());
        for (Slice slice : slicePool) {
            exportSlicePool.add(new ExportSlice(slice));
        }
        return exportSlicePool;
    }

    public ExportSlice getSlice(String token, String sliceId) throws Exception {
        logger.debug("token = [" + token + "], sliceId = [" + sliceId + "]");
        if (token == null) return null;
        if (!authService.isTokenExisted(token)) {
            logger.info("authentication failed");
            throw new Exception("authToken = [" + token + "], sliceId = [" + sliceId + "]");
        }

        try {
            User user = authService.getUserByToken(token);
            Slice slice = coreService.getSlice(user, sliceId);
            if (slice == null) return null;
            ExportSlice exportSlice = new ExportSlice(slice);
            return exportSlice;
        } catch (Exception e) {
            logger.error("getSlice", e);
            return null;
        }
    }

    private List<Slice> getUserSlicePool(User user) {
        String username = user.getUsername();
        List<Slice> userSliceList = new ArrayList<>();
        for (Slice slice : coreService.getSlicePool("SlicePool")) {
            if (slice.getUsername().equals(username)) {
                userSliceList.add(slice);
            }
        }
        return userSliceList;
    }

    public Map<Integer, TreeSet<SliceDuration>> getTimeTable(String token, String timeName) throws Exception {
        if (!authService.isTokenExisted(token)) {
            logger.info("authentication failed");
            throw new Exception("authToken = [" + token + "], timeName = [" + timeName + "]");
        }

        switch (timeName) {
            case "switch":
                return coreService.getSwitchTimeTable();
            case "vm":
                return coreService.getVMTimeTable();
            default:
                return null;
        }
    }

    public ExportSlice tryToTerminateSlice(String token, String sliceId) throws Exception {
        if (!authService.isTokenExisted(token)) {
            logger.info("authentication failed");
            throw new Exception("authToken = [" + token + "], sliceId = [" + sliceId + "]");
        }
        Slice deletedSlice = coreService.tryToTerminateSlice(sliceId);
        return new ExportSlice(deletedSlice);
    }
}


