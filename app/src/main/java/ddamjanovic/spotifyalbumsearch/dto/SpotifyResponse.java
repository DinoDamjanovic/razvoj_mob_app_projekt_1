package ddamjanovic.spotifyalbumsearch.dto;

import java.io.Serializable;
import java.util.List;

public class SpotifyResponse implements Serializable {

    private Albums albums;

    public Albums getAlbums() {
        return albums;
    }

    public static class Albums implements Serializable {

        private List<AlbumResponse> items;

        public List<AlbumResponse> getItems() {
            return items;
        }
    }
}

