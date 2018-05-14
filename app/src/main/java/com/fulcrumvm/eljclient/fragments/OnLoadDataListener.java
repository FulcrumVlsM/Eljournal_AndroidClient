package com.fulcrumvm.eljclient.fragments;

public interface OnLoadDataListener {
    void onLoadStateChanged(boolean state);
    void onFailure();
}
