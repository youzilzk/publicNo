package com.project.publicNo.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;

public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 70;
    // LOGO高度
    private static final int HEIGHT = 70;

    private static BufferedImage createImage(String content, String insertImgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (insertImgPath == null || "".equals(insertImgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, insertImgPath, needCompress);
        return image;
    }

    private static URL getImageFromUrl(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            //测试url是否合法
            url.openConnection();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Image getImageFromLocal(String imgAddr) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(imgAddr);
            InputStream inputStream = classPathResource.getInputStream();
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertImage(BufferedImage source, String insertImgPath, boolean needCompress) throws Exception {
        Image src = null;
        Image imageFromLocal = getImageFromLocal(insertImgPath);
        if (imageFromLocal != null) {
            src = imageFromLocal;
        } else {
            return;
        }
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    private static void insertImage(BufferedImage source, URL insertImgUrl, boolean needCompress) throws Exception {
        Image src = ImageIO.read(insertImgUrl);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    //包含logo的生成方式
    public static void encode(String content, String insertImgPath, String resultPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, insertImgPath, needCompress);
        mkdirs(resultPath);
        ImageIO.write(image, FORMAT_NAME, new File(resultPath));
    }

    public static BufferedImage encode(String content, String insertImgPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, insertImgPath, needCompress);
        return image;
    }

    public static void mkdirs(String resultPath) {
        File file = new File(resultPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String insertImgPath, String resultPath) throws Exception {
        QRCodeUtil.encode(content, insertImgPath, resultPath, false);
    }

    public static void encode(String content, String resultPath) throws Exception {
        QRCodeUtil.encode(content, resultPath, false);
    }

    public static void encode(String content, String insertImgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, insertImgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }
}
