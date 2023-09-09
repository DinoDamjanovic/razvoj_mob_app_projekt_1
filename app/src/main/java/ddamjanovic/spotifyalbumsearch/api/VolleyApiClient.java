package ddamjanovic.spotifyalbumsearch.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.HashMap;
import java.util.Map;

public class VolleyApiClient {

    private static final String BASE_URL = "https://api.spotify.com/v1/";

    public static void searchAlbums(Context context, String accessToken, String searchTerm, final VolleyCallback callback) {
        String url = BASE_URL + "search?q=artist:" + searchTerm + "&type=album&limit=15";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        response -> callback.onSuccess(response.toString()),
                        error -> callback.onFailure(error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}


