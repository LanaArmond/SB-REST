package br.com.javastudies.sbrest.file.exporter.contract;

import br.com.javastudies.sbrest.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

    Resource exportPerson(List<PersonDTO> people) throws Exception;

    Resource exportPerson(PersonDTO person) throws Exception;
}
