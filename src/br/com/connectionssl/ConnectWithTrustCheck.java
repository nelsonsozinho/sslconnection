package br.com.connectionssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConnectWithTrustCheck {

	public static void main(String... strings) {
		try {
			testConnection();
		} catch (KeyManagementException e) {
			
			e.printStackTrace();
		} catch (KeyStoreException e) {
			
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		} catch (CertificateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static void testConnection() throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			KeyManagementException {
		File f = new File("arquivo/test.d");
		if (!f.exists())
			return;

		InputStream stream = new FileInputStream(new File("arquivo/test.d"));
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(stream, "nelsonsozinho".toCharArray());
		stream.close();

		javax.net.ssl.TrustManagerFactory factory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
		factory.init(keyStore);
		X509TrustManager defaultTrustManager = (X509TrustManager) factory.getTrustManagers()[0];
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { defaultTrustManager }, null);
		SSLSocketFactory sslSocketFactory = context.getSocketFactory();
		
		URL url = new URL("https://urini.fucapi.br/viewspo");
		URLConnection urlConnection = url.openConnection();
		((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
		urlConnection.connect();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();
		
	}

}
