package com.ktp.util;

import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    /**
     * 获取课堂派登陆Cookie
     * @param email - 邮箱/账号/手机号
     * @param password - 密码
     * @return cookieStore：用于访问个人页面的Cookie
     */
    public CookieStore getLoginCookie(String email,String password) throws IOException, URISyntaxException {
        // 课堂派登陆地址
        String url = "https://www.ketangpai.com/UserApi/login";

        //请求参数
        List<NameValuePair> loginNV = new ArrayList<NameValuePair>();
        loginNV.add(new BasicNameValuePair("email",email));
        loginNV.add(new BasicNameValuePair("password",password));
        loginNV.add(new BasicNameValuePair("remember","0"));

        //构造请求资源地址
        URI uri = new URIBuilder(url).addParameters(loginNV).build();

        //创建一个HttpContext对象，用来保存Cookie
        HttpClientContext httpClientContext = HttpClientContext.create();

        //创建CookieStore实例
        CookieStore cookieStore = null;

        //构造自定义Header信息【可选】
//        List<Header> headerList = new ArrayList<Header>();
//        headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
//        headerList.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"));
//        headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
//        headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"));
//        headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
//        headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2,de;q=0.2"));

        //构造自定义的HttpClient对象
        HttpClient httpClient = HttpClients.createDefault();

        //构造请求对象
        HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(uri).setEntity(new UrlEncodedFormEntity(loginNV, "utf-8")).build();

        //执行请求，传入HttpContext，将会得到请求结果的信息
        HttpResponse response = httpClient.execute(httpUriRequest, httpClientContext);

        //从请求结果中获取Cookie，此时的Cookie已经带有登录信息了
        cookieStore = httpClientContext.getCookieStore();

        //-验证是否成功登陆
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        //+处理中文乱码
        String result = convertToString(is);
        //+解析Json信息
        JSONObject infoJson = JSON.parseObject(result);
        int status = infoJson.getInteger("status");
        if(status == 1) {
            System.out.println("登陆成功");
            // 保存cookie到本地(可选)
            String filePath = System.getProperty("user.dir") + "\\res\\cookie.txt";
            saveCookieStore(cookieStore,filePath);
            return cookieStore;
        }
        else{
            String info = infoJson.getString("info");
            System.out.println("失败："+ info);
        }
        return null;
    }

    /**
     * 保存Cookie到本地res文件夹中
     *
        #cookie格式
         cookie={
        'domain':'.baidu.com'#注意前面有个点
        'name':''
        'value':''
        'path':''
        #这些都可以在cookie里找到
         }
     */
    private static void saveCookieStore( CookieStore cookieStore, String savePath ) throws IOException {
        FileOutputStream fs = new FileOutputStream(savePath);
        OutputStreamWriter  write =  new OutputStreamWriter(fs,"UTF-8");
        BufferedWriter writer=new BufferedWriter(write);
        write.write("{"+"\r\n");
        write.write("'domain':'.ketangpai.com'"+"\r\n");
        write.write("'name':'"+cookieStore.getCookies().get(0).getName()+"'\r\n");
        write.write("'value':"+cookieStore.getCookies().get(0).getValue()+"\r\n");
        write.write("'path':'"+cookieStore.getCookies().get(0).getPath()+"'\r\n");
        write.write("'expires':"+cookieStore.getCookies().get(0).getExpiryDate()+"\r\n");
        write.write("}");
        write.close();
    }

    public static String doGet(String url,CookieStore cookieStore) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
//            httpClient = HttpClients.createDefault();
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)//设置Cookie
                    .build();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
//            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpclient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            //获得字节流内容
            InputStream is = entity.getContent();
            // 转成String
            result = convertToString(is);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 针对特殊情况下的HTTPS请求
     * @param url - 访问地址
     * @return str ：html源码
     */
    public String getResponse(String url) throws Exception {
        HttpRequest request = new HttpRequest(url);

        cn.edu.hfut.dmic.webcollector.net.HttpResponse response = null;
        int retry = 0;
        do
            try {
                response = request.response();
                break;
            } catch (Exception e) {
                retry++;
                Thread.sleep(retry * 2 * 1000);
            }
        while (retry < 5);

        if (retry >= 5) {
            response = new cn.edu.hfut.dmic.webcollector.net.HttpResponse(new URL(url));
            response.setNotFound(false);
            response.setRedirect(false);
            response.code(200);
            response.setHtml("");
        }

        String str = new String(response.content(), "utf-8");
        if (str.contains("�")) {
            str = new String(response.content(), "GB2312");
        }
        return str;
    }

    /**
     * 解决源码的中文乱码问题
     */
    public static String convertToString(InputStream is) throws IOException   {
        StringBuilder sb =new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try{
            while((size = is.read(bytes))>0){
                String str = new String(bytes,0,size,"UTF-8");
                sb.append(str);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 获取动态页面源码
     */
    public static String getPhjsHtml(String url) {
        Runtime rt = Runtime.getRuntime();
        String exec = System.getProperty("user.dir") + "\\phantomjs\\bin\\phantomjs.exe --cookies-file=\\res\\cookie.txt \\res\\phantom.js " + url;
        String anti_content = "";
        try {
            Process p = rt.exec(exec);
            InputStream is = p.getInputStream();
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                String str = new String(b, 0, n);
                str = str.replace("\r", "").replace("\n", "");
                if (StringUtils.isNotBlank(str)) {
                    out.append(str);
                }
            }
            anti_content = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anti_content;
    }
}