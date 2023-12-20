package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import com.github.javafaker.Faker;

public class testCase_API_03 {

@Test(groups = {"API testing"})
    public void API_03() {
        Faker f = new Faker();
        String name = f.name().firstName() + "@gmail.com";

        RequestSpecification registerRequest = RestAssured.given();

        HashMap<Object, Object> hm = new HashMap<>();
        hm.put("email", name);
        hm.put("password", "test123");
        hm.put("confirmpassword", "test123");


        registerRequest.contentType(ContentType.JSON);
        registerRequest.body(hm);


        Response registerResponse = registerRequest
                .post("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/register");

        System.out.println("Register Response: " + registerResponse.asPrettyString());
        registerResponse.then().assertThat().statusCode(201);


        RequestSpecification loginRequest = RestAssured.given();
        hm.remove("confirmpassword");

        loginRequest.contentType(ContentType.JSON);
        loginRequest.body(hm);

        Response loginResponse = loginRequest
                .post("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/login");

        String loginResponseBody = loginResponse.asPrettyString();

        System.out.println("Login Response: " + loginResponse.asPrettyString());
        JsonPath jsonPath = new JsonPath(loginResponseBody);

        String token = jsonPath.get("data.token");
        String id = jsonPath.get("data.id");
        loginResponse.then().assertThat().statusCode(201);

        JsonPath jsonpath = loginResponse.jsonPath();
        Assert.assertTrue(jsonpath.get("success"));

        RequestSpecification makeReservation = RestAssured.given();
        makeReservation.contentType(ContentType.JSON).header("Authorization", "Bearer " + token);
        makeReservation.body("{\"userId\":\"" + id
                + "\",\"name\":\"testuser\",\"date\":\"2024-15-09\",\"person\":\"1\",\"adventure\":\"2447910730\"}");
        Response makeReservationResponse = makeReservation.post(
                "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/reservations/new");
        makeReservationResponse.then().assertThat().statusCode(200);
        String makeReservationBody = makeReservationResponse.asPrettyString();
        System.out.println(makeReservationBody);

        JsonPath jPath = makeReservationResponse.jsonPath();
        Assert.assertTrue(jPath.get("success"));


        RequestSpecification confirmReservation = RestAssured.given().header("Content-Type", "application/json")
        .header("Authorization", "Bearer " +token);
        
        Response confirmReservationResponse = confirmReservation.get("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/reservations/?id="+id+"");
        String confirmReservationResponseBody = confirmReservationResponse.asPrettyString();
             JsonPath jP = new JsonPath(confirmReservationResponseBody);
             String reservationCode = jP.getString("_id");
        System.out.println(reservationCode);

    }
}
