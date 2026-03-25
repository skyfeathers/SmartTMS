package net.lab1024.tms.common.common.util;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024
 * @copyright (c) 2018 1024 Inc. All rights reserved.
 * @date 2019/8/28 0028 上午 10:59
 * @since JDK1.8
 */
public class SmartHtml2PdfUtil {

    public static final String HTML2PDF_FONT_NAME = "STSONG.TTF";

    public static File html2pdf(String htmlContent, String destPath) {
        FileOutputStream fileOutputStream = null;
        File outPutFile = null;
        boolean exceptionFlag = false;
        try {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    return this.getClass().getClassLoader().getResourceAsStream(HTML2PDF_FONT_NAME);
                }

            }, "SimSun");
            builder.withHtmlContent(htmlContent,null);
            outPutFile = new File(destPath);
            fileOutputStream = new FileOutputStream(outPutFile);
            builder.toStream(fileOutputStream);
            builder.run();
            return outPutFile;
        } catch (Exception e) {
            e.printStackTrace();
            exceptionFlag = true;
            return null;
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fileOutputStream = null;
                }
            }
            if(exceptionFlag){
                if(outPutFile != null){
                    outPutFile.delete();
                }
            }
        }
    }



    /**
     *  html转pdf
     */
    public static File wkConvertToPdf(String srcPath,String toPdfTool, String destPath){
        ProcessBuilder pb = new ProcessBuilder(toPdfTool, srcPath, destPath);
        if(convert(pb)){
            return new File(destPath);
        }
        return null;
    }

    /**
     *  html转image
     */
    public static File wkConvertToImage(String srcPath,String toImageTool, String destPath){
        ProcessBuilder pb = new ProcessBuilder(toImageTool, srcPath, destPath);
        if(convert(pb)){
            return new File(destPath);
        }
        return null;
    }

    private static boolean convert(ProcessBuilder pb){
        Process process = null;
        boolean result = true;
        BufferedReader errStreamReader = null;
        try {
            process = pb.start();
            //注意，调用process.getErrorStream()而不是process.getInputStream()
            errStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            line = errStreamReader.readLine();
            while(line != null) {
                line = errStreamReader.readLine();
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }finally {
            if (errStreamReader != null) {
                try {
                    errStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(process != null){
                process.destroy();
            }
        }
        return result;
    }
}
