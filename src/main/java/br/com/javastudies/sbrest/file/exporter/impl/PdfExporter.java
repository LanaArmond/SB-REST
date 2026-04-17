package br.com.javastudies.sbrest.file.exporter.impl;

import br.com.javastudies.sbrest.data.dto.PersonDTO;
import br.com.javastudies.sbrest.file.exporter.contract.PersonExporter;
import br.com.javastudies.sbrest.service.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    private QRCodeService service;

    @Override
    public Resource exportPerson(List<PersonDTO> people) throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/templates/People.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        Map<String, Object> params = new HashMap<>();
        //params.put("title", "People Report");

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {

        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/Person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("Template file not found: /templates/Person.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/Books.jrxml");
        if (subReportStream == null) {
            throw new RuntimeException("Template file not found: /templates/Books.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // Gerar QR Code
        InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));
        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

        String path = getClass().getResource("/templates/Books.jasper").getPath();

        Map<String, Object> params = new HashMap<>();
        params.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        params.put("BOOK_SUB_REPORT", subReport);
        params.put("SUB_REPORT_DIR", path);
        params.put("QR_CODEIMAGE", qrCodeStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, params, mainDataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

}
