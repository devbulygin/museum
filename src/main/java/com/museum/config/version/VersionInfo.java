package com.museum.config.version;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ConfigurationProperties(prefix = "version")
public class VersionInfo {

    private GateInfo gateInfo = new GateInfo();
    private SystemInfo systemInfo = new SystemInfo();
    private RequisiteInfo requisiteInfo = new RequisiteInfo();

    @Data
    public static class GateInfo {
        private String name;
        private String version;
        private int major;
        private int minor;
        private int release;
        private int build;
        private LocalDate dtLicenceFinish;
    }

    @Data
    public static class SystemInfo {
        private String name;
        private String version;
        private int major;
        private int minor;
        private int release;
        private int build;
        private LocalDate dtLicenceFinish;
    }

    @Data
    public static class RequisiteInfo {
        private String name;
        private String city;
        private String address;
        private String phone1;
        private String fax;
        private LocalTime dtBegin;
        private LocalTime dtEnd;
    }
}
