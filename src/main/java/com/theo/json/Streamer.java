package com.theo.json;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

import com.theo.json.bean.Employee;

public class Streamer {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void readFile() throws Exception{
        InputStream inputStream = getClass().getResourceAsStream("/employee.json");
        JsonParser jsonParser = Json.createParser(inputStream);
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = null;

        while(jsonParser.hasNext()) {
            JsonParser.Event event = jsonParser.next();
            if (event == JsonParser.Event.START_OBJECT) {
                employee = new Employee();
                employeeList.add(employee);
            } else if (event == JsonParser.Event.KEY_NAME) {
                String keyName = jsonParser.getString();

                switch(keyName) {
                    case "id": {
                        jsonParser.next();
                        employee.setId(jsonParser.getInt());
                        break;
                    }
                    case "firstName": {
                        jsonParser.next();
                        employee.setFirstName(jsonParser.getString());
                        break;
                    }
                    case "lastName": {
                        jsonParser.next();
                        employee.setLastName(jsonParser.getString());
                        break;
                    }
                    case "birthDate": {
                        jsonParser.next();
                        String birthDateString = jsonParser.getString();
                        LocalDate localDate = LocalDate.parse(birthDateString, formatter);
                        employee.setBirthDate(localDate);
                    }
                    case "phoneNumberList" : {
                        JsonParser.Event arrayEvent = jsonParser.next();
                        List<String> phoneNumberList = new ArrayList<>();
                        JsonParser.Event evented = jsonParser.next();

                        if (evented == JsonParser.Event.START_ARRAY) {
                            evented = jsonParser.next();

                            while(evented == JsonParser.Event.VALUE_STRING) {
                                String phoneNumber = jsonParser.getString();
                                phoneNumberList.add(phoneNumber);
                                evented = jsonParser.next();
                            }
                        }

                        employee.setPhoneNumberList(phoneNumberList);
                    }
                }
            }
        }
        System.out.println(employeeList);
    }

    private List<Employee> createList() {
        List<Employee> employeeList = new ArrayList<>();

        List<String> phoneNumberList = new ArrayList<>();
        phoneNumberList.add("215-350-9980");
        phoneNumberList.add("215-345-8943");
        Employee employee = new Employee(103, "Moocow", "Jones", LocalDate.of(1982, 03, 14));
        employee.setPhoneNumberList(phoneNumberList);
        employeeList.add(employee);

        employee = new Employee(104, "Oinky", "McCow", LocalDate.of(1957, 05, 03));
        employeeList.add(employee);

        return employeeList;
    }

    public void createFile() throws Exception {
        List<Employee> employeeList = createList();
        OutputStream outputStream = new FileOutputStream("streamed-write.json");
        JsonGenerator jsonGenerator = Json.createGenerator(outputStream);


        jsonGenerator.writeStartArray();

        for (Employee employee : employeeList) {
            String birthDateString = formatter.format(employee.getBirthDate());

            jsonGenerator.writeStartObject();
            jsonGenerator.write("id", employee.getId());
            jsonGenerator.write("firstName", employee.getFirstName());
            jsonGenerator.write("lastName", employee.getLastName());
            jsonGenerator.write("birthDate", birthDateString);
            jsonGenerator.writeStartArray("phoneNumberList");

            for (String phoneNumber : employee.getPhoneNumberList()) {
                jsonGenerator.write(phoneNumber);
            }

            jsonGenerator.writeEnd();
            jsonGenerator.writeEnd();
        }

        jsonGenerator.writeEnd();
        jsonGenerator.close();
        outputStream.close();
    }

    public static void main(String... args) throws Exception{
        Streamer streamer = new Streamer();
        streamer.readFile();
        streamer.createFile();
    }
}
