package no.iegget.androidbeets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
                .inflate(R.layout.fragment_albums, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).name);

        int albumYear = mValues.get(position).albumYear;
        String albumGenre = mValues.get(position).albumGenre;

        String year = (albumYear == 0) ? "Unknown year" : Integer.toString(albumYear);
        String genre = (albumGenre.isEmpty()) ? "Unknown genre" : albumGenre;

        holder.mYearView.setText(year);
        holder.mTrackCountView.setText(genre);

        //Picasso.with(mContext).setIndicatorsEnabled(true);
        //Picasso.with(mContext).setLoggingEnabled(true);
        if (holder.mItem.artworkUrl != null) {
            Picasso.with(mContext)
                    .load(holder.mItem.artworkUrl)
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
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mYearView;
        public final TextView mTrackCountView;
        public AlbumsContent.AlbumItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.album_artwork);
            mTitleView = (TextView) view.findViewById(R.id.album_title);
            mYearView = (TextView) view.findViewById(R.id.album_year);
            mTrackCountView = (TextView) view.findViewById(R.id.album_track_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
