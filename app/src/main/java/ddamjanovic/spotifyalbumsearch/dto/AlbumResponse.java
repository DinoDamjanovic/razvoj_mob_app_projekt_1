package ddamjanovic.spotifyalbumsearch.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AlbumResponse implements Serializable {

    private List<Artist> artists;
    private String name;
    @SerializedName("release_date")
    private String releaseDate;
    private List<Image> images;
    private String id;
    private String href;
    @SerializedName("total_tracks")
    private Integer totalTracks;

    public List<Artist> getArtists() {
        return artists;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public Integer getTotalTracks() {
        return totalTracks;
    }

    public static class Artist implements Serializable {

        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    public static class Image implements Serializable {
        private int height;
        private String url;
        private int width;

        public int getHeight() {
            return height;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }
    }
}
