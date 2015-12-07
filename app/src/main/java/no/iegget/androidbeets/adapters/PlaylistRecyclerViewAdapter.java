package no.iegget.androidbeets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.List;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.content.PlaylistContent;
import no.iegget.androidbeets.fragments.PlaylistFragment;
import no.iegget.androidbeets.models.Playlist;
import no.iegget.androidbeets.models.Track;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Track} and makes a call to the
 * specified {@link no.iegget.androidbeets.fragments.PlaylistFragment.OnListFragmentInteractionListener}.
 */
public class PlaylistRecyclerViewAdapter
        extends ParallaxRecyclerAdapter<Track> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<Track> mValues;
    private final PlaylistFragment.OnListFragmentInteractionListener mListener;
    private PlaylistContent content;

    public PlaylistRecyclerViewAdapter(PlaylistFragment.OnListFragmentInteractionListener listener, Playlist playlist) {
        super(null);
        content = new PlaylistContent(this, playlist);
        mValues = content.items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, ParallaxRecyclerAdapter<Track> parallaxRecyclerAdapter, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<Track> parallaxRecyclerAdapter, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mArtistView.setText(mValues.get(position).getAlbumartist());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<Track> parallaxRecyclerAdapter) {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mArtistView;
        public Track mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.track_title);
            mArtistView = (TextView) view.findViewById(R.id.track_artist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
