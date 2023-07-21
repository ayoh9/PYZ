package com.example.payazatest.service;

import com.example.payazatest.pojo.ClientRequest;
import com.example.payazatest.pojo.ClientResponse;
import com.example.payazatest.repository.TenantRepository;
import com.example.payazatest.repository.TransactionLogRepository;
import com.example.payazatest.utility.Utility;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class VirtualAccountService {

    private TenantRepository tenantRepository;
    private TransactionLogRepository transactionLogRepository;
    private static final Logger logger = LogManager.getLogger(VirtualAccountService.class);

    public VirtualAccountService(TenantRepository tenantRepository,
                                 TransactionLogRepository transactionLogRepository){
        this.transactionLogRepository = transactionLogRepository;
        this.tenantRepository = tenantRepository;
    }

    @Value("${providus.base.url}")
    String providusBaseUrl;

    @Value("${providus.dynamic.account.number.url}")
    String providusDynamicAccountNumberUrl;

    @Value("${providus.get.transaction.details}")
    String providusGetTransactionDetailsUrl;

    @Value("${providus.reserved.account.number.url}")
    String providusReservedAccountNumberUrl;

    @Value("${providus.update.account.name.url}")
    String providusUpdateAccountNameUrl;

    @Value("${providus.blacklist.account.number.url}")
    String providusBlacklistAccountNumberUrl;

    @Value("${providus.client.id}")
    String providusClientId;

    @Value("${providus.client.secret}")
    String providusClientSecret;

    @Value("${providus.client.xauth}")
    String providusClientXauth;

    @Value("${monnify.login.url}")
    String monnifyLogin;

    @Value("${monnify.api.key}")
    String monnifyAPIKey;

    @Value("${monnify.secret.key}")
    String monnifySecretKey;

    @Value("${monnify.init.url}")
    String monnifyInitAPI;

    @Value("${monnify.dynamic.account.url}")
    String monnifyDynamicAPI;

    @Value("${monnify.status.url}")
    String monnifyTransactionDetailsURL;
    
    public ClientResponse generateProvidusBankVirtualAccount(ClientRequest serverRequest) {
        ClientResponse ClientResponse = new ClientResponse();
        JSONObject json = new JSONObject();

        logger.info("The request from the Controller class is: " + serverRequest.toString());

        try {
            json = new JSONObject();

            json.put("account_name", serverRequest.getCustomerName() == null ? "" : serverRequest.getCustomerName());

            String url = providusBaseUrl + providusDynamicAccountNumberUrl;
            String httpResp = null;

            CloseableHttpClient httpClient = Utility.getMyHttpClient(url);

            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
                    .setConnectionRequestTimeout(60000).build();

            httpPost.setConfig(requestConfig);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("X-Auth-Signature", getAuth());
            httpPost.setHeader("Client-Id", providusClientId);

            try {
                httpPost.setEntity(new StringEntity(json.toString()));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ClientResponse = new ClientResponse();
                ClientResponse.getResponseDetails().setResponseCode("09x");
                return ClientResponse;
            }
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ClientResponse = new ClientResponse();
                ClientResponse.getResponseDetails().setResponseCode("09x");
                return ClientResponse;
            }
            HttpEntity entity = response.getEntity();
            try {
                httpResp = EntityUtils.toString(entity).trim();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ClientResponse = new ClientResponse();
                ClientResponse.getResponseDetails().setResponseCode("09x");
                return ClientResponse;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ClientResponse = new ClientResponse();
                ClientResponse.getResponseDetails().setResponseCode("09x");
                return ClientResponse;
            }
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ClientResponse = new ClientResponse();
                ClientResponse.getResponseDetails().setResponseCode("09x");
                return ClientResponse;
            }

            logger.info("response is: " + httpResp);
            json = new JSONObject(httpResp);

            logger.info("JSON response is: " + json.toString());

            if (json != null) {
                String responseCode = "";
                String responseMessage = "";
                if (json.has("responseCode")) {
                    responseCode = json.getString("responseCode");
                }
                if (json.has("responseCode")) {
                    responseMessage = json.getString("responseCode");
                }
                logger.info("The response code returned is:" + responseCode);
                if (responseCode.equals("00")) {
                    ClientResponse = new ClientResponse();
                    ClientResponse.setAccountName(json.getString("accountName"));
                    ClientResponse.setAccountNumber(json.getString("accountNumber"));

                }

            }
        } catch (NonUniqueResultException | NoResultException | DataAccessException ex) {
            logger.info("Exception in ex -->", ex.toString());
            ex.printStackTrace();
        } catch (Exception ex1) {
            logger.info("Exception in ex1-->", ex1.toString());
            ex1.printStackTrace();
        }

        return ClientResponse;
    }
}
