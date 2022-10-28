package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.example.configs.SimpleOptions;
import org.example.dto.MyUser;
import org.example.mappers.MapToMyUser;
import org.example.mappers.MapToString;
import org.example.services.SafeScanService;
import org.example.services.SimpleAvroService;
import org.example.services.SimpleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ApplicationS2 implements CommandLineRunner {
    private final SimpleOptions options;
    private final SimpleService simpleService;
    private final SimpleAvroService simpleAvroService;
    private final SafeScanService safeScanService;
    private final ObjectMapper objectMapper;

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
                .namespace(MyUser.class.getName())
                .fields()
                .requiredString("name")
                .requiredInt("age")
                .endRecord();
        List<MyUser> originalUsers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            originalUsers.add(new MyUser("User" + i, i));
        }

        simpleAvroService.createAvro(schema, options.getOutputPath(), originalUsers);

        simpleAvroService.readAvro(options.getOutputPath());

        simpleAvroService.writeJson(options.getOutputPath());

//        avro read/write by apache beam
        PipelineOptions pipelineOptions = PipelineOptionsFactory.create();
        pipelineOptions.setRunner(DirectRunner.class);
        Pipeline pipeline = Pipeline.create(pipelineOptions);
        PCollection<GenericRecord> read = pipeline.apply("Read", AvroIO.readGenericRecords(schema).from(options.getOutputPath()));
        PCollection<MyUser> transformToMyUser = read.apply("Transform to MyUser", ParDo.of(new MapToMyUser(objectMapper)));
        PCollection<String> transformToString = transformToMyUser.apply("Transfer to String", ParDo.of(new MapToString(objectMapper)));
        transformToString.apply("Save to file", TextIO.write()
                .to(options.getOutputPath() + "_result")
                .withSuffix(".json")
                .withNumShards(1));

        pipeline.run().waitUntilFinish();

//        safeScan files from GCP bucket
//        Map<String, Object> safeScanContent = safeScanService.parseContent(options.getBucketName(), options.getSafeScanFiles());
//        safeScanContent.forEach((k, v) -> log.debug("Key: {}, value: {}", k, v));
    }
}