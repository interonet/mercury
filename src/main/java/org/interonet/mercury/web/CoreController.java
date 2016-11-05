package org.interonet.mercury.web;

import org.interonet.mercury.domain.core.Slice;
import org.interonet.mercury.domain.core.UserSlice;
import org.interonet.mercury.domain.core.datetime.SliceDuration;
import org.interonet.mercury.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@RestController
@RequestMapping(value = "/core")
@EnableAutoConfiguration
public class CoreController {

    @Autowired
    APIService apiService;

    @RequestMapping(value = "/timetable/{type}", method = RequestMethod.GET)
    public Map<Integer, TreeSet<SliceDuration>> getTimeTable(
            @PathVariable("type") String tableName,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getTimeTable(token, tableName);
    }

    @RequestMapping(value = "/slice_pool", method = RequestMethod.POST)
    public UserSlice submitSlice(
            @RequestBody Slice slice,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.submitSlice(token, slice);
    }

    @RequestMapping(value = "/slice_pool", method = RequestMethod.GET)
    public List<UserSlice> getUserSlicePool(
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getUserSlicePool(token);
    }

    @RequestMapping(value = "/slice_pool/{sliceId}", method = RequestMethod.GET)
    public UserSlice submitSlice(
            @PathVariable("sliceId") String sliceId,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.getSlice(token, sliceId);
    }

    @RequestMapping(value = "/slice_pool/{sliceId}", method = RequestMethod.DELETE)
    public UserSlice terminateSlice(
            @PathVariable("sliceId") String sliceId,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        return apiService.tryToTerminateSlice(token, sliceId);
    }
}


