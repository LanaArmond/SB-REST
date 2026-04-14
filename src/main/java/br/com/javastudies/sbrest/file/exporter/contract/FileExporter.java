package br.com.javastudies.sbrest.file.exporter.contract;

import br.com.javastudies.sbrest.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
