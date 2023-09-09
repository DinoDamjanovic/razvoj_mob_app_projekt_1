package ddamjanovic.spotifyalbumsearch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ddamjanovic.spotifyalbumsearch.dto.AlbumResponse;

public class AlbumDetailsActivity extends AppCompatActivity {
    private ImageView ivAlbumCover;
    private TextView tvAlbumName;
    private TextView tvReleaseDate;
    private TextView tvTotalTracks;
    private TextView tvFirstArtistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        // Initialize components
        ivAlbumCover = findViewById(R.id.ivAlbumCover);
        tvAlbumName = findViewById(R.id.tvAlbumName);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvFirstArtistName = findViewById(R.id.tvFirstArtistName);
        tvTotalTracks = findViewById(R.id.tvTotalTracks);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> finish());

        // Retrieve the album data
        AlbumResponse albumResponse = (AlbumResponse) getIntent().getSerializableExtra("album");

        populateAlbumDetails(albumResponse);
    }

    private void populateAlbumDetails(AlbumResponse albumResponse) {
        if (albumResponse != null) {
            tvAlbumName.setText(albumResponse.getName());
            tvReleaseDate.setText(formatReleaseDate(albumResponse.getReleaseDate()));
            tvTotalTracks.setText(String.format(Locale.getDefault(), "%d", albumResponse.getTotalTracks()));

            if (albumResponse.getArtists() != null && !albumResponse.getArtists().isEmpty()) {
                tvFirstArtistName.setText(albumResponse.getArtists().get(0).getName());
            }

            if (albumResponse.getImages() != null && !albumResponse.getImages().isEmpty()) {
                Picasso.with(this)
                        .load(albumResponse.getImages().get(0).getUrl())
                        .into(ivAlbumCover);
            }
        }
    }

    private String formatReleaseDate(String rawDate) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);

        Date date;
        try {
            date = originalFormat.parse(rawDate);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate; // Return the original date string if parsing fails
        }
    }

}
