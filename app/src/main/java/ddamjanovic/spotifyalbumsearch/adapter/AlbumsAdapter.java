package ddamjanovic.spotifyalbumsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ddamjanovic.spotifyalbumsearch.R;
import ddamjanovic.spotifyalbumsearch.dto.AlbumResponse;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.Row> {

    private List<AlbumResponse> albumResponses = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final ItemClickInterface itemClickInterface;

    public AlbumsAdapter(Context context, ItemClickInterface itemClickInterface) {
        this.layoutInflater = LayoutInflater.from(context);
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public Row onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_row, parent, false);
        return new Row(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Row holder, int position) {
        AlbumResponse albumResponse = albumResponses.get(position);
        loadImage(holder.ivImage, albumResponse);
        holder.tvName.setText(albumResponse.getName());
    }

    private void loadImage(ImageView imageView, AlbumResponse albumResponse) {
        if (albumResponse.getImages() == null || albumResponse.getImages().isEmpty()) {
            imageView.setImageResource(0);
            return;
        }

        String imageUrl = albumResponse.getImages().get(0).getUrl();
        if (albumResponse.getImages().size() > 1) {
            imageUrl = albumResponse.getImages().get(1).getUrl();
        }

        Picasso.with(layoutInflater.getContext())
                .load(imageUrl)
                .fit()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return albumResponses.size();
    }

    public void setAlbums(List<AlbumResponse> albumResponses) {
        this.albumResponses = albumResponses == null ? new ArrayList<>() : albumResponses;
        notifyDataSetChanged();
    }

    public class Row extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivImage;
        private final TextView tvName;

        public Row(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && itemClickInterface != null) {
                itemClickInterface.onItemClick(albumResponses.get(position));
            }
        }
    }

    public interface ItemClickInterface {
        void onItemClick(AlbumResponse albumResponse);
    }
}

