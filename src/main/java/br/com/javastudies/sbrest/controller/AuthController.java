package br.com.javastudies.sbrest.controller;

import br.com.javastudies.sbrest.controller.docs.AuthControllerDocs;
import br.com.javastudies.sbrest.data.dto.security.AccountCredentialsDTO;
import br.com.javastudies.sbrest.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint!")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    
    @Autowired
    AuthService service;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token  = service.signIn(credentials);
        if (token == null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!"); }
        // return ResponseEntity.ok().body(token);
        return token;
    }



    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (paramsAreValid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token  = service.refreshToken(username, refreshToken);
        if (token == null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!"); }
        // return ResponseEntity.ok().body(token);
        return token;
    }



    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null || StringUtils.isBlank(credentials.getPassword()) || StringUtils.isBlank(credentials.getUsername());
    }



    private static boolean paramsAreValid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    @PostMapping(value = "/createUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials) { return service.create(credentials); }
}
