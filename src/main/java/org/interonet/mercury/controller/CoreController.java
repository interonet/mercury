package org.interonet.mercury.controller;

import org.interonet.mercury.domain.core.datetime.SliceDuration;
import org.interonet.mercury.export.ExportSlice;
import org.interonet.mercury.export.ExportSubmitSlice;
import org.interonet.mercury.service.APIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;


@RestController
@RequestMapping(value = "/core")
@EnableAutoConfiguration
public class CoreController {

    private static final Logger logger = LoggerFactory.getLogger(CoreController.class);

    @Autowired
    APIService apiService;


    @RequestMapping(value = "/timetable/{type}", method = RequestMethod.GET)
    public Map<Integer, TreeSet<SliceDuration>> getTimeTable(
            @PathVariable("type") String tableName,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getTimeTable(token, tableName);
    }

    @RequestMapping(value = "/slice_pool", method = RequestMethod.POST)
    public ExportSlice submitSlice(
            @RequestBody ExportSubmitSlice slice,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.submitSlice(token, slice);
    }

    @RequestMapping(value = "/slice_pool", method = RequestMethod.GET)
    public List<ExportSlice> getUserSlicePool(
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getUserSlicePool(token);
    }

    @RequestMapping(value = "/slice_pool/{sliceId}", method = RequestMethod.GET)
    public ExportSlice getSliceById(
            @PathVariable("sliceId") String sliceId,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getSlice(token, sliceId);
    }

    @RequestMapping(value = "/slice_pool/{sliceId}", method = RequestMethod.DELETE)
    public ExportSlice terminateSlice(
            @PathVariable("sliceId") String sliceId,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.tryToTerminateSlice(token, sliceId);
    }

}


