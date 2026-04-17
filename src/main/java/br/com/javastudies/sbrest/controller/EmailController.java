package br.com.javastudies.sbrest.controller;

import br.com.javastudies.sbrest.controller.docs.EmailControllerDocs;
import br.com.javastudies.sbrest.data.dto.request.EmailRequestDTO;
import br.com.javastudies.sbrest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        service.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("Email sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam("emailRequest") String emailRequest,
                                                          @RequestParam("attachment") MultipartFile attachment)
    {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("Email with attachment sent with success!", HttpStatus.OK);
    }
}
