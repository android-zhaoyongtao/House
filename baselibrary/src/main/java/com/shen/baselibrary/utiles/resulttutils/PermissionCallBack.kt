package com.shen.baselibrary.utiles.resulttutils

abstract class PermissionCallBack {
    abstract fun hasPermission()

    open fun refusePermission() {}

    open fun refusePermissionDonotAskAgain() {}
}
