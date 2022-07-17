import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionTester {
    public static void main(String[] args) throws IOException {
        String hostName = "https://matrix.org";
        String token = "syt_bXl0ZXN0MjAyMg_xnfImdPEhxWYyVdSiKLS_1xLPq2";
        String path = "/_matrix/client/v3/account/whoami";
        String surl = hostName + path + "?access_token="+token + "";

        URL obj = new URL(surl);
        URLConnection con = obj.openConnection();
        HttpsURLConnection http = (HttpsURLConnection)con;

        con.setConnectTimeout(3000);

        http.setRequestMethod("GET");
        System.out.println(http.usingProxy());
        System.out.println(http.getResponseMessage());
        System.out.println(http.getContent());

    }
}
