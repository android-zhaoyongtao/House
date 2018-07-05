package com.shen.baselibrary.utiles.requestutiles;

public abstract class PermissionCallBack {
    public abstract void hasPermission();

    public void refusePermission() {
    }

    public void refusePermissionDonotAskAgain() {
    }
}
