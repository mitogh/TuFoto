package mitogh.com.github.tufoto.util;

import android.content.Context;
import android.widget.Toast;

public class MessageUtils {

    public static Toast create(Context context, String message){
        return Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
        );
    }
}
