package service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtilService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtilService.class);
    private long lastModificationRecordedTime = 0l;
    private static final String DEFAULT_DATA_FILE_NAME = "people.csv";

    public static Path getExternalCSVPath (String fileName) {
        Path path = Paths.get(System.getProperty("ratpackpush_csv_path") + java.io.File.separator + fileName);
        if ( !Files.exists(path) ) {
            LOGGER.debug("CSV source file not found at path: " + path);
        }
        return path;
    }

    public static long getFileModifiedTime(Path file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        return attr.lastModifiedTime().toMillis();
    }

    public static Path getDataSourceFile() {
        Path dataSourceFile = getExternalCSVPath(DEFAULT_DATA_FILE_NAME);

        if ( !Files.exists(dataSourceFile) ) {
            LOGGER.error("Missing data source file at: " + dataSourceFile.normalize().toString());
            System.exit(1);
        }

        return dataSourceFile;
    }

    /**
     * Checks if the data source file was modified since the last
     * modification recorded, if that's the case the content of the
     * file is converted from CSV to a JSON String and returned. The String
     * "skip" is returned in any other case.
     */
    public String getEventData() throws Exception {
        long fileModificationTime = getFileModifiedTime(getDataSourceFile());
        String result = "skip";

        if ( fileModificationTime != lastModificationRecordedTime ) {
            LOGGER.info("CSV source file modified on: " + fileModificationTime);
            result =  getCSVDataFileAsJSONString();
            lastModificationRecordedTime = fileModificationTime;
        }
        return result;
    }

    public static String getCSVDataFileAsJSONString() throws Exception {
        CsvSchema orderLineSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<User> orderLines = csvMapper.readerFor(User.class)
                .with(orderLineSchema)
                .readValues(getDataSourceFile().toFile());

        return new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(orderLines.readAll());
    }
}
