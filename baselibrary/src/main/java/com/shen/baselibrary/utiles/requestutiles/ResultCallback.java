package com.shen.baselibrary.utiles.requestutiles;

import android.content.Intent;

public interface ResultCallback {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}