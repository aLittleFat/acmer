package cn.edu.scau.acm.acmer.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class VjudgeClient extends BaseHttpClient {

	public boolean checkLogInStatus() throws IOException, ProtocolException {
		return "true".equals(get("https://vjudge.net/user/checkLogInStatus"));
	}
	
	public void login(String username, String password) throws Exception {
		CloseableHttpClient client = createHttpClient();
		try {
			if (!checkLogInStatus()) {
				HttpPost httpPost = new HttpPost("https://vjudge.net/user/login");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				client.execute(httpPost, context).close();
			}
			if (!checkLogInStatus())
				throw new Exception();
		} catch (UnknownHostException e) {
			throw new Exception("无法访问vjudge.net，请稍后再试");
		} catch (Exception e) {
			throw new Exception("VJ登录失败，请联系管理员");
		}
	}

//	public void logout() {
//		try {
//			HttpPost httpPost = new HttpPost("https://vjudge.net/user/logout");
//			client.execute(httpPost, context).close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
