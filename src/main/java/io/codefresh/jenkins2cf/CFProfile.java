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
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author antweiss
 */
public class CFProfile {
    private final String cfUser;
    private final Secret cfToken;
    private CFService service;
    private CFApi api;
    //private List<CFService> services;

    public CFProfile(String cfUser, Secret cfToken, String repoName) throws IOException {
        this.cfUser = cfUser;
        this.cfToken = cfToken;
        this.service = new CFService(repoName,getServiceId(repoName));
    }
    
    public String getServiceId(String repoName) throws IOException{
             
        
        String httpsURL = "https://g.codefresh.io/api/services/" + cfUser + "/" + repoName ;
     //   CFApi api =  new CFApi(this.cfToken, httpsURL);
     //   HttpsURLConnection conn = api.getConnection();
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }
        HttpsURLConnection conn = null;
        URL myURL = new URL(httpsURL);
        conn = (HttpsURLConnection) myURL.openConnection();
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
        String id = obj.get("_id").getAsString();
        return id;
        
        
    }
    
    public CFService getService()
    {
        return service;
    }
    
}
