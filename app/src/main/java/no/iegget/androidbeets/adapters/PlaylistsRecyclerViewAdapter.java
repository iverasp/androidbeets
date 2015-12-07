package no.iegget.androidbeets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.content.PlaylistsContent;
import no.iegget.androidbeets.fragments.PlaylistsFragment;
import no.iegget.androidbeets.models.Playlist;


/**
 * {@link RecyclerView.Adapter} that can display a {@link no.iegget.androidbeets.models.Playlist} and makes a call to the
 * specified {@link no.iegget.androidbeets.fragments.PlaylistsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PlaylistsRecyclerViewAdapter
        extends RecyclerView.Adapter<PlaylistsRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<Playlist> mValues;
    private final PlaylistsFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private PlaylistsContent content;

    public PlaylistsRecyclerViewAdapter(PlaylistsFragment.OnListFragmentInteractionListener listener, Context context) {
        content = new PlaylistsContent(this);
        mValues = content.items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlists_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getName());

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
    public int getItemCount() {
        return mValues.size();
    }

    public void updateItems() {
        content.updateItems();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public Playlist mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.playlist_title);
            mImageView = (ImageView) view.findViewById(R.id.playlist_artwork);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
