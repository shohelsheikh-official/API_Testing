package qtriptest.APITests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.UUID;
import com.github.javafaker.Faker;



public class testCase_API_01 {

    @Test(groups = {"API"})
    public void API_01() {

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

        System.out.println("Register Response: "+registerResponse.asPrettyString());        
        registerResponse.then().assertThat().statusCode(201);


        RequestSpecification loginRequest = RestAssured.given();
        hm.remove("confirmpassword");

        loginRequest.contentType(ContentType.JSON);
        loginRequest.body(hm);

        Response loginResponse = loginRequest
                .post("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/login");

        System.out.println("Login Response: "+loginResponse.asPrettyString());           
        loginResponse.then().assertThat().statusCode(201);

        JsonPath jsonpath = loginResponse.jsonPath();
        Assert.assertTrue(jsonpath.get("success"));
    }

}
