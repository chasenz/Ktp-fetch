package com.ktp.util;

import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    public DefaultHttpClient httpclient = new DefaultHttpClient();

    public String get(String url) {
        return get(url, null, null);
    }

    public String get(String url, Map<String, String> headers) {
        return get(url, headers, null);
    }

    public String get(String url, String charset) {
        return get(url, null, charset);
    }

    public String get(String url, Map<String, String> headers, String charset) {
        return get(url, headers, charset, false);
    }

    public String get(String url, boolean isSSL) {
        return get(url, null, "utf-8", isSSL);
    }

    public String get(String url, Map<String, String> headers, boolean isSSL) {
        return get(url, headers, "utf-8", isSSL);
    }

    public String get(String url, String charset, boolean isSSL) {
        return get(url, null, charset, isSSL);
    }

    public String get(String url, Map<String, String> headers, String charset, boolean isSSL) {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        if (isSSL) {
            httpClient = getNewHttpClient();
        }
        String result = "";
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        HttpGet get = new HttpGet(url);

        if (headers != null) {
            for (String key : headers.keySet()) {
                get.setHeader(key, (String) headers.get(key));
            }
        }

        int i = 0;
        do
            try {
                org.apache.http.HttpResponse response = httpClient.execute(get);
                if ((response != null) && (response.getStatusLine().getStatusCode() == 200)) {
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity, charset);
                    break;
                }
                System.out.println("请求失败，正在第" + i + "次重试...  " + url);
                TimeUnit.SECONDS.sleep(2L);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                System.out.println("请求失败，正在第" + i + "次重试...  " + url);
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("请求失败，正在第" + i + "次重试... " + url);
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        while (i++ < 5);

        return result;
    }

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

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
        }
        return new DefaultHttpClient();
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.mshw.net/2016/yule_1123/64335.html";
        new HttpClientUtil().getResponse(url);
    }
}