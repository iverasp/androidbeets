package no.iegget.androidbeets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.List;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.content.AlbumContent;
import no.iegget.androidbeets.fragments.AlbumFragment;
import no.iegget.androidbeets.fragments.AlbumFragment.OnListFragmentInteractionListener;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.Track;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Track} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AlbumRecyclerViewAdapter
        extends ParallaxRecyclerAdapter<Track> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<Track> mValues;
    private final AlbumFragment.OnListFragmentInteractionListener mListener;
    private AlbumContent content;

    public AlbumRecyclerViewAdapter(AlbumFragment.OnListFragmentInteractionListener listener, Album album) {
        super(null);
        content = new AlbumContent(this, album);
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
        holder.mNumberView.setText(String.format("%-6d", mValues.get(position).getTrack()));

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
        public final TextView mNumberView;
        public Track mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.track_title);
            mNumberView = (TextView) view.findViewById(R.id.track_number);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
