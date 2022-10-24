package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        SimpleService simpleService = new SimpleService();
        for (int i = 0; i < 100000; i++) {
            simpleService.action();
        }

        SimpleAvroService simpleAvroService = new SimpleAvroService();
        Schema schema = SchemaBuilder.record("MyTestSchema")
                .namespace("org.example.MyUser")
                .fields()
                .requiredString("name")
                .requiredInt("age")
                .endRecord();
        String outputPath = "output.avro";
        List<MyUser> originalUsers = List.of(new MyUser("Vasya", 12), new MyUser("Petya", 23));

        simpleAvroService.createAvro(schema, outputPath, originalUsers);

        simpleAvroService.readAvro(outputPath);

    }
}