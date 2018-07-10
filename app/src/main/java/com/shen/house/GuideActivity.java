package com.shen.house;

import android.content.Intent;
import android.view.View;

import com.shen.baselibrary.base.BaseActivity;


public class GuideActivity extends BaseActivity {

    @Override
    public int getcontentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void afterInjectView(View view) {
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        IntentHelper.parseUri(this, intent.getData(), "h5");
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
