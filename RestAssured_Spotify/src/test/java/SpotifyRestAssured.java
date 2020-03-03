import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;

public class SpotifyRestAssured {
    String token;
    String userId;
    String playlistName;
    String tracklist;


    @Before
    public void setUp() {
        token = "Bearer BQABAdlvpSc2AreWFDdwSoEgwo3R0Ns9zfJcT3YbgyVlHvsDJcdkAHJjzn1xFbqLmLu-W8OwERDI5PcS7bBjKnpLCKGGmSmMp5WdQxqi1r5S0Z9rMW57TD_uXBg2YZ6u_xnT3pX9Uig35rDwq3CIuPopdHBV6IfE5D-aTrjl9-0Yt9mdCfprHLW0YO1omudR6XKhJKV7rLu-kz8LWKd3-2UaJe3aekYnhNobbBGj_7PLKXfaPWs2KMLFWZciK9Fduu6sk4IPcdxLZn5SSqsUWTOBaUismw";
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
        System.out.println(userId);
        respons.then().assertThat().statusCode(200);
    }

    @Test
    public void whenGivenToken_ShouldReturnUserId() {
        Response responseForUser = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/"+userId);
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
        String asString = responseUserList.asString();
        JsonPath jsonPath = new JsonPath(asString);
        int total = jsonPath.getInt("total");
        System.out.println("total==>" + total);
        responseUserList.prettyPrint();

    }
    //-------------------------------playlist---------------------------------------------------------------------------
    @Test
    public void spotify_RestAssured_playlist() {
        Response respons = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
              .get("https://api.spotify.com/v1/me/playlists")
               .then()
                .extract()
                .path("id");
        System.out.println();

    }

//        ResponseBody body = respons.getBody();
//       JsonObject object = (JsonObject) new JsonParser().parse(body.prettyPrint());
//        String playName = String.valueOf(object.get("name"));
//        System.out.println(playName);
//        respons.then().assertThat().statusCode(200);
 //   }
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
        Response response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .body("{\"name\": \"New add Playlist 1\",\"description\": \"New playlist description\",\"public\": false" +
                   "}")
                .when()
                .post("https://api.spotify.com/v1/users/"+userId+"/playlists");
        System.out.println(userId);
        response.prettyPrint();
    }

    //-----------------------------------Tracklist---------------------------------------------

    @Test
    public void givenPlaylistId_ShouldCheckTrackList() {
        Response respons = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/playlists/"+ playlistName +"/tracks");
        ResponseBody body = respons.getBody();
        JsonObject object = (JsonObject) new JsonParser().parse(body.prettyPrint());
        tracklist = String.valueOf(object.get("id"));
        System.out.println(tracklist);
        respons.then().assertThat().statusCode(200);
    }

    @Test
    public void givenPlayList_ShouldReturnTotalTrackList() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", token)
                .when().accept("application/json")
                .get("https://api.spotify.com/v1/playlists/"+tracklist+"/tracks");
        String asString = response.asString();
        JsonPath jsonPath = new JsonPath(asString);
        int total = jsonPath.getInt("total");
        System.out.println("total==>" + total);
        response.prettyPrint();
        Object trackId = response.path("trackId");

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
                .delete("https://api.spotify.com/v1/playlists/"+ playlistName +"/tracks").then()
                .extract().response();
        Object trackId = response.path("trackId");
        response.prettyPrint();
    }
}
