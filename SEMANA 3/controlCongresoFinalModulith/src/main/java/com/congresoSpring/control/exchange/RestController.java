package com.congresoSpring.control.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private CsvReaderService csvReaderService;

    @GetMapping("/read-csv")
    public List<String[]> readCsvConferencias() {
        return csvReaderService.readCsv("conferencias.csv"); // Nombre del archivo en resources
    }
}
