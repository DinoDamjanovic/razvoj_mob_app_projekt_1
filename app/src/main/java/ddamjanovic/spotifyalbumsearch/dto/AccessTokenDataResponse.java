package ddamjanovic.spotifyalbumsearch.dto;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.time.Duration;

public class AccessTokenDataResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private int expiresIn;

    // Timestamp to record when the object was created
    private final Instant creationTime = Instant.now();

    public String getAccessToken() {
        return accessToken;
    }

     // This method returns the time left until the token expires in seconds.
    public long timeLeftUntilExpiration() {
        Instant expirationTime = creationTime.plusSeconds(expiresIn);
        Duration duration = Duration.between(Instant.now(), expirationTime);
        return duration.getSeconds();
    }

}
