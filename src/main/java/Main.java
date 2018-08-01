import com.ktp.util.HttpClientUtil;
import com.ktp.util.*;
import org.apache.http.client.CookieStore;

/**
 * Created by Administrator on 2018/7/31 0031.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        HttpClientUtil hcu = new HttpClientUtil();

        CookieStore cookiestore = hcu.getLoginCookie("703382282@qq.com","herry123qwe");
//        System.out.println("cookie:"+cookiestore);
        String res = hcu.getPhjsHtml("https://www.ketangpai.com/Testpaper/dotestpaper/testpaperid/MDAwMDAwMDAwMLOGqZWHz6uy.html");
        System.out.println("res:"+res);
//        String res = hcu.doGet("https://www.ketangpai.com/Interact/index/courseid/MDAwMDAwMDAwMLOcpduHua-x.html",cookiestore);
//        System.out.println("post:"+res);
//        String res = hcu.get("https://www.ketangpai.com/Main/index.html",true);
//        System.out.println("res:"+res);
    }

}
