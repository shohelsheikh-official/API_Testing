package qtriptest.APITests;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
public class testCase_API_02 {

    @Test(groups = {"API testing"})
    public void API_02(){

        List<Object> repsonseArray = new ArrayList<>();
        RequestSpecification serachRequest = RestAssured.given();

        String[] cities = {"bengaluru","goa","kolkata","singapore","malaysia","bangkok","new york","paris"};
        String city = getRandomElement(cities);
        System.out.println(city);

        Response searchResponse = serachRequest.get("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/cities?q="+city);

            String responseBody = searchResponse.asPrettyString();
            repsonseArray.add(responseBody);
           // System.out.println(repsonseArray.size());

            if(repsonseArray.size() == 1 && responseBody.contains("100+ Places") && searchResponse.getStatusCode() == 200){
                searchResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("app/src/test/resources/schema.json"))); 
            }


        


    }

    private String getRandomElement(String[] cities){
        Random ran = new Random();
        int index = ran.nextInt(cities.length);

        return cities[index];

    }





}
