package com.nadji.moviecatalogue.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

class StackWidgetServices extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
