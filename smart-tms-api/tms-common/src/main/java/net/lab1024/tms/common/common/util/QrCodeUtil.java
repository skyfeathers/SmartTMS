package net.lab1024.tms.common.common.util;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;

/**
 * 二维码
 *
 * @author Liu Yutao <koal@vip.qq.com>
 * @date 2017/8/28 13:40
 */
@Slf4j
public class QrCodeUtil {
    //编码
    private static final String CHARSET = "utf-8";
    //输出格式
    public static final String FORMAT_NAME = "JPG";
    //base64前缀
    private static final String BASE64_PREFIX = "data:image/jpg;base64,";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 75;
    // LOGO高度
    private static final int HEIGHT = 75;

    /**
     * 生成二维码图片流
     *
     * @param content      二维码内容
     * @param logoPath     二维码LOGO路径
     * @param needCompress 是否压缩二维码LOGO
     * @return BufferedImage
     * @throws Exception 异常
     */
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        //外边框距离
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 插入图片
        if (StringUtils.isNotBlank(logoPath)) {
            QrCodeUtil.insertImage(image, logoPath, needCompress);
        }
        return image;
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param logoPath     LOGO图片地址
     * @param needCompress 是否压缩
     */
    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        Image src = ImageIO.read(new URL(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
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


    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     */
    private static String encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, logoPath, needCompress);
        mkdirs(destPath);
        String destFileName = new Random().nextInt(99999999) + ".jpg";
        String destFileFullPath = destPath + "/" + destFileName;
        ImageIO.write(image, FORMAT_NAME, new File(destFileFullPath));
        return destFileFullPath;
    }
    
    /**
     * 生成二维码(自定义名称)
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     */
    private static String encode(String name,String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, logoPath, needCompress);
        mkdirs(destPath);
        String destFileName = StringUtils.isBlank(name) ? new Random().nextInt(99999999)+"" : name + ".jpg";
        String destFileFullPath = destPath + "/" + destFileName;
        ImageIO.write(image, FORMAT_NAME, new File(destFileFullPath));
        return destFileFullPath;
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     */
    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content  内容
     * @param logoPath LOGO地址
     * @param destPath 存储地址
     */
    public static String encode(String content, String logoPath, String destPath) throws Exception {
        return QrCodeUtil.encode(content, logoPath, destPath, false);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param destPath     存储地址
     * @param needCompress 是否压缩LOGO
     */
    public static String encode(String content, String destPath, boolean needCompress) throws Exception {
        return QrCodeUtil.encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param logoPath     logo地址
     * @param needCompress 是否压缩LOGO
     */
    public static BufferedImage image(String content, String logoPath, boolean needCompress) throws Exception {
        return QrCodeUtil.createImage(content, logoPath, needCompress);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     */
    public static BufferedImage image(String content) throws Exception {
        return QrCodeUtil.createImage(content, null, false);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     */
    public static byte[] base64(String content) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = QrCodeUtil.createImage(content, null, false);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "gif", out);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error("生成二维码base64异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 存储地址
     */
    public static String encode(String content, String destPath) throws Exception {
        return QrCodeUtil.encode(content, null, destPath, false);
    }
    
    /**
     * 生成二维码(自定义名称)
     *
     * @param content  内容
     * @param destPath 存储地址
     */
    public static String encodeByName(String name,String content, String destPath) throws Exception {
        return QrCodeUtil.encode(name,content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param output  输出流
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QrCodeUtil.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     */
    private static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     */
    private static String decode(String path) throws Exception {
        return QrCodeUtil.decode(new File(path));
    }

    /**
     * 图片流转换base64
     *
     * @param bufferedImage 图片流
     * @return base64
     */
    private static String toBase64(BufferedImage bufferedImage) {
        //io流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //写入流中
        try {
            ImageIO.write(bufferedImage, FORMAT_NAME, outputStream);
        } catch (IOException e) {
            log.error("图片流转换base64失败");
        }
        //转换成字节
        byte[] bytes = outputStream.toByteArray();
        //转换成base64串
        String base64 = Base64.getEncoder().encodeToString(bytes);
        //删除 \r\n
        return BASE64_PREFIX + base64.replaceAll("\n", "").replaceAll("\r", "");
    }


}
