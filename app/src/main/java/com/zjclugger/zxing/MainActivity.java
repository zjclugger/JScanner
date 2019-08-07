package com.zjclugger.zxing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zjclugger.qrcode.encode.QRCodeGenerator;
import com.zjclugger.qrcode.scanner.CaptureActivity;
import com.zjclugger.qrcode.utils.ScannerConfig;
import com.zjclugger.qrcode.utils.ScannerConstants;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView result;
    private EditText contentEt;
    private Button encodeBtn;
    private ImageView contentIv;
    private Toolbar toolbar;
    private Button fragScanBtn;
    private Button otherQRCreate;
    private int REQUEST_CODE_SCAN = 111;
    private final String message = "为保障本应用的功能正常运行，请授于写存储卡和使用相机的权限";
    /**
     * 生成带logo的二维码
     */
    private Button encodeBtnWithLogo;
    private ImageView contentIvWithLogo;
    private String contentEtString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        /*扫描按钮*/
        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        /*扫描结果*/
        result = findViewById(R.id.result);

        /*要生成二维码的输入框*/
        contentEt = findViewById(R.id.contentEt);
        /*生成按钮*/
        encodeBtn = findViewById(R.id.encodeBtn);
        encodeBtn.setOnClickListener(this);
        /*生成的图片*/
        contentIv = findViewById(R.id.contentIv);

        fragScanBtn = findViewById(R.id.fragScanBtn);
        fragScanBtn.setOnClickListener(this);

        otherQRCreate = findViewById(R.id.other_demo);
        otherQRCreate.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("扫一扫");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        result = (TextView) findViewById(R.id.result);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        contentEt = (EditText) findViewById(R.id.contentEt);
        encodeBtnWithLogo = (Button) findViewById(R.id.encodeBtnWithLogo);
        encodeBtnWithLogo.setOnClickListener(this);
        contentIvWithLogo = (ImageView) findViewById(R.id.contentIvWithLogo);
        encodeBtn = (Button) findViewById(R.id.encodeBtn);
        contentIv = (ImageView) findViewById(R.id.contentIv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionDispatcherManager.getInstance().isDialogShowing()) {
            MainActivityPermissionsDispatcher.requestStoragePermissionWithPermissionCheck(MainActivity.this);
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void requestStoragePermission() {
        //申请权限
    }

    private void scan(){
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                /*ZxingConfig是配置类
                                 *可以设置是否显示底部布局，闪光灯，相册，
                                 * 是否播放提示音  震动
                                 * 设置扫描框颜色等
                                 * 也可以不传这个参数
                                 * */
                                ScannerConfig config = new ScannerConfig();
                                config.setPlayBeep(false);//是否播放扫描声音 默认为true
                                config.setShake(false);//是否震动  默认为true
                                //config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                intent.putExtra(ScannerConstants.INTENT_ZXING_CONFIG, config);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showRationaleForStorage(PermissionRequest request) {
        PermissionDispatcherManager.getInstance().showRationaleDialog(this, "",
                message, request, true);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    void onStoragePermissionNeverAskAgain() {
        //被拒绝并勾选不在提醒授权时调用
        PermissionDispatcherManager.getInstance().showAskAgainDialog(this, "",
                message, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @Override
    public void onClick(View v) {

        Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanBtn:
                scan();
                break;
            case R.id.encodeBtn:
                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
                    return;
                }

                bitmap = QRCodeGenerator.create(contentEtString, 400, 400, null);
                if (bitmap != null) {
                    contentIv.setImageBitmap(bitmap);
                }
                break;
            case R.id.encodeBtnWithLogo:
                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.h2);
                bitmap = QRCodeGenerator.create(contentEtString, 400, 400, logo);

                if (bitmap != null) {
                    contentIvWithLogo.setImageBitmap(bitmap);
                }
                break;
            case R.id.fragScanBtn:
                Intent intent = new Intent(this, FragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.other_demo:
                Intent intentDemo = new Intent(this, QRActivity.class);
                startActivity(intentDemo);
                break;
            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(ScannerConstants.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
            }
        }
    }

}
