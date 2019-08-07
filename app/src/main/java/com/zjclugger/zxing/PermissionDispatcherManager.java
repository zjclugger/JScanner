package com.zjclugger.zxing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import permissions.dispatcher.PermissionRequest;

public class PermissionDispatcherManager {
    private AlertDialog mAskAgainDialog;

    private static class SingletonHolder {
        private static PermissionDispatcherManager instance = new PermissionDispatcherManager();
    }

    private PermissionDispatcherManager() {
    }

    public static PermissionDispatcherManager getInstance() {
        return SingletonHolder.instance;
    }


    public void showRationaleDialog(Context context, String title, String message,
                                    PermissionRequest request) {
        showRationaleDialog(context, title, message, request, false);
    }

    public void showRationaleDialog(Context context, String title, String message,
                                    PermissionRequest request, boolean disableCancelButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!disableCancelButton) {
            builder.setNegativeButton("取消", (dialog, which) -> request.cancel());
        }
        builder.setTitle(title)
                .setPositiveButton("确定", (dialog, which) -> request.proceed())
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

    public void showAskAgainDialog(Context context, String title, String message,
                                   boolean disableCancelButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!disableCancelButton) {
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
        }
        builder.setTitle(title)
                .setPositiveButton("设置", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                })
                .setCancelable(false)
                .setMessage(message);
        mAskAgainDialog = builder.create();
        mAskAgainDialog.show();
    }

    public boolean isDialogShowing() {
        return mAskAgainDialog != null && mAskAgainDialog.isShowing();
    }

    public void showAskAgainDialog(Context context, String title, String message) {
        showAskAgainDialog(context, title, message, false);
    }
}
