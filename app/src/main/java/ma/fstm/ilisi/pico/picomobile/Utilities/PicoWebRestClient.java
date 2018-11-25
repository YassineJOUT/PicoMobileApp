package ma.fstm.ilisi.pico.picomobile.Utilities;

import com.loopj.android.http.*;
// Rest API client class
public class PicoWebRestClient  {
    // Url to the api
    private static final String BASE_URL = "http://pico.ossrv.nl:9090/api/";
    // Asynchronous Http client
    private static AsyncHttpClient client = new AsyncHttpClient();
    // adding the content type to the header
    public  static void setUp(){
        client.addHeader("content-type","application/x-www-form-urlencoded");
    }
    // Get function
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    // Post function
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    // Absolute url to the API
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}