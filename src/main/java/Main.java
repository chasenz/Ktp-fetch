import com.ktp.util.HttpClientUtil;
import com.ktp.util.Utils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/7/31 0031.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        HttpClientUtil hcu = new HttpClientUtil();
//        String res = hcu.getResponse("https://idanmu.at/v15/v08/92354/");
        String res = Utils.getPhjsHtml("http://www.nfydd.com/shaonv/xiaonvzifeijiabuke/");
        System.out.println("res:"+res);

    }

}
