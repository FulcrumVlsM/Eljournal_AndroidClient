package com.fulcrumvm.eljclient.fragments;

public interface OnLoadDataListener {
    String getToken();
    void onLoadStateChanged(boolean state);
    void onFailure();
}
