package ddamjanovic.spotifyalbumsearch.repository;

import ddamjanovic.spotifyalbumsearch.dto.AccessTokenDataResponse;

public class AccessTokenRepository {

    private static AccessTokenRepository instance;
    private AccessTokenDataResponse accessTokenData;

    private AccessTokenRepository() {}

    public static synchronized AccessTokenRepository getInstance() {
        if (instance == null) {
            instance = new AccessTokenRepository();
        }
        return instance;
    }

    public AccessTokenDataResponse getAccessTokenData() {
        return accessTokenData;
    }

    public void setAccessTokenData(AccessTokenDataResponse accessTokenData) {
        this.accessTokenData = accessTokenData;
    }
}

