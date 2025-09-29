package com.congresoSpring.control.exchange;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvReaderService {


    public List<String[]> readCsv(String fileName) {
        List<String[]> data = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Dividir la línea por comas (,) para obtener las columnas
                String[] columns = line.split(",");
                data.add(columns);
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo CSV: " + fileName, e);
        }

        return data;
    }

    public List<Conferencia> leerCsv(InputStream is) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            List<Conferencia> conferencias = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvParser) {
                Conferencia conferencia = new Conferencia();
                conferencia.setDia(Integer.parseInt(record.get("dia")));
                conferencia.setFecha(Long.parseLong(record.get("dia")));
                conferencia.setHoraInicio(Long.parseLong(record.get("horaInicio")));
                conferencia.setHoraFin(Long.parseLong(record.get("horaFin")));
                conferencia.setDuracion(Long.parseLong(record.get("duracion")));
                conferencia.setSede(record.get("sede"));
                conferencia.setTipo(record.get("tipo"));
                conferencia.setTitulo(record.get("titulo"));
                conferencia.setPonente(record.get("ponente"));
                conferencia.setPuntos(Integer.parseInt(record.get("puntos")));
                conferencias.add(conferencia);
            }
            return conferencias;

        } catch (IOException e) {
            throw new RuntimeException("CSV data is failed to parse: " + e.getMessage());
        }

    }
    private final Map<Long, Integer> conferencePointsMap = new HashMap<>();

    public Integer getPuntos(Long conferenciaId) {
        return conferencePointsMap.getOrDefault(conferenciaId, 0);
        }

        // 🚨 FALTA LEER CSV DE FREEBIES
    // 🚨 FALTA LEER CSV DE EVENTOS ESPECIALS


}
