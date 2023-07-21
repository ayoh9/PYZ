package com.example.payazatest.controller;

import com.example.payazatest.model.Tenant;
import com.example.payazatest.pojo.ClientRequest;
import com.example.payazatest.pojo.ClientResponse;
import com.example.payazatest.repository.TenantRepository;
import com.example.payazatest.service.VirtualAccountService;
import com.example.payazatest.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/virtualAccount/apis/v1")
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;
    private TenantRepository tenantRepository;
    private static final Logger logger = LogManager.getLogger(VirtualAccountController.class);
    ResponseEntity<?> responseEntity = null;

    public VirtualAccountController(VirtualAccountService virtualAccountService,
                                    TenantRepository tenantRepository){
        this.virtualAccountService = virtualAccountService;
        this.tenantRepository = tenantRepository;
    }

    @PostMapping(value = "/virtualAccount", produces = "Application/JSON")
    public ResponseEntity<?> generateVirtualAccount(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody ClientRequest clientRequest) {
        // Validate Client Credentials
        String clientUsername = null;
        String clientPassword = null;
        final String authorization = authHeader;
        ClientResponse serverResponse = new ClientResponse();

        String tenant = clientRequest.getRequestHeader().getClientId();

        logger.info("Basic Auth into /virtualAccount is: " + authorization);
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            logger.info("Valid auth type: : ");
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            clientUsername = values[0];
            clientPassword = values[1];

            if ((clientUsername == null ? "" : clientUsername).equals("")
                    || (clientPassword == null ? "" : clientPassword).equals("")) {
                // An error occurred
                return responseEntity = new ResponseEntity<>("Error: Basic Auth Credentials not provided.",
                        HttpStatus.FORBIDDEN);
            }
            logger.info("Client Username is: " + clientUsername);
            logger.info("Client Password is: " + clientPassword);

            Optional<Tenant> clientDetails = tenantRepository
                    .findById(clientRequest.getRequestHeader().getClientId());
            if (!clientDetails.isPresent()) {
                serverResponse.getResponseDetails().setResponseCode(Utility.INVALID_MERCHANT);
                serverResponse.getResponseDetails().setResponseMessage("Error: Invalid Client");
                return new ResponseEntity<>(serverResponse, HttpStatus.OK);
            }

            logger.info("The tenant id is: "+tenant);
            //Check the provider assigned to this tenant
            if("BANKA".equalsIgnoreCase(clientDetails.get().getTenantAssignedProvider())){
                serverResponse = virtualAccountService.generateProvidusBankVirtualAccount(clientRequest)
                responseEntity = new ResponseEntity<>(serverResponse, HttpStatus.OK);
            }
            return responseEntity;
        }
    }
}
