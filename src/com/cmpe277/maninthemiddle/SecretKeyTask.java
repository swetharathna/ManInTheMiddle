package com.cmpe277.maninthemiddle;

import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class SecretKeyTask extends AsyncTask<Void, Void, Object> {

	@Override
	protected void onPreExecute() {
		assert (null != MainActivity.pb);
		if (null != MainActivity.pb) {
			MainActivity.pb.setVisibility(ProgressBar.VISIBLE);
		}
	}

	@Override
	protected Object doInBackground(Void... params) {

		Object result = null;

		try {

			byte[] secret = null;

			TrustManager tm[] = { new PublicKeyManager() };
			assert (null != tm);

			SSLContext context = SSLContext.getInstance("TLS");
			assert (null != context);
			context.init(null, tm, null);

			URL url = new URL(MainActivity.url_value);
			assert (null != url);

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			assert (null != connection);

			connection.setSSLSocketFactory(context.getSocketFactory());
			InputStreamReader instream = new InputStreamReader(connection.getInputStream());
			assert (null != instream);

			StreamTokenizer tokenizer = new StreamTokenizer(instream);
			assert (null != tokenizer);

			secret = new byte[16];
			assert (null != secret);

			int idx = 0, token;
			while (idx < secret.length) {
				token = tokenizer.nextToken();
				if (token == StreamTokenizer.TT_EOF)
					break;
				if (token != StreamTokenizer.TT_NUMBER)
					continue;

				secret[idx++] = (byte) tokenizer.nval;
			}
			result = (Object) secret;
			System.out.println(secret);

		} 
		catch (Exception ex) {
			result = (Object) ex;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Object result) {

		assert (null != MainActivity.pb);
		if (null != MainActivity.pb) {
			MainActivity.pb.setVisibility(ProgressBar.INVISIBLE);
		}
		assert (null != result);
		if (null == result)
			return;

		assert (result instanceof Exception || result instanceof byte[]);
		if (!(result instanceof Exception || result instanceof byte[]))
			return;

		if (result instanceof Exception) {
			ExitWithException((Exception) result);
			return;
		}

		ExitWithSecret((byte[]) result);
	
	}

	protected void ExitWithException(Exception ex) {

		assert (null != ex);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context);
		builder.setMessage(ex.toString()).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	protected void ExitWithSecret(byte[] secret) {

		assert (null != secret);

		StringBuilder sb = new StringBuilder(secret.length * 3 + 1);
		assert (null != sb);

		for (int i = 0; i < secret.length; i++) {
			sb.append(String.format("%02X ", secret[i]));
			secret[i] = 0;
		}
		
		String url = MainActivity.url_value;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		MainActivity.context.startActivity(i);

	}
}
