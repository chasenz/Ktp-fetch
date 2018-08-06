import com.ktp.bean.examInfo;
import com.ktp.file.WriteTxtFile;
import com.ktp.parse.JsonParse;
import com.ktp.util.HttpClientUtil;
import com.ktp.util.*;
import org.apache.http.client.CookieStore;

import java.io.File;
import java.util.List;

/**
 * 测试入口
 */
public class Main {

    public static void main(String[] args) throws Exception {
        HttpClientUtil hcu = new HttpClientUtil();
        // 第一次登陆获取cookie凭证
        CookieStore cookiestore = hcu.getLoginCookie("your email","your password");
        // 答题url
        String authHost = "https://www.ketangpai.com/Testpaper/dotestpaper/testpaperid/MDAwMDAwMDAwMLSspdyGz9F1.html";
        // 提取答题url的ID
        String getTokenUrl = Utils.getStrByRegex1(authHost,".*testpaperid\\/(.*).html");
        // 获取动态题目地址
        String getSubjectUrl = "https://www.ketangpai.com/TestpaperApi/doSubjectList?testpaperid="+getTokenUrl;
        // 获取题目Json数据
        String res = hcu.doGet(getSubjectUrl,cookiestore);
        System.out.println(res);
        // 解析题目Json数据
        List<examInfo> examList = JsonParse.examParse(res);
        // 打印：验证结果是否正确
        for(examInfo ei : examList){
            System.out.println(ei.getNo());
            System.out.println("过滤html标签后------->"+Utils.getExamByRegex(ei.getTitle()));
            System.out.println("原始Json数据--------->"+ei.getTitle());
            for(String s : ei.getOptionList())
                System.out.println(Utils.getExamByRegex(s));
            System.out.println("==============================");
        }
        // 把题目写入myTest.txt文件中
        File file = new File("D:\\myTest.txt");
        WriteTxtFile.writeTxtFile(examList,file);
    }

}
