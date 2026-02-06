package gov.cms.smart.utils.auth;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SalesforceTokenHelper {

    /**
     * Exchanges a signed JWT for a Salesforce Access Token.
     * @param signedJwt The encoded and signed JWT string.
     * @param loginUrl https://login.salesforce.com or https://test.salesforce.com
     * @return The access_token string.
     */
    public static String fetchToken(String signedJwt, String loginUrl) {
        
        Response response = RestAssured.given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .formParam("assertion", signedJwt)
                .post(loginUrl + "/services/oauth2/token");

        if (response.getStatusCode() == 200) {
            // jsonPath() parses the response body so we can grab the specific field
            return response.jsonPath().getString("access_token");
        } else {
            // If it fails, Salesforce returns the reason in the body (e.g., "invalid_grant")
            throw new RuntimeException("Salesforce Auth Failed! Status: " 
                    + response.getStatusCode() + " Body: " + response.getBody().asString());
        }
    }
}