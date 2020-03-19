package cn.edu.scau.acm.acmer.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpClient {

	Logger log = LoggerFactory.getLogger(this.getClass());

	HttpClientContext context = HttpClientContext.create();
	
	public static CloseableHttpClient createHttpClient() {
		CloseableHttpClient httpclient = null;
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(
					new File(BaseHttpClient.class.getResource("/truststore").getFile()),
					"acmTrust".toCharArray(), new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
			        sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(5000)
					.setConnectionRequestTimeout(5000)
					.setSocketTimeout(20000)
//					.setProxy(new HttpHost("localhost", 8888))
					.build();
			httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf)
					.setDefaultRequestConfig(requestConfig).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpclient;
	}
	
	public String get(String url) throws Exception {
		return get(url, "utf-8");
	}

	public String get(String url, String charset) throws Exception {
		CloseableHttpClient client = createHttpClient();
		HttpGet httpGet = new HttpGet(url);
		int retry = 5;
		while (retry > 0) {
			try {
				CloseableHttpResponse response = client.execute(httpGet, context);
				return getResponseContent(url, charset, response);
			} catch (IOException | ProtocolException e) {
				e.printStackTrace();
				retry--;
				log.error("连接出错，第{}次重试，{}", 5-retry, e.getMessage());
			}
		}
		client.close();
		throw new Exception("连接服务器错误");

	}

	public String post(String url, List<NameValuePair> params) throws Exception {
		return post(url, "utf-8", params, null);
	}

	public String post(String url, List<NameValuePair> params, List<Header> headers) throws Exception {
		return post(url, "utf-8", params, headers);
	}

	public String post(String url, String charset, List<NameValuePair> params, List<Header> headers) throws Exception {
		CloseableHttpClient client = createHttpClient();
		HttpPost httpPost = new HttpPost(url);
		int retry = 3;
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		if(headers != null) {
			for (Header header : headers) {
				httpPost.setHeader(header);
			}
		}
		while (retry > 0) {
			try {
				CloseableHttpResponse response = client.execute(httpPost, context);
				return getResponseContent(url, charset, response);
			} catch (IOException e) {
				retry--;
				log.error("连接出错，第{}次重试，{}", 5-retry, e.getMessage());
			}
		}
		throw new Exception("连接服务器错误");
	}

	public String postJson(String url, List<NameValuePair> params) throws Exception {
		return postJson(url, "utf-8", params, null);
	}

	public String postJson(String url, List<NameValuePair> params, List<Header> headers) throws Exception {
		return postJson(url, "utf-8", params, headers);
	}

	public String postJson(String url, String charset, List<NameValuePair> params, List<Header> headers) throws Exception {
		CloseableHttpClient client = createHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = new JSONObject();
		for (NameValuePair param : params) {
			jsonObject.put(param.getName(), param.getValue());
		}
		httpPost.setEntity(new StringEntity(jsonObject.toJSONString(), ContentType.APPLICATION_JSON));
		if(headers != null) {
			for (Header header : headers) {
				httpPost.setHeader(header);
			}
		}
		int retry = 3;
		while (retry > 0) {
			try {
				CloseableHttpResponse response = client.execute(httpPost, context);
				return getResponseContent(url, charset, response);
			} catch (IOException e) {
				retry--;
				log.error("连接出错，第{}次重试，{}", 5-retry, e.getMessage());
			}
		}
		throw new Exception("连接服务器错误");
	}

	private String getResponseContent(String url, String charset, CloseableHttpResponse response) throws IOException, ProtocolException {
		try {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 400) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, charset) : null;
            } else {
                throw new ProtocolException("Unexpected response status: " + status + "(" + url + ")");
            }
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	public String getCookie(String name) {
		for (Cookie cookie : context.getCookieStore().getCookies()) {
			if (cookie.getName().equals(name))
				return cookie.getValue();
		}
		return null;
	}
}
