package com.beetrack.bitcoin_wallet.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.beetrack.bitcoin_wallet.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrConvertHelper {

    public static Bitmap qrConvert(String stringToConvert, Context ctx) throws WriterException {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(stringToConvert, BarcodeFormat.QR_CODE, 180, 180);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : ContextCompat.getColor(ctx, R.color.colorCircle));
            }
        }
        return bmp;
    }

}
