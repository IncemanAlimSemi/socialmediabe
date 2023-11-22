package com.alseinn.socialmedia.utils;

import com.alseinn.socialmedia.service.storage.ImageService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    private static final int BUFFER_SIZE = 4 * 1024;
    private static final Logger LOG = Logger.getLogger(ImageService.class.getName());

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[BUFFER_SIZE];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            LOG.warning("Error occurred while decompressing image: " + e.getMessage());
            return null;
        }
        return outputStream.toByteArray();
    }



    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[BUFFER_SIZE];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.warning("Error occurred while decompressing image: " + e.getMessage());
            return null;
        }
        return outputStream.toByteArray();
    }

}