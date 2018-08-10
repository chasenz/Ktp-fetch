import com.ktp.bean.examInfo;
import com.ktp.file.WriteTxtFile;
import com.ktp.parse.JsonParse;
import com.ktp.util.HttpClientUtil;
import com.ktp.util.*;
import org.apache.http.client.CookieStore;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * 测试入口
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String email = "";      //登陆账号
        String password = "";   //登陆密码
        String fetch_url = "";  //抓取测试连接

        Scanner sc = new Scanner(System.in);
        System.out.println("本程序用于课堂派[ketangpai.com]测试题目抓取！");
        System.out.print("请输入您的用户名：");
        email = sc.nextLine();
        System.out.print("请输入您的密 码：");
        password = sc.nextLine();
        HttpClientUtil hcu = new HttpClientUtil();
        // 第一次登陆获取cookie凭证
        CookieStore cookiestore = hcu.getLoginCookie(email,password);
        // 登陆成功后
        if(cookiestore != null){
            System.out.println("测试地址形如：https://www.ketangpai.com/Testpaper/dotestpaper/testpaperid/MDAwMDAwMDAwMLSspdyGz9F1.html");
            System.out.println("请输入您的抓取地址：");
            fetch_url = sc.nextLine();
            String getTokenUrl = Utils.getStrByRegex1(fetch_url,".*testpaperid\\/(.*).html");
            // 获取动态题目地址
            String getSubjectUrl = "https://www.ketangpai.com/TestpaperApi/doSubjectList?testpaperid="+getTokenUrl;
            // 获取题目Json数据
            String res = hcu.doGet(getSubjectUrl,cookiestore);
            System.out.println(res);
            // 解析题目Json数据
            List<examInfo> examList = JsonParse.examParse(res);
            // 数据解析成功
            if(examList != null) {
                System.out.println("Json数据解析成功！");
                // 把题目写入myTest.txt文件中
                String savePath = System.getProperty("user.dir") + "\\myTest.txt";
                File file = new File(savePath);
                WriteTxtFile.writeTxtFile(examList, file);
                System.out.println("测试题目已经保存在当前目录，文件名为myTest.txt");
            }
        }
    }

    public void TestMain() throws Exception {
        HttpClientUtil hcu = new HttpClientUtil();
        // 第一次登陆获取cookie凭证
        CookieStore cookiestore = hcu.getLoginCookie("Your email","Your password");
        // 答题url
        String authHost = "这里是完整的测试题目Url连接";
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
