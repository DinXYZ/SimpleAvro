package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.utils.SimpleCloudStorageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeScanService {
    private final SimpleCloudStorageUtils simpleCloudStorageUtils;

    public Map<String, Object> parseContent(String bucketName, List<String> safeScans) {
        Map<String, String> content = simpleCloudStorageUtils.getContent(bucketName, safeScans);
        log.debug(this.getClass().getSimpleName());
        return content.entrySet().stream()
                .filter(e -> StringUtils.isNotBlank(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> mapToObject(e.getValue())));
    }

    private Object mapToObject(String value) {
        return value;
    }
}
