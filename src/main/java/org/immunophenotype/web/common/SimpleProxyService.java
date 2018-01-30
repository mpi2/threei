package org.immunophenotype.web.common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleProxyService extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		String paramValue = "param\\with\\backslash";
//		String yourURLStr = "http://host.com?param=" + java.net.URLEncoder.encode(paramValue, "UTF-8");
//		java.net.URL url = new java.net.URL(yourURLStr);
		String queryString=request.getQueryString();
		String urlString = "https://www.mousephenotype.org/data/charts?"+queryString;//request.getParameter("url");
		//response.setHeader("Content-Type", "text/plain");
		// response.setContentType(“text/xml”);
		urlString = urlString.replace(" ", "" + "");
		System.out.println("urlString="+urlString);
		URLConnection connection = new URL(urlString).openConnection();

		//String contentEncoding = connection.getHeaderField("Content-Encoding");// should
																				// be”Content-Encoding”,”ISO-8859-1″
		// if (contentType.startsWith(“text”)) {
		// String charset = “ISO-8859-1”;//this encoding is what the registry
		// uses so must be set here to override default;
		// System.out.println(“charset=”+charset);
		response.setHeader("Access-Control-Allow-Origin", "*");
		BufferedReader reader = null;
		PrintWriter writer = response.getWriter();
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			for (String line; (line = reader.readLine()) != null;) {
				writer.println(line);
				System.out.println("blah2"+line);
			}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
				}
		}

		reader.close();
		writer.flush();
		writer.close();

		// }

	} // end of main
}
