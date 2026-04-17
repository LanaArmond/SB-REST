package br.com.javastudies.sbrest.file.exporter.factory;

import br.com.javastudies.sbrest.file.exporter.MediaTypes;
import br.com.javastudies.sbrest.file.exporter.contract.PersonExporter;
import br.com.javastudies.sbrest.file.exporter.impl.CsvExporter;
import br.com.javastudies.sbrest.file.exporter.impl.PdfExporter;
import br.com.javastudies.sbrest.file.exporter.impl.XlsxExporter;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public PersonExporter getExporter(String acceptHeader) throws Exception {

        // accept application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            // return new XlsxExporter();
            return context.getBean(XlsxExporter.class);

        // accept text/csv
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            // return new CsvExporter();
            return context.getBean(CsvExporter.class);

        // PDFs
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            // return new PdfExporter();
            return context.getBean(PdfExporter.class);

        } else {
            throw new BadRequestException("Invalid file format!");
        }
    }




}
