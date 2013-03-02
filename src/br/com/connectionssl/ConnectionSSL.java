package br.com.connectionssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConnectionSSL {

	public static void main(String... strings) throws IOException {
		try {
			connection();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Without ssl check
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void connection() throws IOException, NoSuchAlgorithmException, KeyManagementException { 
		//Cadeia de confianca que nao valida o certificado
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,	String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,	String authType) {
			}
		} };
		
		// Instala a cadeia de confiacan
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// sempre recorna true 
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		URL url = new URL("https://host_withssl/app");
		URLConnection con = url.openConnection();
		Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			System.out.print((char) ch);
		}

	}

	public static void obterKeyStore() throws KeyStoreException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

	}

	public static void basicConnection() throws IOException {
		URL url = null;
		url = new URL("https://host_withssl/app");

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory
				.getDefault();
		SSLSocket socket = (SSLSocket) socketFactory.createSocket();
		PrintWriter writter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		// writter.println("POST" +"test" +"HTTP/1.1");
		writter.print("POST " + "teste" + " HTTP/1.1\r\n\r\n");
		writter.println();
		writter.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		writter.close();
		in.close();
	}

}
