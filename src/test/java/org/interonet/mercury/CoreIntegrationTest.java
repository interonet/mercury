package org.interonet.mercury;

import org.interonet.mercury.domain.auth.Credential;
import org.interonet.mercury.domain.auth.Token;
import org.interonet.mercury.export.ExportSlice;
import org.interonet.mercury.export.ExportSubmitSlice;
import org.interonet.mercury.tool.MercuryTimer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoreIntegrationTest {

    MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
    private Token token;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createSlice() throws Exception {
        Credential credential = new Credential("admin", "admin");
        token = restTemplate.postForObject("/auth/token", credential, Token.class);

        header.add("Content-Type", "application/json");
        header.add("Authorization", token.getToken());

        ExportSubmitSlice slice = new ExportSubmitSlice();
        slice.setBeginTime(ZonedDateTime.now().plusSeconds(3));
        slice.setEndTime(ZonedDateTime.now().plusSeconds(6));

        HttpEntity<ExportSubmitSlice> request = new HttpEntity<>(slice, header);
        HttpEntity<ExportSlice> httpEntity = restTemplate.postForEntity("/core/slice_pool", request, ExportSlice.class);
        ExportSlice exportSlice = httpEntity.getBody();

        MercuryTimer mercuryTimer = new MercuryTimer(10);
        mercuryTimer.addListener(1, () -> {
            ExportSlice timeWaitingExportSlice = getSliceById(exportSlice.getId(), header);
            assertNotNull(timeWaitingExportSlice);
//            assertEquals(timeWaitingExportSlice.getStatus(), Slice.SliceStatus.TIME_WAITING);
        });

        mercuryTimer.addListener(5, () -> {
            ExportSlice runningExportSlice = getSliceById(exportSlice.getId(), header);
            assertNotNull(runningExportSlice);
//            assertEquals(runningExportSlice.getStatus(), Slice.SliceStatus.RUNNING);

        });

        mercuryTimer.addListener(9, () -> {
            ExportSlice terminatedExportSlice = getSliceById(exportSlice.getId(), header);
            assertNotNull(terminatedExportSlice);
//            assertEquals(terminatedExportSlice.getStatus(), Slice.SliceStatus.TERMINATED);
        });
        mercuryTimer.start();

    }

    public ExportSlice getSliceById(String sliceId, MultiValueMap<String, String> header) {
        String url = String.format("/core/slice_pool/%s", sliceId);
        HttpEntity requestEntity = new HttpEntity(header);
        HttpEntity<ExportSlice> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ExportSlice.class);
        ExportSlice exportSlice = responseEntity.getBody();
        return exportSlice;
    }
}
