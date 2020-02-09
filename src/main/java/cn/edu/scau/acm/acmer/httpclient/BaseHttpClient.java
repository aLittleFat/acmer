package cn.edu.scau.acm.acmer.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BaseHttpClient {
	HttpClientContext context = HttpClientContext.create();
	
	public static CloseableHttpClient createHttpClient() {
		CloseableHttpClient httpclient = null;
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(
					new File(BaseHttpClient.class.getResource("/truststore").getFile()),
					"acmTrust".toCharArray(), new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
			        sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpclient;
	}
	
	public String get(String url) throws ProtocolException, IOException {
		return get(url, "utf-8");
	}

	public String get(String url, String charset) throws ProtocolException, IOException {
		CloseableHttpClient client = createHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(httpGet, context);
			return getResponseContent(url, charset, response);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String post(String url, List<NameValuePair> params) throws IOException, ProtocolException {
		return post(url, "utf-8", params);
	}

	public String post(String url, String charset, List<NameValuePair> params) throws IOException, ProtocolException {
		CloseableHttpClient client = createHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = client.execute(httpPost, context);
			return getResponseContent(url, charset, response);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getResponseContent(String url, String charset, CloseableHttpResponse response) throws IOException, ProtocolException {
		try {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300 || status == 302) {
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
