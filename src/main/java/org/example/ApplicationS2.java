package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.example.configs.SimpleOptions;
import org.example.dto.MyUser;
import org.example.services.SafeScanService;
import org.example.services.SimpleAvroService;
import org.example.services.SimpleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ApplicationS2 implements CommandLineRunner {
    private final SimpleOptions options;
    private final SimpleService simpleService;
    private final SimpleAvroService simpleAvroService;
    private final SafeScanService safeScanService;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationS2.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        logs check
        for (int i = 0; i < 1; i++) {
            simpleService.action();
        }

//        avro/schema create, write and read
        Schema schema = SchemaBuilder.record("MyTestSchema")
                .namespace("org.example.dto.MyUser")
                .fields()
                .requiredString("name")
                .requiredInt("age")
                .endRecord();
        List<MyUser> originalUsers = List.of(new MyUser("Vasya", 12), new MyUser("Petya", 23));

        simpleAvroService.createAvro(schema, options.getOutputPath(), originalUsers);

        simpleAvroService.readAvro(options.getOutputPath());

        simpleAvroService.writeJson(options.getOutputPath());

//        safeScan files from GCP bucket
        Map<String, Object> safeScanContent = safeScanService.parseContent(options.getBucketName(), options.getSafeScanFiles());
        safeScanContent.forEach((k, v) -> log.debug("Key: {}, value: {}", k, v));
    }
}