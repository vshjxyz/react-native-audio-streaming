package com.audioStreaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

class SignalReceiver extends BroadcastReceiver {
    private Signal signal;

    public SignalReceiver(Signal signal) {
        super();
        this.signal = signal;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        final long step = (30 * 1000);

        if (action.equals(Signal.BROADCAST_PLAYBACK_PLAY)) {
            if (!this.signal.isPlaying()) {
                this.signal.resume();
            } else {
                this.signal.pause();
            }
        } else if (action.equals(Signal.BROADCAST_PLAYBACK_BACKWARD_30_SEC)) {
            if (this.signal.isPlaying()) {
                long progress = this.signal.getCurrentPosition();
                long newPos = progress - step;
                this.signal.seekTo(newPos < 0 ? 0 : newPos);
            }
        } else if (action.equals(Signal.BROADCAST_PLAYBACK_FORWARD_30_SEC)) {
            if (this.signal.isPlaying()) {
                long progress = this.signal.getCurrentPosition();
                long total = this.signal.getDuration();
                long newPos = progress + step;
                this.signal.seekTo(newPos > total ? total : newPos);
            }
        } else if (action.equals(Signal.BROADCAST_EXIT)) {
            this.signal.getNotifyManager().cancelAll();
            this.signal.stop();
            this.signal.exitNotification();
        }
    }
}
