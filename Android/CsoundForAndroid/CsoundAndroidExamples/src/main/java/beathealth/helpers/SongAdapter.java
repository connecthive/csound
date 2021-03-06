package beathealth.helpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.csounds.examples.R;

import java.util.ArrayList;
import java.util.List;

import beathealth.api.IAudioFinishedCallback;
import beathealth.data.Song;

/**
 * @author pfister@connecthive.com
 * for sattaxlr - Montpellier
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements Filterable {
    private static IAudioFinishedCallback owner = null;
    private List<Song> mSongs_;
    private List<Song> mFilter_;

    private ValueFilter valueFilter;

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence filter) {
            FilterResults results = new FilterResults();
            if (filter != null && filter.length() > 0) {
                ArrayList<Song> filtered = new ArrayList<Song>();
                for (int i = 0; i < mFilter_.size(); i++) {
                    if ((mFilter_.get(i).getTitlez().toUpperCase())
                            .contains(filter.toString().toUpperCase())) {
                        Song bean = new Song(mFilter_.get(i));
                        filtered.add(bean);
                    }
                }
                results.count = filtered.size();
                results.values = filtered;
            } else {

                //TODO if filter empty
                results.count = mFilter_.size();
                results.values = mFilter_;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSongs_ = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvsongtitle;
        private final TextView tvsongartist;


        private Song currentSong;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tvsongtitle = ((TextView) itemView.findViewById(R.id.songtitle));
            tvsongartist = ((TextView) itemView.findViewById(R.id.songartist));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    owner.selectSong(currentSong);
                }
            });
        }

        public void display(Song song) {
            currentSong = song;

            tvsongtitle.setText(song.getTitlez());
            tvsongartist.setText(song.getArtistz());

        }
    }

    public SongAdapter(IAudioFinishedCallback owner, List<Song> songs) {
        this.owner = owner;
        mSongs_ = songs;
        mFilter_ = new ArrayList<>();
        mFilter_.addAll(songs);
    }

    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.song_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song s = mSongs_.get(position);
        holder.display(s);
    }

    @Override
    public int getItemCount() {
        return mSongs_.size();
    }
}
