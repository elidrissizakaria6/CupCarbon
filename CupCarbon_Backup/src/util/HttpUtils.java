package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class HttpUtils {

	public static String getResponseBody(HttpResponse response)
			throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	public static boolean isOKResponse(HttpResponse response) {
		if (response == null) {
			return false;
		}
		int responseCode = response.getStatusLine().getStatusCode();
		return responseCode == HttpStatus.SC_OK;
	}
}
