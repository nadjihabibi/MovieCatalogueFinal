package com.nadji.moviecatalogue.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetServicesTvshow extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewFactoryTvshow(this.getApplicationContext());
    }
}
