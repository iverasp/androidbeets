package no.iegget.androidbeets.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.adapters.PlaylistsRecyclerViewAdapter;
import no.iegget.androidbeets.fragments.PlaylistsFragment;
import no.iegget.androidbeets.models.Playlist;
import no.iegget.androidbeets.models.PlaylistTrack;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.Global;

/**
 * Created by iver on 07/12/15.
 */
public class AddTrackToPlaylistDialog extends DialogFragment implements
        PlaylistsFragment.OnListFragmentInteractionListener {

    private ViewGroup container;
    private OnAddTrackToPlaylistListener mListener;

    public static AddTrackToPlaylistDialog newInstance(Track track) {
        AddTrackToPlaylistDialog dialog = new AddTrackToPlaylistDialog();
        Bundle args = new Bundle();
        args.putSerializable(Global.TRACK, track);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_playlists_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new PlaylistsRecyclerViewAdapter(this, getActivity()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add to playlist");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnAddTrackToPlaylistListener) {
            mListener = (OnAddTrackToPlaylistListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListFragmentInteraction(Playlist playlist) {
        Track track = (Track) getArguments().getSerializable(Global.TRACK);
        Track localTrack = new Select()
                .from(Track.class)
                .where("track_id = ?", track.getTrackId())
                .executeSingle();
        if (localTrack != null) {
            new PlaylistTrack(localTrack, playlist).save();
            if (null != mListener) mListener.onAddTrackToPlaylist(playlist, localTrack);
        } else {
            track.save();
            new PlaylistTrack(track, playlist).save();
            if (null != mListener) mListener.onAddTrackToPlaylist(playlist, track);
        }
        Log.w("HALP", "adding track to playlist " + playlist);
        dismiss();
    }

    public interface OnAddTrackToPlaylistListener {
        void onAddTrackToPlaylist(Playlist playlist, Track track);
    }
}
