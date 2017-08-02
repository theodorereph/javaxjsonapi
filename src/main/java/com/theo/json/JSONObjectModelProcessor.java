package com.theo.json;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import com.theo.json.bean.Employee;

public class JSONObjectModelProcessor {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<Employee> createListFromJSON(JsonArray jsonArray) {
        List<Employee> employeeList = new ArrayList<>();

        for (JsonValue employeeJsonValue : jsonArray) {
            if (employeeJsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject jsonObject = (JsonObject) employeeJsonValue;
                Employee employee = new Employee();
                employee.setId(jsonObject.getInt("employeeId"));
                employee.setFirstName(jsonObject.getString("firstName"));
                employee.setLastName(jsonObject.getString("lastName"));

                String birthDateString = jsonObject.getString("birthDate");
                LocalDate localDate = LocalDate.parse(birthDateString, formatter);
                employee.setBirthDate(localDate);

                JsonArray phoneNumbersJsonArray = jsonObject.getJsonArray("phoneNumberList");

                if (phoneNumbersJsonArray != null) {
                    for (JsonValue phoneNumberJsonValue : phoneNumbersJsonArray) {
                        employee.addPhoneNumber(phoneNumberJsonValue.toString());
                    }
                }

                employeeList.add(employee);
            }
        }

        return employeeList;
    }

    public void readFile() throws Exception{
        InputStream inputStream = getClass().getResourceAsStream("/employee.json");
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        JsonReader jsonReader = Json.createReader(reader);
        JsonArray jsonArray = jsonReader.readArray();
        List<Employee> employeeList = createListFromJSON(jsonArray);
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
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Employee employee : employeeList) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("employeeId", employee.getId());
            objectBuilder.add("firstName", employee.getFirstName());
            objectBuilder.add("lastName", employee.getLastName());

            String birthDateString = formatter.format(employee.getBirthDate());
            objectBuilder.add("birthDate", birthDateString);


            JsonArrayBuilder phoneNumberListArrayBuilder = Json.createArrayBuilder();
            for (String phoneNumber : employee.getPhoneNumberList()) {
                phoneNumberListArrayBuilder.add(phoneNumber);
            }

            objectBuilder.add("phoneNumberList", phoneNumberListArrayBuilder.build());
            arrayBuilder.add(objectBuilder);
        }

        JsonArray jsonArray = arrayBuilder.build();
        OutputStream outputStream = new FileOutputStream("write.json");
        JsonWriter jsonWriter = Json.createWriter(outputStream);
        jsonWriter.writeArray(jsonArray);
        jsonWriter.close();
        outputStream.close();
    }

    public static void main(String... args) throws Exception{
        JSONObjectModelProcessor reader = new JSONObjectModelProcessor();
        reader.readFile();
        reader.createFile();
    }

}
