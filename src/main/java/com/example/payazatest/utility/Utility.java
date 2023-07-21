package com.example.payazatest.utility;

import com.coralpay.providers.monnify.model.DateDifference;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import com.pagatech.collect.core.Collect;

@Component
public class Utility {
	public static final String INVALID_MERCHANT = "03";
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	 
	    return builder
	            .setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000))
	            .build();
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Calendar.getInstance(TimeZone.getTimeZone("GMT+1")).getTimeInMillis());
	}
	
	public static CloseableHttpClient getHttpClient(String url) {
		System.out.println("Entering getHttpClient");
		CloseableHttpClient httpclient = null;
		SSLConnectionSocketFactory sslsf = null;
		
		if (url.toLowerCase().startsWith("https")) {
			final SSLContextBuilder builder = new SSLContextBuilder();
			try {
				builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				sslsf = new SSLConnectionSocketFactory(builder.build(),
						new NoopHostnameVerifier());
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} else {

			httpclient = HttpClients.createDefault();
		}
		System.out.println("Exiting getHttpClient..."+httpclient.toString());
		return httpclient;
	}
	
	public static CloseableHttpClient getMyHttpClient(String url) {
        if (url.toLowerCase().startsWith("https")) {
            try {
                javax.net.ssl.SSLContext sslContext = new org.apache.http.ssl.SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                return HttpClients.custom()
                        .setSslcontext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
						.evictIdleConnections(300, TimeUnit.SECONDS)
                        .build();
            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                
            }
        } else {
            return HttpClientBuilder.create().build();
        }
        return null;
    }
	
	public static String generateModuleValue(String input)
    {
		logger.info("The input into module value is: "+input);
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
  
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
  
            // return the HashText
            return hashtext;
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static LocalDate findPrevDay(LocalDate localdate)
    {
        return localdate.minusDays(1);
    }
	
	public static String getCredentials(String apiKey, String apiSecret) {
		String originalInput = apiKey + ":" + apiSecret;
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		return encodedString;
	}
	
	public static String encodeReference(String reference) {
		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(reference, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            
        }
		return encodedString;
	}



		private static final String HMAC_SHA512 = "HmacSHA512";

		private static String toHexString(byte[] bytes) {
			Formatter formatter = new Formatter();
			for (byte b : bytes) {
				formatter.format("%02x", b);
			}
			return formatter.toString();
		}

		public static String calculateHMAC512TransactionHash(String data, String merchantClientSecret)
				throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
		{
			SecretKeySpec secretKeySpec = new SecretKeySpec(merchantClientSecret.getBytes(), HMAC_SHA512);
			Mac mac = Mac.getInstance(HMAC_SHA512);
			mac.init(secretKeySpec);
			return toHexString(mac.doFinal(data.getBytes()));
		}

	public static String getSHA512(String input) {
		logger.info("The input into getSHA512 is: "+input);
		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes());
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}

		public static void main(String[] args) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
			Random rand = new Random();
			int num = rand.nextInt(9000000) + 1000000;
			System.out.println("int is: "+num);

		}

	/**
	 * Generates an MD5 hash of a plain text
	 *
	 * @param plainText - the plain text to hash
	 * @return - returns the MD5 hashed text
	 */
	public static String generateMD5Hash(String plainText) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] bytes = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));

			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < bytes.length; ++i) {
				stringBuffer.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return stringBuffer.toString(); // get the hash
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateAndCompareGTHash(String payLoad, String secret, String squadSignature)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		String result = "";
		String HMAC_SHA512 = "HmacSHA512";
		boolean signatureCompare = false;

		byte [] byteKey = secret.getBytes("UTF-8");
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
		Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);
		sha512_HMAC.init(keySpec);
		byte [] mac_data = sha512_HMAC.
				doFinal(payLoad.toString().getBytes("UTF-8"));
		result = String.format("%040x", new BigInteger(1, mac_data));
		logger.info("The calculated signature is: " + result);
		/*if(result.equals(squadSignature)) {
			signatureCompare = true;
		}else{
			signatureCompare = false;
		}*/
		return result;
	}

}
