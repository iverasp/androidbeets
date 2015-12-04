package no.iegget.androidbeets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.content.AlbumsContent;
import no.iegget.androidbeets.fragments.AlbumsFragment;


/**
 * {@link RecyclerView.Adapter} that can display a {@link no.iegget.androidbeets.content.AlbumsContent.AlbumItem} and makes a call to the
 * specified {@link no.iegget.androidbeets.fragments.AlbumsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AlbumsRecyclerViewAdapter
        extends RecyclerView.Adapter<AlbumsRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<AlbumsContent.AlbumItem> mValues;
    private final AlbumsFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private AlbumsContent content;

    public AlbumsRecyclerViewAdapter(AlbumsFragment.OnListFragmentInteractionListener listener, Context context, String artist) {
        content = new AlbumsContent(this, artist);
        mValues = content.ITEMS;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_artists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).name);

        //Picasso.with(mContext).setIndicatorsEnabled(true);
        //Picasso.with(mContext).setLoggingEnabled(true);
        if (holder.mItem.artworkUrl != null) {
            Picasso.with(mContext)
                    .load(holder.mItem.artworkUrl)
                    .resize(50, 50)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.mImageView);

        }

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
        public final ImageView mImageView;
        public AlbumsContent.AlbumItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.artist_title);
            mImageView = (ImageView) view.findViewById(R.id.artist_artwork);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}