package no.iegget.androidbeets.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.adapters.AlbumRecyclerViewAdapter;
import no.iegget.androidbeets.content.AlbumContent;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.utils.Global;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AlbumFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String albumArtist;
    private String albumTitle;
    private String albumArtworkUrl;
    private Album album;
    private OnListFragmentInteractionListener mListener;
    private AlbumRecyclerViewAdapter adapter;

    private Toolbar mToolbar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AlbumFragment newInstance(int columnCount, Album album) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(Global.ALBUM_TITLE, album.getAlbum());
        args.putString(Global.ALBUM_ARTIST, album.getAlbumartist());
        args.putString(Global.ALBUM_ART_URL, album.getArtworkUrl(1000));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            albumArtist = getArguments().getString(Global.ALBUM_ARTIST);
            albumTitle = getArguments().getString(Global.ALBUM_TITLE);
            albumArtworkUrl = getArguments().getString(Global.ALBUM_ART_URL);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_album_list, container, false);
        Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_album_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        album = new Album(albumArtist, albumTitle, null);
        AlbumRecyclerViewAdapter adapter = new AlbumRecyclerViewAdapter(mListener, album);

        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.view_image_header, recyclerView, false);
        ImageView headerImage = ((ImageView) header.findViewById(R.id.image_header));

        Activity parentActivity = getActivity();
        mToolbar = (Toolbar) parentActivity.findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.toolbarTransparent));

        adapter.setParallaxHeader(header, recyclerView);
        adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View view) {
                // TODO: toolbar alpha
                /*
                mToolbar.setBackgroundColor(
                    ScrollUtils.getColorWithAlpha(percentage, getResources().getColor(R.color.toolbarTransparent))
                );
                */
            }
        });

        recyclerView.setAdapter(adapter);

        Picasso.with(context)
                .load(albumArtworkUrl)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder)
                .into(headerImage);

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AlbumContent.TrackItem item);
    }
}
