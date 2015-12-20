package cu;

import android.os.Handler;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by wang on 15/12/21.
 */
public class HttpResultHandler implements Callback {

    private Handler hd;

    public HttpResultHandler(Handler hd) {
        this.hd = hd;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        hd.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        hd.post(new Runnable() {
            @Override
            public void run() {
                if (response.isSuccessful()) {

                } else {

                }
            }
        });
    }
}
