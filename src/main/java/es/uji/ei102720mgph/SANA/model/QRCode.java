package es.uji.ei102720mgph.SANA.model;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class QRCode {

    public File generateQR(File file, String text, int h, int w) throws Exception {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, w, h);

        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrix.getWidth(), matrix.getHeight());
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        ImageIO.write(image, "png", file);
        return file;
    }

    public String decoder(File file) throws Exception {

        FileInputStream inputStream = new FileInputStream(file);

        BufferedImage image = ImageIO.read(inputStream);

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);
        return new String(result.getText());
    }
}
