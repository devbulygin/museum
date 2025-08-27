package com.museum.controller.version;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.museum.generated.model.version.Gate;
import com.museum.generated.model.version.Requisite;
import com.museum.generated.model.version.System;
import com.museum.generated.model.version.VersionResponse;
import com.museum.service.version.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VersionController.class)
@AutoConfigureMockMvc(addFilters = false)
class VersionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private VersionService versionService;

  @Test
  void getVersion() throws Exception {
    VersionResponse expected = createVersionResponse();
    when(versionService.getVersion()).thenReturn(ResponseEntity.ok(expected));

    mockMvc
        .perform(get("/status").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expected)));
  }

  private VersionResponse createVersionResponse() {
    VersionResponse versionResponse = new VersionResponse();
    versionResponse.setGate(createGate());
    versionResponse.setRequisite(createRequisite());
    versionResponse.setSystem(createSystem());
    return versionResponse;
  }

  private Gate createGate() {
    Gate gate = new Gate();
    gate.setName("TLMuseumGate");
    gate.setVersion("7.0.6.1");
    gate.setMajor(7);
    gate.setMinor(0);
    gate.setRelease(6);
    gate.setBuild(1);
    gate.setDtLicenceFinish(OffsetDateTime.parse("2030-01-01T00:00:00Z"));
    return gate;
  }

  private Requisite createRequisite() {
    Requisite requisite = new Requisite();
    requisite.setName(
        "Государственное автономное учреждение культуры "
                + "Московской области «Государственный мемориальный музыкальный музей-заповедник П.И.Чайковского»");
    requisite.setCity("Москва");
    requisite.setAddress("Нежинская 5");
    requisite.setPhone1("+7(495)1234567");
    requisite.setFax("+7(495)1234568");
    requisite.setDtBegin("09:00:00");
    requisite.setDtEnd("20:00:00");
    return requisite;
  }

  private System createSystem() {
    System system = new System();
    system.setName("TLMuseumGate");
    system.setVersion("6.10.9.19");
    system.setMajor(6);
    system.setMinor(10);
    system.setRelease(9);
    system.setBuild(19);
    system.setDtLicenceFinish(OffsetDateTime.parse("2023-08-05T00:00:00Z"));
    return system;
  }
}
