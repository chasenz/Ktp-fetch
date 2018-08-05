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
        // 答题url
        String authHost = "https://www.ketangpai.com/Testpaper/dotestpaper/testpaperid/MDAwMDAwMDAwMLSspdyGz9F1.html";
        // 提取答题url的ID
        String getTokenUrl = Utils.getStrByRegex1(authHost,".*testpaperid\\/(.*).html");
        // 获取题目地址
        String getSubjectUrl = "https://www.ketangpai.com/TestpaperApi/doSubjectList?testpaperid="+getTokenUrl;

//        System.out.println("reg:"+getSubjectUrl);
        String res = hcu.doGet(getSubjectUrl,cookiestore);
        System.out.println(res);
    }

}
