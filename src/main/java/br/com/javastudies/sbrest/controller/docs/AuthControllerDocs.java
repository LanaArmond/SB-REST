package br.com.javastudies.sbrest.controller.docs;

import br.com.javastudies.sbrest.data.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates an user and returns a token")
    ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials);

    @Operation(summary = "Refreshes token for authenticated user and returns a token")
    ResponseEntity<?> refreshToken(String username, String refreshToken);


    AccountCredentialsDTO create(AccountCredentialsDTO credentials);


}
