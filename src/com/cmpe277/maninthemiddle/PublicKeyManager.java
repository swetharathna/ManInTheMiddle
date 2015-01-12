package com.cmpe277.maninthemiddle;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public final class PublicKeyManager implements X509TrustManager {

	private static String PUB_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a028201010093f2e2d804a67c0e4f7807f7517e35da465259fd9c6d3b73ef03480279074d31edf567b8781fef54710810a7ca12f58fb0603b7aed6a43f0dba3ad1a568eeee7ad3f8086ddc0e0a226e535b354a7ffe67a82ce5f2ffda1c134291af259960a4746799268d5e5385ac5685ccd18c3c25a76193fa8b2cdd0083a29f2bd7be1d1e5de1741226c6dae182739aca7a8de96a3e44ce0fd6fc5ed001ecb8767c1e8542bdade1b8e3e49dcfcfe234903f35e3ebc1346bd2ca2ce8e474322604473e6d985f33748c7dbe0f3da9bfc830e23a3231eff57e3889bcd366ea5f2efe74f3cb6d6ff6b012dd4eceb62d2ad7efc822a63dc348805e38de60b7c4c46cc850477e0bb0203010001";
	
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

		assert (chain != null);
		if (chain == null) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate array is null");
		}

		assert (chain.length > 0);
		if (!(chain.length > 0)) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate is empty");
		}

		/*assert (null != authType && authType.equalsIgnoreCase("RSA"));
		if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
			throw new CertificateException(
					"checkServerTrusted: AuthType is not RSA");
		}*/

		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance("X509");
			tmf.init((KeyStore) null);

			for (TrustManager trustManager : tmf.getTrustManagers()) {
				((X509TrustManager) trustManager).checkServerTrusted(
						chain, authType);
			}

		} catch (Exception e) {
			throw new CertificateException(e);
		}

		RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
		String encoded = new BigInteger(1, pubkey.getEncoded())
				.toString(16);
		
		final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
		Log.d ("received publicKey-", encoded);

		assert(expected);
		if (!expected) {
			
			//System.out.println(encoded);
			throw new CertificateException(
					"checkServerTrusted: Expected public key: " + PUB_KEY
							+ ", got public key:" + encoded);
			
		}
	}

	public void checkClientTrusted(X509Certificate[] xcs, String string) {

	}

	public X509Certificate[] getAcceptedIssuers() {

		return null;
	}
}
