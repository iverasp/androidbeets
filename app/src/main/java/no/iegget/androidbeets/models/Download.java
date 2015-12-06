package no.iegget.androidbeets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by iver on 06/12/15.
 */
@Table(name = "download")
public class Download extends Model{

    @Column(name = "track", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Track track;

    @Column(name = "reference")
    private long reference;

    @Column(name = "status")
    private int status;

    public Download() { super(); }

    public Download(Track track, long reference) {
        super();
        this.track = track;
        this.reference = reference;
    }

    public Track getTrack() {
        return track;
    }

    public long getReference() {
        return reference;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
