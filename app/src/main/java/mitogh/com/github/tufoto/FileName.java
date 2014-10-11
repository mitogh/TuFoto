package mitogh.com.github.tufoto;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileName{

    private static final String format = "yyyyMMdd_HHmmss";

    private static final String PREFIX = "JPEG_";

    private static final String SUFFIX = "_TUFOTO_";

    private static final String EXTENSION = ".jpg";

    private static final File STORAGE_DIRECTORY = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
    );

    private StringBuilder timeStamp = new StringBuilder();

    private StringBuilder imageFileName = new StringBuilder();

    public File image;


    public FileName() throws IOException {
        this.generate();
    }

    private void generate() throws IOException {

        appendFileNameToTimeStamp();

        createImageFileName();

        image = File.createTempFile(
                getImageFileName(),
                EXTENSION,
                STORAGE_DIRECTORY
        );
    }


    private void createImageFileName() {
        imageFileName.append(
                PREFIX
        ).append(
                timeStamp
        ).append(
                SUFFIX
        );
    }

    public File getImageFile(){
        return image;
    }

    private String getImageFileName(){
        return imageFileName.toString();
    }

    private void appendFileNameToTimeStamp() {
        timeStamp.append(
                this.fileName()
        );
    }

    private static String fileName() {
        return new SimpleDateFormat(format).format(
                new Date()
        );
    }


}
