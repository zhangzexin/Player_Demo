package voole.example.com.myapplication.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public class HttpHelp {

	public String Get(String url) throws IOException {
		URL mUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) mUrl
				.openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");
		connection.connect();
		int responseCode = connection.getResponseCode();
		if (responseCode<=299&&200 <=responseCode) {
			InputStream inputStream = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputStream));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				// response = br.readLine();
				response = response + readLine;
			}
			inputStream.close();
			br.close();
			connection.disconnect();
			return response;
		} else {
			throw new IOException("Unexpected code "
					+ connection.getResponseCode());
		}
	}
	public String Post(String url,Map<String,String> data,String ecode) throws IOException {
		byte[] mData = getRequestData(data,ecode).toString().getBytes();
		URL mUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
		connection.setConnectTimeout(3000);
		connection.setReadTimeout(5000);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(true);
//		connection.setRequestProperty("Content-Type","application/text/html;charset=utf-8");
//		connection.setRequestProperty("Content-Length",String.valueOf(mData.length));
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(mData);
		int responseCode = connection.getResponseCode();
		if(responseCode == connection.HTTP_OK){
			InputStream inputStream = connection.getInputStream();
			return  dealResponseResult(inputStream);
		}

		return "-1";
	}
	public static StringBuffer getRequestData(Map<String, String> data, String encode) {
		StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
		try {
			for(Map.Entry<String, String> entry : data.entrySet()) {
				stringBuffer.append(entry.getKey())
						.append("=")
						.append(URLEncoder.encode(entry.getValue(), encode))
						.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}
	public static String dealResponseResult(InputStream inputStream) {
		String resultData = null;      //存储处理结果
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultData = new String(byteArrayOutputStream.toByteArray());
		return resultData;
	}
}
