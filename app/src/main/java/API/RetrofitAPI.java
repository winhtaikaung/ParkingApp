package API;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by winhtaikaung on 2/27/16.
 */
public class RetrofitAPI {
    private static RetrofitAPI mInstance;
    private ApiInterface mService;



    public RetrofitAPI(Context context) {

        //ApiRequestInterceptor request_Interceptor = new ApiRequestInterceptor();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(300000, TimeUnit.MILLISECONDS);


        String BASE_URL = ApiConfig.Base_URL;

        String credentials = ApiConfig.username + ":" +ApiConfig.pwd;
        // create Base64 encodet string
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Log.i("BASE_URL", BASE_URL);

        final RestAdapter restAdapter = new
                RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", basic);
                    }
                })

                .setClient(new OkClient(okHttpClient))

                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.e("-------Car PARK API-----------", msg);
                    }
                })
                .setConverter(new StringConverter()) //Reply String result
                .build();





        mService = restAdapter.create(ApiInterface.class);


    }


    /*RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            request.addHeader("Authorization","Basic <base64-encoded info@myanmarplus.net:L3bE33t3y16yr4Zk9ft1qhZid0IE938W pair>=");

            //request.addHeader("API key", "L3bE33t3y16yr4Zk9ft1qhZid0IE938W");
        }
    };*/




    public static RetrofitAPI getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RetrofitAPI(context);
        }


        return mInstance;
    }

    public ApiInterface getService() {
        return mService;
    }

    public static class StringConverter implements Converter {


        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            String text = null;
            try {
                text = fromStream(body.in());
            } catch (IOException ignored) {/*NOP*/ }

            return text;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return null;
        }

        public static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }
}
