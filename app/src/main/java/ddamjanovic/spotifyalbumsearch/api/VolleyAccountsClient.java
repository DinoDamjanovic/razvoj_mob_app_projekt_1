package ddamjanovic.spotifyalbumsearch.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VolleyAccountsClient {

    private static final String BASE_URL = "https://accounts.spotify.com";

    public static void refreshToken(Context context, String grantType, String clientId, String clientSecret, final VolleyCallback callback) {
        String url = BASE_URL + "/api/token";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.onSuccess(response),
                error -> callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", grantType);
                params.put("client_id", clientId);
                params.put("client_secret", clientSecret);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
