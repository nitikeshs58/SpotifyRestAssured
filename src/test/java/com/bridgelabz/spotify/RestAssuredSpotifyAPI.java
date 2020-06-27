package com.bridgelabz.spotify;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RestAssuredSpotifyAPI {
    String userID = "";
    String tokenValue = "";
    String firstPlaylistId = "";
    String secondPlaylistId = "";
    String[] trackId;
    String expectedUserId="g93w2xw0r603qa4ixw8xqttdx";
    String expectedEmail="nitikeshs58@gmail.com";
    String expectedFirstPlaylistId="61t7PAj3FvUrL7eIjHkTWv";
    String expectedSecondPlaylistId="0J91KR5FgsG87QVxvSH12x";
    int expectedStatusCode=201;



    @BeforeMethod
    public void setUp() {
        tokenValue = "Bearer BQAS0JzVy87KiWdG7JciEz9MiZvbUIR52a8eOkUvibERl0EKdBbxsp5YhyayMm5Rr3owA0eZQXR_cBfakRl08Z82RX7Al7vmQZ2rcWnl3AQvfdBLNLqQxiwoSFy3KRHULypCdRBzl242yNtsPsXdqUpOYR13VanGUTc1Fy5OxfXfDLyFypq54l7TC9LyzB2eqzv141gFpJfQA98qVTTiSq2mwcrp45RbcFGFXCRYxcF2yzBuQUGs4QcRG8gG9kJuDztMaBI_pz_bhhfG-tZdga6lZ273IJ3X";
    }

    // User will get UserId as a response
    @Test(priority = 1)
    public void getUserId_OfUser_OfSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        userID = response.path("id");
        Assert.assertEquals(expectedUserId,userID);
    }

    // To get User profile of Spotify App
    @Test(priority = 2)
    public void getUsersProfile_ofStotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        String userBody = response.getBody().asString();
        String actualEmail=response.path("email").toString();
        Assert.assertEquals(expectedEmail,actualEmail);
    }

    /*
    // To create playlist in Spotify App
    @Test (priority = 3)
    public void createPlaylist_inSpotifyApp()
    {
        JSONObject playlistBody=new JSONObject();
        playlistBody.put("name","New HeartBreaking Songs");
        playlistBody.put("description","PainFul");
        playlistBody.put("public",false);

        Response response=RestAssured.given()
            .accept("application/json")
            .contentType("application/json")
            .header("Authorization", tokenValue)
            .body(playlistBody.toJSONString())
            .pathParam("user_id",userID)
            .when()
            .post("https://api.spotify.com/v1/users/{user_id}/playlists");
        int actualResponseCode=response.getStatusCode();
        Assert.assertEquals(expectedStatusCode,actualResponseCode);
        }
    */
//To get the playlist id's in Spotify App
    @Test(priority = 4)
    public void addTrack_InPlaylist_ofSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("user_id", userID)
                .when()
                .get("https://api.spotify.com/v1/users/{user_id}/playlists");
        int totalNumberOfPlaylist = response.path("total");
        System.out.println("Total number of playlist :" + totalNumberOfPlaylist);
        firstPlaylistId = response.path("items[0].id");
        secondPlaylistId = response.path("items[1].id");
        Assert.assertEquals(expectedFirstPlaylistId,firstPlaylistId);
        Assert.assertEquals(expectedSecondPlaylistId,secondPlaylistId);
    }

    //Get a Playlist's Items
    @Test(priority = 5)
    public void getPlaylistItems_FromPlaylist_ofSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", firstPlaylistId)
                .when()
                .get("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        int totalTracks = response.path("total");
        trackId = new String[totalTracks];
        for (int i = 0; i < trackId.length; i++) {
            // get the track of uri
            trackId[i] = response.path("items[" + i + "].track.uri");
        }
        for (int i = 0; i < trackId.length; i++) {
            //printing track id's
            System.out.println("Number " + i + " Track Uri :" + trackId[i]);
        }
    }

    // 	Add Items to a Playlist
    @Test(priority = 6)
    public void addItems_inPlaylist_ofSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", firstPlaylistId)
                .body("{\"uris\": [\"" + "spotify:track:5Pgq1Gfeth2CuUhyCXwlfC" + "\"]}")
                .when()
                .post("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        int statusCode=response.getStatusCode();
        response.prettyPrint();
        Assert.assertEquals(expectedStatusCode,statusCode);
    }

    //Remove Items from a Playlist
    @Test(priority = 7)
    public void deleteAnItem_fromPlaylist_ofSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", firstPlaylistId)
                .body("{\"uris\": [\"" + "spotify:track:5Pgq1Gfeth2CuUhyCXwlfC" + "\"]}")
                .when()
                .delete("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        int statusCode=response.getStatusCode();
        response.prettyPrint();
        Assert.assertEquals(200,statusCode);
    }

    //Change a Playlist's Details
    @Test(priority = 8)
    public void changeDetails_ofPlaylist_ofSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", firstPlaylistId)
                .body("{\"name\": \"HeartBreaking Songs \",\"description\": \"Lots of pain\",\"public\": false}")
                .when()
                .put("https://api.spotify.com/v1/playlists/{playlist_id}");
        int statusCode=response.getStatusCode();
        response.prettyPrint();
        Assert.assertEquals(200,statusCode);
    }
}