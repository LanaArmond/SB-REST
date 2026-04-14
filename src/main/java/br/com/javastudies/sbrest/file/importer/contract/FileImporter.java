package br.com.javastudies.sbrest.file.importer.contract;

import br.com.javastudies.sbrest.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
