package ddamjanovic.spotifyalbumsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import ddamjanovic.spotifyalbumsearch.adapter.AlbumsAdapter;
import ddamjanovic.spotifyalbumsearch.api.VolleyAccountsClient;
import ddamjanovic.spotifyalbumsearch.api.VolleyApiClient;
import ddamjanovic.spotifyalbumsearch.api.VolleyCallback;
import ddamjanovic.spotifyalbumsearch.dto.AccessTokenDataResponse;
import ddamjanovic.spotifyalbumsearch.dto.AlbumResponse;
import ddamjanovic.spotifyalbumsearch.dto.SpotifyResponse;
import ddamjanovic.spotifyalbumsearch.repository.AccessTokenRepository;


public class MainActivity extends AppCompatActivity implements AlbumsAdapter.ItemClickInterface {

    private AlbumsAdapter albumsAdapter;
    private AccessTokenDataResponse accessTokenData;

    private EditText searchBar;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private final int DEBOUNCE_TIME = 600;  // Milliseconds

    private final TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            handler.removeCallbacks(runnable); // Remove the previous search call
            runnable = () -> searchAlbumsOnSpotify(s.toString()); // Assign a new search call
            handler.postDelayed(runnable, DEBOUNCE_TIME); // Add a delay before the search
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView albumsRecyclerView = findViewById(R.id.album_list);
        albumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        albumsAdapter = new AlbumsAdapter(this, this);
        albumsRecyclerView.setAdapter(albumsAdapter);

        searchBar = findViewById(R.id.search_bar);

        searchBar.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            return source.toString().contains("\n") ? "" : null;
        }});

        searchBar.addTextChangedListener(searchTextWatcher);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        accessTokenData = AccessTokenRepository.getInstance().getAccessTokenData();
        if (Objects.isNull(accessTokenData) || accessTokenData.timeLeftUntilExpiration() <= 60) {
            refreshTokenAndRetry(null);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    private void refreshTokenAndRetry(String searchTerm) {
        VolleyAccountsClient.refreshToken(
                getApplicationContext(),
                "client_credentials",
                getString(R.string.client_id),
                getString(R.string.client_secret),
                new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        AccessTokenDataResponse accessTokenDataResponse = new Gson().fromJson(result, AccessTokenDataResponse.class);
                        if (accessTokenDataResponse != null) {
                            AccessTokenRepository.getInstance().setAccessTokenData(accessTokenDataResponse);
                            accessTokenData = accessTokenDataResponse;

                            if (Objects.nonNull(searchTerm)) {
                                searchAlbumsOnSpotify(searchTerm);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("Error", error);
                    }
                }
        );
    }

    private void searchAlbumsOnSpotify(String searchTerm) {
        // If searchTerm is null or empty, clear the album list
        if (Objects.isNull(searchTerm) || searchTerm.isEmpty()) {
            albumsAdapter.setAlbums(null);
            return;
        }

        // If searchTerm is less than 2 characters, don't execute the search
        if (searchTerm.length() < 2) {
            return;
        }

        String encodedSearchTerm;
        try {
            encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;  // If encoding fails for some reason, return without doing the search
        }

        Log.i("ACCESS_TOKEN_TIMER", Long.toString(accessTokenData.timeLeftUntilExpiration()));

        if (Objects.isNull(accessTokenData) || accessTokenData.timeLeftUntilExpiration() <= 60) {
            refreshTokenAndRetry(searchTerm);
            return;
        }

        VolleyApiClient.searchAlbums(
                getApplicationContext(),
                accessTokenData.getAccessToken(),
                encodedSearchTerm,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        SpotifyResponse spotifyResponse = new Gson().fromJson(result, SpotifyResponse.class);
                        if (spotifyResponse != null) {
                            albumsAdapter.setAlbums(spotifyResponse.getAlbums().getItems());
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("Error", error);
                    }
                }
        );
    }

    @Override
    public void onItemClick(AlbumResponse albumResponse) {
        Intent intent = new Intent(this, AlbumDetailsActivity.class);
        intent.putExtra("album", albumResponse);
        startActivity(intent);
    }

}

