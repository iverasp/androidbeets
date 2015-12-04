package no.iegget.androidbeets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.content.AlbumContent;
import no.iegget.androidbeets.fragments.AlbumFragment;
import no.iegget.androidbeets.fragments.AlbumFragment.OnListFragmentInteractionListener;
import no.iegget.androidbeets.models.Album;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AlbumContent.TrackItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AlbumRecyclerViewAdapter
        extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<AlbumContent.TrackItem> mValues;
    private final AlbumFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private AlbumContent content;

    public AlbumRecyclerViewAdapter(AlbumFragment.OnListFragmentInteractionListener listener, Context context, Album album) {
        content = new AlbumContent(this, album);
        mValues = content.ITEMS;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public AlbumContent.TrackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.track_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
