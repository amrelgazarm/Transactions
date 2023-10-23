package com.qceccenter.qcec.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {
    private static boolean doesDirExists = false;

    public static File saveBitmapToStorage(Bitmap bmp, int arrayIndex) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String imagesDirectory = "/QCEC_Pictures";
        if (!doesDirExists) {
            checkIfImageDirExists(extStorageDirectory, imagesDirectory);
        }

        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "QCEC_Pictures/qcec_image_" + Integer.toString(arrayIndex) + ".jpg");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "QCEC_Pictures/qcec_image_" + Integer.toString(arrayIndex) + ".jpg");
        }
        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private static void checkIfImageDirExists(String extStorageDirectory, String imagesDirectory){
        File dir = new File(extStorageDirectory + imagesDirectory);
        if (!dir.isDirectory()) {
            dir.mkdir();
            doesDirExists = true;
        } else {
            doesDirExists = true;
        }
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
    }

    private File convertBitmapToFile3(Bitmap bitmap, Context context) {
        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), "image_" + getTimeStamp() + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//          Convert bitmap to byte array
//          Bitmap bitmap = your bitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//          write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private File convertBitmapToFile(@NotNull Bitmap bitmap, Context context, int imageIndex) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);

        File f = new File(context.getCacheDir(), "image_" + Integer.toString(imageIndex++) + ".jpg");
        try {
            f.createNewFile();
            byte[] bitmapdata = bos.toByteArray();
//              write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private File convertBitmapToFile2(@NotNull Bitmap bitmap, Context context, int imageIndex) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        File dir = new File(context.getFilesDir(), "images_dir");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, "image_" + Integer.toString(imageIndex++) + ".jpg");
        try {
            file.createNewFile();
            OutputStream fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(byteArray);
            fileOuputStream.flush();
            fileOuputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
