package com.zjclugger.qrcode.utils;

import androidx.annotation.ColorRes;

import com.zjclugger.qrcode.R;

import java.io.Serializable;

/**
* 配置类
 */
public class ScannerConfig implements Serializable {
    /*是否播放声音*/
    private boolean isPlayBeep = true;
    /*是否震动*/
    private boolean isShake = true;
    /*是否显示下方的其他功能布局*/
    private boolean isShowBottomLayout = true;
    /*是否显示闪光灯按钮*/
    private boolean isShowFlashLight = true;
    /*是否显示相册按钮*/
    private boolean isShowAlbum = true;
    /*是否解析条形码*/
    private boolean isDecodeBarCode = true;
    /*是否全屏扫描*/
    private boolean isFullScreenScan = true;

    /*四个角的颜色*/
    @ColorRes
    private int reactColor = R.color.react;
    /*扫描框颜色*/
    @ColorRes
    private int frameLineColor = -1;


    /*扫描线颜色*/
    @ColorRes
    private int scanLineColor = R.color.scanLineColor;

    public int getFrameLineColor() {
        return frameLineColor;
    }

    public void setFrameLineColor(@ColorRes int frameLineColor) {
        this.frameLineColor = frameLineColor;
    }

    public int getScanLineColor() {
        return scanLineColor;
    }

    public void setScanLineColor(@ColorRes int scanLineColor) {
        this.scanLineColor = scanLineColor;
    }

    public int getReactColor() {
        return reactColor;
    }

    public void setReactColor(@ColorRes int reactColor) {
        this.reactColor = reactColor;
    }

    public boolean isFullScreenScan() {
        return isFullScreenScan;
    }

    public void setFullScreenScan(boolean fullScreenScan) {
        isFullScreenScan = fullScreenScan;
    }

    public boolean isDecodeBarCode() {
        return isDecodeBarCode;
    }

    public void setDecodeBarCode(boolean decodeBarCode) {
        isDecodeBarCode = decodeBarCode;
    }

    public boolean isPlayBeep() {
        return isPlayBeep;
    }

    public void setPlayBeep(boolean playBeep) {
        isPlayBeep = playBeep;
    }

    public boolean isShake() {
        return isShake;
    }

    public void setShake(boolean shake) {
        isShake = shake;
    }

    public boolean isShowBottomLayout() {
        return isShowBottomLayout;
    }

    public void setShowBottomLayout(boolean showBottomLayout) {
        isShowBottomLayout = showBottomLayout;
    }

    public boolean isShowFlashLight() {
        return isShowFlashLight;
    }

    public void setShowFlashLight(boolean showFlashLight) {
        isShowFlashLight = showFlashLight;
    }

    public boolean isShowAlbum() {
        return isShowAlbum;
    }

    public void setShowAlbum(boolean showAlbum) {
        isShowAlbum = showAlbum;
    }
}
