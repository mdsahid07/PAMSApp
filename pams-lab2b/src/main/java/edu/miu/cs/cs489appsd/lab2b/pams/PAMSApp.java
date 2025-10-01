package edu.miu.cs.cs489appsd.lab2b.pams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.miu.cs.cs489appsd.lab2b.pams.model.Patient;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PAMSApp {
    public static void main(String[] args) throws Exception {
        List<Patient> patients = seedPatients();

        // Sort by age DESC (oldest first). If age ties, sort by lastName then firstName.
        patients.sort(
                Comparator.comparingInt(Patient::getAge).reversed()
                        .thenComparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Patient::getFirstName, String.CASE_INSENSITIVE_ORDER)
        );

        // Prepare output folder + mapper that understands Java time
        File outDir = new File("output");
        if (!outDir.exists()) Files.createDirectories(outDir.toPath());

        ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules() // picks up jsr310 for LocalDate
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outDir, "patients.json"), patients);

        System.out.println("Wrote JSON to " + new File(outDir, "patients.json").getPath());
    }

    // Sample data from the handout (missing fields left null as appropriate)
    private static List<Patient> seedPatients() {
        List<Patient> list = new ArrayList<>();
        list.add(new Patient(1L, "Daniel", "Agar", "(641) 123-0009", "dagar@m.as", "1 N Street",
                LocalDate.parse("1987-01-19")));
        list.add(new Patient(2L, "Ana", "Smith", null, "amsith@te.edu", null,
                LocalDate.parse("1948-12-05")));
        list.add(new Patient(3L, "Marcus", "Garvey", "(123) 292-0018", null, "4 East Ave",
                LocalDate.parse("2001-09-18")));
        list.add(new Patient(4L, "Jeff", "Goldbloom", "(999) 165-1192", "jgold@es.co.za", null,
                LocalDate.parse("1995-02-28")));
        list.add(new Patient(5L, "Mary", "Washington", null, null, "30 W Burlington",
                LocalDate.parse("1932-05-31")));
        return list;
    }
}
