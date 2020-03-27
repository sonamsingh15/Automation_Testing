import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

public class SpotifyRestAssured {
    String token;
    String userId;;
    List<String> playlists=new ArrayList<>();
    List<String> tracklists=new ArrayList<>();


    @Before
    public void setUp() {
        token ="Bearer BQDUyjqTqEn8SuMKi1CcDirlIKgK666SdPA7QF3XKjpN6wsEMpBX_T0Bh_9jFIckAH9AQXzLFq3kc5tzpgKBdAUTBUdwjShHEbP48wL7ahvcYihwwWjTsHMplpApPOcV1gkVaysWuj0wX3IFRJ1zfpLGCxRdmhsVt7Sghlt9_5p2gUM2KhdqhfHfAVNTMgUFK7Ayxg37j_NQ4UCPkYVrkMOxZbPq3vL_QG2yDzETSYxn1xjf7PMQO2MJrq8Rkv6Z5taAuchVB78pH3pUqdE0Cw3513-8WA";
    }
   @Test
    public void spotify_RestAssured_AutomationTest() {
        Response respons = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/me");
        ResponseBody body = respons.getBody();
        JsonObject object = (JsonObject) new JsonParser().parse(body.prettyPrint());
        userId = String.valueOf(object.get("id"));
        userId=userId.replace("\"","");
        System.out.println(userId);
        respons.then().assertThat().statusCode(200);
    }

    @Test
    public void whenGivenToken_ShouldReturnUserId() {
        spotify_RestAssured_AutomationTest();
        Response responseForUser = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/"+userId);
        System.out.println(userId);
        ResponseBody body = responseForUser.getBody();
        JsonObject object = (JsonObject) new JsonParser().parse(body.prettyPrint());
        String type = String.valueOf(object.get("type"));
        System.out.println(type);
        responseForUser.then().assertThat().statusCode(200);
    }

    @Test
    public void givenUserID_GetToatalUserList() {
        Response responseUserList = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/"+userId+"/playlists");
        System.out.println(userId);
        String asString = responseUserList.asString();
        JsonPath jsonPath = new JsonPath(asString);
        int total = jsonPath.getInt("total");
        System.out.println("total==>" + total);
        responseUserList.prettyPrint();

    }
    //-------------------------------playlist---------------------------------------------------------------------------
    @Test
    public void spotify_RestAssured_playlist() {
        RestAssured.defaultParser= Parser.JSON;
        Response respons = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
              .get("https://api.spotify.com/v1/me/playlists");
        ResponseBody body = respons.getBody();
       List<Map<String,String>> playlistName=respons.jsonPath().getList("items");
        for (Map<String,String> playlist:playlistName) {
            System.out.println(playlist.get("id"));
            playlists.add(playlist.get("id"));
        }
    }
    @Test
    public void givenUserID_ShouldReturnTotalPlayList() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/me/playlists");
        String asString = response.asString();
        JsonPath jsonPath = new JsonPath(asString);
        int total = jsonPath.getInt("total");
        System.out.println("total==>" + total);
        response.prettyPrint();
    }
    @Test
    public void givenCodingLogin_WhenTokenIsCorrect_ShouldCreatePlayList() {
        spotify_RestAssured_AutomationTest();
        Response response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .body("{\"name\": \"New add Playlist 7\",\"description\": \"New playlist description\",\"public\": false" +
                   "}")
                .when()
                .post("https://api.spotify.com/v1/users/"+userId+"/playlists");
        response.prettyPrint();
    }

    //-----------------------------------Tracklist---------------------------------------------

    @Test
    public void givenPlaylistId_ShouldCheckTrackList() {
        spotify_RestAssured_playlist();
            Response respons = given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .when()
                    .get("https://api.spotify.com/v1/playlists/" + playlists.get(5) + "/tracks");
            ResponseBody body = respons.getBody();
         //  JsonObject object = (JsonObject) new JsonParser().parse(body.prettyPrint());
            respons.then().assertThat().statusCode(200);
        List<Map<String,String>> tracklistName=respons.jsonPath().getList("items");
        for (Map<String,String> tracklist:tracklistName)
        {
          System.out.println(tracklist.toString());
//            tracklists.add(tracklist.get("id"));
        }
        }


    @Test
    public void givenPlayList_ShouldReturnTotalTrackList() {
        givenPlaylistId_ShouldCheckTrackList();
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when().accept("application/json")
                .get("https://api.spotify.com/v1/playlists/"+tracklists.get(0)+"/tracks");
        String asString = response.asString();
        JsonPath jsonPath = new JsonPath(asString);

        Object trackId = response.path("trackId");
        System.out.println(trackId);
        response.prettyPrint();


    }
    //--------------------------delete track--------------------------------
    @Test
    public void givenTrackId_shoudDeleteThisTrack(){
        Response response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .body("{\"tracks\":[{\"uri\":\"spotify:track:2DB2zVP1LVu6jjyrvqD44z\",\"positions\":[0]}]}")
                .when()
                .delete("https://api.spotify.com/v1/playlists/"+ playlists +"/tracks").then()
                .extract().response();
        Object trackId = response.path("trackId");
        response.prettyPrint();
    }
}

