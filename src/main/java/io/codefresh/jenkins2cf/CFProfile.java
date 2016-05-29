/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.codefresh.jenkins2cf;

import hudson.util.Secret;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author antweiss
 */
public class CFProfile {
    private final String cfUser;
    private final Secret cfToken;
    private CFService service
    //private List<CFService> services;

    public CFProfile(String cfUser, Secret cfToken, String repoName) {
        this.cfUser = cfUser;
        this.cfToken = cfToken;
        this.service = getService(repoName);
    }
    
    public String getService(String repoName){
        
        String serviceId = "";
        
        
        String httpsURL = "https://g.codefresh.io/api/services/" + cfUser + repoName ;
        HttpsURLConnection conn = new CFApi(this.cfToken, httpsURL).getConnection();
        
                
    outs.write("");
    outs.flush();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String output;
            while ((inputLine = in.readLine()) != null) {
              
                    output += inputLine;
            }
            outs.close();
            in.close();
    }
            
    
                        
    return output;
    
}
