package com.museum.service.version;

import com.museum.generated.model.version.VersionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface VersionService {
    ResponseEntity<VersionResponse> getVersion();
}
