package com.museum.controller.version;

import com.museum.generated.api.version.VersionApi;
import com.museum.generated.model.version.VersionResponse;
import com.museum.service.version.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VersionController implements VersionApi {

    private final VersionService versionService;

    @Override
    public ResponseEntity<VersionResponse> getVersion() {
        return versionService.getVersion();
    }
}
