package com.project.publicNo.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * 把两张图片合并
 *
 * @author nurxat
 */
public class ImgUtil {
    public static InputStream load(BufferedImage qrcodeImage, String bgImgAddress) {
        SimpleImage smpleImage = new SimpleImage();
        BufferedImage background = smpleImage.loadImageLocal(bgImgAddress);
        BufferedImage result = smpleImage.modifyImagetogeter(qrcodeImage, background);
        return smpleImage.bufferedImageToInputStream(result);
    }

    public static class SimpleImage {
        private Font font = new Font("宋体", Font.PLAIN, 12); // 添加字体的属性设置

        private Graphics2D graphics2D = null;

        private int fontsize = 0;

        private int x = 0;

        private int y = 0;

        /**
         * 导入本地图片到缓冲区
         */
        public BufferedImage loadImageLocal(String imgAddress) {
            try {
                File file = new File(imgAddress);
                return ImageIO.read(file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        /**
         * 导入网络图片到缓冲区
         */
        public BufferedImage loadImageUrl(String imgUrl) {
            try {
                URL url = new URL(imgUrl);
                return ImageIO.read(url);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        /**
         * 生成新图片到本地
         */
        public void writeImageLocal(String imgStoreAddress, BufferedImage imgSource) {
            if (imgStoreAddress != null && imgSource != null) {
                try {
                    File outputfile = new File(imgStoreAddress);
                    ImageIO.write(imgSource, "jpg", outputfile);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        /**
         * 设定文字的字体等
         */
        public void setFont(String fontStyle, int fontSize) {
            this.fontsize = fontSize;
            this.font = new Font(fontStyle, Font.PLAIN, fontSize);
        }

        /**
         * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
         */
        public BufferedImage modifyImage(BufferedImage imgSource, Object content, int x, int y) {

            try {
                int w = imgSource.getWidth();
                int h = imgSource.getHeight();
                graphics2D = imgSource.createGraphics();
                graphics2D.setBackground(Color.WHITE);
                graphics2D.setColor(Color.orange);//设置字体颜色
                if (this.font != null)
                    graphics2D.setFont(this.font);
                // 验证输出位置的纵坐标和横坐标
                if (x >= h || y >= w) {
                    this.x = h - this.fontsize + 2;
                    this.y = w;
                } else {
                    this.x = x;
                    this.y = y;
                }
                if (content != null) {
                    graphics2D.drawString(content.toString(), this.x, this.y);
                }
                graphics2D.dispose();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return imgSource;
        }

        /**
         * 修改图片,返回修改后的图片缓冲区（输出多个文本段） xory：true表示将内容在一行中输出；false表示将内容多行输出
         */
        public BufferedImage modifyImage(BufferedImage imgSource, Object[] contentArr, int x, int y,
                                         boolean xory) {
            try {
                int w = imgSource.getWidth();
                int h = imgSource.getHeight();
                graphics2D = imgSource.createGraphics();
                graphics2D.setBackground(Color.WHITE);
                graphics2D.setColor(Color.RED);
                if (this.font != null)
                    graphics2D.setFont(this.font);
                // 验证输出位置的纵坐标和横坐标
                if (x >= h || y >= w) {
                    this.x = h - this.fontsize + 2;
                    this.y = w;
                } else {
                    this.x = x;
                    this.y = y;
                }
                if (contentArr != null) {
                    int arrlen = contentArr.length;
                    if (xory) {
                        for (int i = 0; i < arrlen; i++) {
                            graphics2D.drawString(contentArr[i].toString(), this.x, this.y);
                            this.x += contentArr[i].toString().length() * this.fontsize / 2 + 5;// 重新计算文本输出位置
                        }
                    } else {
                        for (int i = 0; i < arrlen; i++) {
                            graphics2D.drawString(contentArr[i].toString(), this.x, this.y);
                            this.y += this.fontsize + 2;// 重新计算文本输出位置
                        }
                    }
                }
                graphics2D.dispose();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return imgSource;
        }

        /**
         * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
         * <p>
         * 时间:2007-10-8
         *
         * @param imgSource
         * @return
         */
        public BufferedImage modifyImageYe(BufferedImage imgSource, String content) {

            try {
                int w = imgSource.getWidth();
                int h = imgSource.getHeight();
                graphics2D = imgSource.createGraphics();
                graphics2D.setBackground(Color.WHITE);
                graphics2D.setColor(Color.blue);//设置字体颜色
                if (this.font != null)
                    graphics2D.setFont(this.font);
                graphics2D.drawString(content, w - 85, h - 5);
                graphics2D.dispose();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return imgSource;
        }

        public BufferedImage modifyImagetogeter(BufferedImage qrcode, BufferedImage background) {
            try {
                int w = qrcode.getWidth();
                int h = qrcode.getHeight();

                graphics2D = background.createGraphics();
                graphics2D.drawImage(qrcode, 100, 1000, w, h, null);
                graphics2D.dispose();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return background;
        }

        public InputStream bufferedImageToInputStream(BufferedImage image) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpg", os);
                InputStream input = new ByteArrayInputStream(os.toByteArray());
                return input;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}  