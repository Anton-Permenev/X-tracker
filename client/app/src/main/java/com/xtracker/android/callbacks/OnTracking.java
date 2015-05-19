package com.xtracker.android.callbacks;

import com.xtracker.android.objects.Track;

public interface OnTracking {
    public void trackPrepared(Track track);
    public void pointAdded(double length);
}
