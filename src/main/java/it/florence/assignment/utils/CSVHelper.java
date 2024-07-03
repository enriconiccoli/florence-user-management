package it.florence.assignment.utils;

import it.florence.assignment.model.UserDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVHelper {

    private CSVHelper(){}


    public static List<UserDTO> csvToUserDTO(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            return csvParser.getRecords().stream().map(CSVHelper::mapToDTO).toList();


        } catch (IOException e) {
            throw new WebApplicationException("Fail to parse CSV file: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static UserDTO mapToDTO(CSVRecord csvRecord){

        UserDTO userDTO = new UserDTO();

        try {

            try {
                userDTO.setId(Long.valueOf(csvRecord.get("id")));
            } catch (NumberFormatException e){
                userDTO.setId(null);
            }

            userDTO.setName(csvRecord.get("name"));
            userDTO.setSurname(csvRecord.get("surname"));
            userDTO.setMail(csvRecord.get("mail"));
            userDTO.setAddress(csvRecord.get("address"));
        } catch (IllegalArgumentException e){
            throw new WebApplicationException("Incorrect CSV file: " + e.getMessage(), Response.Status.BAD_REQUEST);
        }

        return userDTO;
    }
}
