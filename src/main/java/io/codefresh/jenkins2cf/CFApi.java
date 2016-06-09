/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.codefresh.jenkins2cf;

import hudson.util.Secret;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author antweiss
 */
public class CFApi {
    
    private SSLSocketFactory sf = null;
    private HttpsURLConnection conn;
    private URL myURL;
    
    public CFApi(Secret cfToken, String HttpsURL) throws MalformedURLException, IOException {
    
    
         TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            this.sf = sc.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(this.sf);
        } catch (Exception e) {
            ;
        }

        myURL = new URL(HttpsURL);
        conn = (HttpsURLConnection) myURL.openConnection();
        conn.setRequestProperty("x-access-token", cfToken.getPlainText());
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setFollowRedirects(true);
        conn.setInstanceFollowRedirects(true);
    
    }
    
    public HttpsURLConnection getConnection()
    {
        return conn;
    }

}
