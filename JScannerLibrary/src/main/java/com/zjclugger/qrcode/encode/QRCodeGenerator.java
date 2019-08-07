package com.zjclugger.qrcode.encode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 二维码生成器
 */
public class QRCodeGenerator {
    /**
     * 生成二维码
     *
     * @param QRString             字符串内容
     * @param width                二维码宽度
     * @param height               二维码高度
     * @param characterSet         编码方式（一般使用UTF-8）
     * @param errorCorrectionLevel 容错率 L：7% M：15% Q：25% H：35%
     * @param margin               空白边距（二维码与边框的空白区域）
     * @param lineColor            线色
     * @param background           背景色
     * @param logoBitmap           logo图片
     * @param logoPercent          logo所占百分比
     * @param lineBitmap           用来代替线的图片（传null时不代替）
     * @return
     */
    public static Bitmap create(String QRString, int width, int height, String characterSet,
                                String errorCorrectionLevel, String margin, int lineColor,
                                int background, Bitmap logoBitmap, float logoPercent,
                                Bitmap lineBitmap) {
        // 字符串内容判空
        if (TextUtils.isEmpty(QRString)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(characterSet)) {
                hints.put(EncodeHintType.CHARACTER_SET, characterSet);
            }
            // 容错率设置
            if (!TextUtils.isEmpty(errorCorrectionLevel)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(QRString, BarcodeFormat.QR_CODE, width
                    , height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            if (lineBitmap != null) {
                //从当前位图按一定的比例创建一个新的位图
                lineBitmap = Bitmap.createScaledBitmap(lineBitmap, width, height, false);
            }
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {// 黑色色块像素设置
                        if (lineBitmap != null) {//图片不为null，则将黑色色块换为新位图的像素。
                            pixels[y * width + x] = lineBitmap.getPixel(x, y);
                        } else {
                            pixels[y * width + x] = lineColor;
                        }
                    } else {
                        pixels[y * width + x] = background;// 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            /** 5.为二维码添加logo图标 */
            if (logoBitmap != null) {
                return addQRCodeLogo(bitmap, logoBitmap, logoPercent);
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向二维码中间添加logo图片(图片合成)
     *
     * @param srcBitmap   原图片（生成的简单二维码图片）
     * @param logoBitmap  logo图片
     * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1] )
     *                    原图片是二维码时,建议使用0.2F,百分比过大可能导致二维码扫描失败。
     * @return
     */
    @Nullable
    private static Bitmap addQRCodeLogo(@Nullable Bitmap srcBitmap, @Nullable Bitmap logoBitmap,
                                        float logoPercent) {
        if (srcBitmap == null) {
            return null;
        }
        if (logoBitmap == null) {
            return srcBitmap;
        }
        //传值不合法时使用0.2F
        if (logoPercent < 0F || logoPercent > 1F) {
            logoPercent = 0.2F;
        }

        /** 1. 获取原图片和Logo图片各自的宽、高值 */
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        /** 2. 计算画布缩放的宽高比 */
        float scaleWidth = srcWidth * logoPercent / logoWidth;
        float scaleHeight = srcHeight * logoPercent / logoHeight;

        /** 3. 使用Canvas绘制,合成图片 */
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.scale(scaleWidth, scaleHeight, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logoBitmap, srcWidth / 2 - logoWidth / 2,
                srcHeight / 2 - logoHeight / 2, null);

        return bitmap;
    }

    /**
     * 生成二维码
     *
     * @param QRString
     * @param width
     * @param height
     * @param logo
     * @return
     */
    public static Bitmap create(String QRString, int width, int height, Bitmap logo) {
        if (TextUtils.isEmpty(QRString)) {
            return null;
        }
        /*偏移量*/
        int offsetX = width / 2;
        int offsetY = height / 2;

        /*生成logo*/
        Bitmap logoBitmap = null;

        if (logo != null) {
            Matrix matrix = new Matrix();
            float scaleFactor = Math.min(width * 1.0f / 5 / logo.getWidth(),
                    height * 1.0f / 5 / logo.getHeight());
            matrix.postScale(scaleFactor, scaleFactor);
            logoBitmap = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(),
                    matrix, true);
        }

        /*如果log不为null,重新计算偏移量*/
        int logoW = 0;
        int logoH = 0;
        if (logoBitmap != null) {
            logoW = logoBitmap.getWidth();
            logoH = logoBitmap.getHeight();
            offsetX = (width - logoW) / 2;
            offsetY = (height - logoH) / 2;
        }

        /*指定为UTF-8*/
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 0);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(QRString, BarcodeFormat.QR_CODE, width,
                    height, hints);

            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x >= offsetX && x < offsetX + logoW && y >= offsetY && y < offsetY + logoH) {
                        int pixel = logoBitmap.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (matrix.get(x, y)) {
                                pixel = 0xff000000;
                            } else {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * width + x] = pixel;
                    } else {
                        if (matrix.get(x, y)) {
                            pixels[y * width + x] = 0xff000000;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            System.out.print(e);
            return null;
        }
    }
}
