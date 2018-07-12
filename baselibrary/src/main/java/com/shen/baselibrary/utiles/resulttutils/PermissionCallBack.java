package com.shen.baselibrary.utiles.resulttutils;

public abstract class PermissionCallBack {
    public abstract void hasPermission();

    public void refusePermission() {
    }

    public void refusePermissionDonotAskAgain() {
    }
}
