package org.example.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SimpleCloudStorageUtils {
    private final Storage storage;

    public Map<String, String> getContent(String bucketName, List<String> fileNames) {
        Bucket bucket = storage.get(bucketName);
        List<Blob> blobs = bucket.get(fileNames);
        return blobs.stream()
                .collect(Collectors.toMap(Blob::getName, blob -> new String(blob.getContent())));
    }
}
