/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.codefresh.jenkins2cf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hudson.util.Secret;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author antweiss
 */
public class CFService {
    private String name;
    private String id;

    public CFService(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
    public String newBuild(Secret token) throws IOException
    {
        String httpsURL = "https://g.codefresh.io/api/builds" + id ;
        CFApi api =  new CFApi(token, httpsURL);
        HttpsURLConnection conn = api.getConnection();
        conn.setRequestMethod("POST");
    
        OutputStreamWriter outs = new OutputStreamWriter(conn.getOutputStream());
        outs.write("");
        outs.flush();

        InputStream is = conn.getInputStream();
        return is.toString();
    }
    
}
