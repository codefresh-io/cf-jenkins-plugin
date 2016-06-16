/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.codefresh.jenkins2cf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hudson.util.Secret;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.io.IOUtils;

/**
 *
 * @author antweiss
 */
public class CFApi {
    
    private SSLSocketFactory sf = null;
    private String httpsUrl = "https://g.codefresh.io/api";
    private Secret cfToken;
    private TrustManager[] trustAllCerts;
    
    
    public CFApi(Secret cfToken) throws MalformedURLException, IOException {
    
        this.cfToken = cfToken;
        trustAllCerts = new TrustManager[]{new X509TrustManager(){
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
    
    }
    
 
   
    public CFService[] getServices() throws MalformedURLException, IOException
    {
        String serviceUrl = httpsUrl + "/services";
        URL serviceEp = new URL(serviceUrl);
        HttpsURLConnection conn = (HttpsURLConnection) serviceEp.openConnection();
        CFService[] services = new CFService[10];
        conn.setRequestProperty("x-access-token", cfToken.getPlainText());
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setFollowRedirects(true);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        String jsonString = IOUtils.toString(is);
        JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
        //TODO - parse to a list of services
        //String id = obj.get("_id").getAsString();
        return services;
    }
}
