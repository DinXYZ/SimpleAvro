package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class SimpleAvroService {
    public void createAvro(Schema schema, String outputPath, List<MyUser> users) {
        try (OutputStream out = new FileOutputStream(outputPath);
             OutputStream outSchema = new FileOutputStream("mySchema.avsc");
             DataFileWriter<MyUser> dataFileWriter = new DataFileWriter<>(new ReflectDatumWriter<>(MyUser.class))) {

            log.debug("Schema: {}", schema.toString(true));
            outSchema.write(schema.toString().getBytes(StandardCharsets.UTF_8));

            dataFileWriter.create(schema, out);
            for (MyUser user : users) {
                log.debug("User for adding: {}", user);
                dataFileWriter.append(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readAvro(String inputPath) {
        try (DataFileReader<MyUser> dataFileReader = new DataFileReader<>(new File(inputPath), new ReflectDatumReader<>(MyUser.class))) {
            dataFileReader.iterator().forEachRemaining(user -> log.debug("User was restored: {}", user));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
