package com.museum.service.version;

import com.museum.generated.model.version.VersionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {

    private final VersionHelper versionHelper;

    @Override
    public ResponseEntity<VersionResponse> getVersion() {
        return ResponseEntity.ok(versionHelper.getVersion());
    }
}
