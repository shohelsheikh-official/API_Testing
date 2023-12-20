package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.UUID;
import com.github.javafaker.Faker;

public class testCase_API_04 {

    @Test(groups = {"API"})
    public void API_04() {
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

        registerRequest.body(hm);
        Response registerResponse1 = registerRequest
                .post("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/register");
        System.out.println("Register Response: " + registerResponse1.asPrettyString());
        
        registerResponse1.then().assertThat().statusCode(400);
        Assert.assertTrue(registerResponse1.asPrettyString().contains("Email already exists"));

    }
}


