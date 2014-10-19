package mitogh.com.github.tufoto.File;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileName {

    private static final String FORMAT = "yyyyMMdd_HHmmss";
    private static final String PREFIX = "JPEG_";
    private static final String EXTENSION = ".jpg";

    public static String timeStamp(){
        return new SimpleDateFormat(FORMAT).format(new Date());
    }

    public static File create(String directoryPath){
        return new File(
                directoryPath + File.pathSeparator + PREFIX + FileName.timeStamp() + EXTENSION
        );
    }
}
