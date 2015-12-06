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
import no.iegget.androidbeets.content.ArtistsContent;
import no.iegget.androidbeets.fragments.ArtistsFragment.OnListFragmentInteractionListener;
import no.iegget.androidbeets.models.Artist;

/**
 * {@link RecyclerView.Adapter} that can display a {@link no.iegget.androidbeets.models.Artist} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ArtistsRecyclerViewAdapter
        extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<Artist> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private ArtistsContent content;

    public ArtistsRecyclerViewAdapter(OnListFragmentInteractionListener listener, Context context) {
        content = new ArtistsContent(this);
        mValues = content.items;
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
        holder.mTitleView.setText(mValues.get(position).getName());

        //Picasso.with(mContext).setIndicatorsEnabled(true);
        //Picasso.with(mContext).setLoggingEnabled(true);
        if (holder.mItem.getArtworkUrl() != null) {
            Picasso.with(mContext)
                    .load(holder.mItem.getArtworkUrl())
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
        public Artist mItem;

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
