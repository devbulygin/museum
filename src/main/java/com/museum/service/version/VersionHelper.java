package com.museum.service.version;

import com.museum.config.version.VersionInfo;
import com.museum.generated.model.version.Gate;
import com.museum.generated.model.version.Requisite;
import com.museum.generated.model.version.VersionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class VersionHelper {
    private final VersionInfo versionInfo;

    public VersionResponse getVersion() {
        VersionResponse response = new VersionResponse();
        response.setGate(getGateResponce());
        response.setSystem(getSystemResponce());
        response.setRequisite(getRequisiteResponse());
        return response;
    }

    private Gate getGateResponce() {
        VersionInfo.GateInfo gateInfo = versionInfo.getGateInfo();
        Gate gate = new Gate();
        gate.setName(gateInfo.getName());
        gate.setVersion(gateInfo.getVersion());
        gate.setMajor(gateInfo.getMajor());
        gate.setMinor(gateInfo.getMinor());
        gate.setRelease(gateInfo.getRelease());
        gate.setBuild(gateInfo.getBuild());
        gate.setDtLicenceFinish(gateInfo.getDtLicenceFinish() != null ?
                gateInfo.getDtLicenceFinish().atStartOfDay().atOffset(ZoneOffset.UTC) : null);
        return gate;
    }

    private com.museum.generated.model.version.System getSystemResponce() {
        VersionInfo.SystemInfo systemConfig = versionInfo.getSystemInfo();

        com.museum.generated.model.version.System systemResponse =  new com.museum.generated.model.version.System();
        systemResponse.setName(systemConfig.getName());
        systemResponse.setVersion(systemConfig.getVersion());
        systemResponse.setMajor(systemConfig.getMajor());
        systemResponse.setMinor(systemConfig.getMinor());
        systemResponse.setRelease(systemConfig.getRelease());
        systemResponse.setBuild(systemConfig.getBuild());
        systemResponse.setDtLicenceFinish(systemConfig.getDtLicenceFinish() != null ?
                systemConfig.getDtLicenceFinish().atStartOfDay().atOffset(ZoneOffset.UTC) : null);
        return systemResponse;
    }

    private Requisite getRequisiteResponse() {
        VersionInfo.RequisiteInfo requisiteInfo = this.versionInfo.getRequisiteInfo();

        Requisite requisiteResponse = new Requisite();
        requisiteResponse.setName(requisiteInfo.getName());
        requisiteResponse.setCity(requisiteInfo.getCity());
        requisiteResponse.setAddress(requisiteInfo.getAddress());
        requisiteResponse.setPhone1(requisiteInfo.getPhone1());
        requisiteResponse.setFax(requisiteInfo.getFax());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        requisiteResponse.setDtBegin(requisiteInfo.getDtBegin() != null ?
                requisiteInfo.getDtBegin().format(timeFormatter) : null);
        requisiteResponse.setDtEnd(requisiteInfo.getDtEnd() != null ?
                requisiteInfo.getDtEnd().format(timeFormatter) : null);
        return requisiteResponse;
    }
}