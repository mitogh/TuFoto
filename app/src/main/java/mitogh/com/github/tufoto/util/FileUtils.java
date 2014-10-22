package mitogh.com.github.tufoto.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    private static final String FORMAT = "yyyyMMdd_HHmmss";
    private static final String PREFIX = "JPEG_";
    private static final String EXTENSION = ".jpg";

    public static String timeStamp(){
        return new SimpleDateFormat(FORMAT).format(new Date());
    }

    public static File createFileIn(String directoryPath){
        return new File(
                directoryPath + File.pathSeparator + PREFIX + FileUtils.timeStamp() + EXTENSION
        );
    }
}
