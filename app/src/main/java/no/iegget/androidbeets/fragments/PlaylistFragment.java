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
import no.iegget.androidbeets.adapters.PlaylistRecyclerViewAdapter;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.Playlist;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.Global;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlaylistFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Playlist playlist;
    private OnListFragmentInteractionListener mListener;
    private PlaylistRecyclerViewAdapter adapter;

    private Toolbar mToolbar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaylistFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlaylistFragment newInstance(int columnCount, Playlist playlist) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(Global.PLAYLIST, playlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
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

        playlist = (Playlist) getArguments().getSerializable(Global.PLAYLIST);
        PlaylistRecyclerViewAdapter adapter = new PlaylistRecyclerViewAdapter(mListener, playlist);

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

        /*
        Picasso.with(context)
                .load(playlist.getArtworkUrl())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder)
                .into(headerImage);
        */

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
        void onListFragmentInteraction(Track track);
    }
}
