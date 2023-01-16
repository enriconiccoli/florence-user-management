package it.florence.assignment.user_management.utils;

import it.florence.assignment.user_management.exceptions.CSVParsingException;
import it.florence.assignment.user_management.model.UserDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVHelper {

    private CSVHelper(){}

    public static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<UserDTO> csvToUserDTO(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            return csvParser.getRecords().stream().map(CSVHelper::mapToDTO).toList();


        } catch (IOException e) {
            throw new CSVParsingException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    private static UserDTO mapToDTO(CSVRecord csvRecord){

        UserDTO userDTO = new UserDTO();

        try {
            userDTO.setId(Long.valueOf(csvRecord.get("id")));
        }
        catch (IllegalArgumentException e){
            userDTO.setId(null);
        }

        userDTO.setName(csvRecord.get("name"));
        userDTO.setSurname(csvRecord.get("surname"));
        userDTO.setMail(csvRecord.get("mail"));
        userDTO.setAddress(csvRecord.get("address"));

        return userDTO;
    }
}
