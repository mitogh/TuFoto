package mitogh.com.github.tufoto;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFile {

    private static final String FORMAT = "yyyyMMdd_HHmmss";
    private static final String PREFIX = "JPEG_";
    private static final String SUFFIX = "_TUFOTO_";
    private static final String EXTENSION = ".jpg";
    private static final java.io.File STORAGE_DIRECTORY = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
    );

    private StringBuilder timeStamp = new StringBuilder();
    private StringBuilder fileName = new StringBuilder();
    public File image;


    public ImageFile() throws IOException {
        this.generate();
    }

    private void generate() throws IOException {

        appendFileNameToTimeStamp();

        createImageFileName();

        image = File.createTempFile(
                getFileName(),
                EXTENSION,
                STORAGE_DIRECTORY
        );
    }


    private void createImageFileName() {
        fileName.append(
                PREFIX
        ).append(
                timeStamp
        ).append(
                SUFFIX
        );
    }

    public java.io.File getImageFile(){
        return image;
    }

    private String getFileName(){
        return fileName.toString();
    }

    private void appendFileNameToTimeStamp() {
        timeStamp.append(
                this.fileName()
        );
    }

    private static String fileName() {
        return new SimpleDateFormat(FORMAT).format(
                new Date()
        );
    }


}
