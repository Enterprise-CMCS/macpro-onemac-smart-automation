package gov.cms.smart.utils.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class SalesforceAuth {

    /**
     * Get a Salesforce Access Token using JWT Bearer Flow.
     * Works for Developer Edition or Production orgs.
     */
    public static String getAccessToken(String consumerKey, String username, String loginUrl, String privateKeyPath) throws Exception {

        // 1️⃣ Load Private Key (PKCS#8)
        byte[] keyBytes;
        try {
            String keyPem = new String(Files.readAllBytes(Paths.get(privateKeyPath)))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            keyBytes = Base64.getDecoder().decode(keyPem);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read private key file: " + e.getMessage(), e);
        }

        PrivateKey privateKey;
        try {
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Invalid private key format. Make sure it is PKCS#8 (-----BEGIN PRIVATE KEY-----)", e);
        }

        // 2️⃣ Build JWT Claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(consumerKey)
                .subject(username)
                .audience(loginUrl)  // e.g., https://login.salesforce.com
                .expirationTime(new Date(System.currentTimeMillis() + 300_000)) // 5 mins
                .issueTime(new Date())
                .build();

        // 3️⃣ Sign the JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(new RSASSASigner(privateKey));
        String assertion = signedJWT.serialize();

        System.out.println("### DEBUG: JWT Assertion Generated:");
        System.out.println(assertion);

        // 4️⃣ POST JWT to Salesforce
        Response response = RestAssured.given()
                .formParam("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .formParam("assertion", assertion)
                .post(loginUrl + "/services/oauth2/token");

        // 5️⃣ Handle Response
        if (response.getStatusCode() == 200) {
            String accessToken = response.jsonPath().getString("access_token");
            System.out.println("### SUCCESS: Token Received!");
            return accessToken;
        } else {
            String errorBody = response.getBody().asString();
            System.err.println("### ERROR: Salesforce JWT Auth Failed!");
            System.err.println("HTTP Status: " + response.getStatusCode());
            System.err.println("Response Body: " + errorBody);

            if (errorBody.contains("invalid_client")) {
                throw new RuntimeException("Invalid client credentials. Most likely your private key does not match the public key uploaded in the Connected App, or the consumer key is incorrect.");
            } else if (errorBody.contains("invalid_grant")) {
                throw new RuntimeException("Invalid grant. Check that the user has approved the connected app and the username is correct.");
            } else {
                throw new RuntimeException("Salesforce Auth Failed. Status: " + response.getStatusCode() + " Body: " + errorBody);
            }
        }
    }

    // Example usage
    public static void main(String[] args) throws Exception {
        String instanceUrl = "https://orgfarm-1d597eb61f-dev-ed.develop.my.salesforce.com";
        String privateKeyPath = "C:\\Users\\57901\\salesforce-jwt\\private_key_pkcs8.pem";
        String consumerKey = "3MVG9dAEux2v1sLvGBZ4hGdDD3J1Iy0BKenp8xxyRDZh46_cRiqrx5Xo_BzBiFz683W3UE0lo0S2C3ZqkUE2.";
        String username = "amjad.ammar.e78c9bd56c7c@agentforce.com";
        String token = getAccessToken(
                consumerKey,
                username,
                "https://login.salesforce.com",
                privateKeyPath
        );
        String frontDoorUrl = instanceUrl
                + "/secur/frontdoor.jsp?sid=" + token
                + "&retURL=" + URLEncoder.encode("/lightning/n/devedapp__Welcome", StandardCharsets.UTF_8);

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\57901\\Desktop\\chromedriver\\chromedriver\\chromedriver.exe");
        WebDriver driver;
        ChromeOptions chromeOptions;
        chromeOptions = new ChromeOptions();
        // chromeOptions.addArguments("--headless=new");
        // chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.setBinary("C:\\Users\\57901\\Desktop\\chrome-win64\\chrome-win64\\chrome.exe");
        driver = new ChromeDriver(chromeOptions);
// Wait for the Lightning page to load
        // 1. Navigate to Frontdoor
        driver.get(frontDoorUrl);

      /*  Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("https://orgfarm-1d597eb61f-dev-ed.develop.my.salesforce.com/services/data/v56.0/limits");
        System.out.println(response.getBody().asString());*/

    }
}
