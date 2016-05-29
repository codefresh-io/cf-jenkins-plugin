package io.codefresh.jenkins2cf;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import hudson.util.Secret;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import org.restlet.Client;
import org.restlet.data.Protocol;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import  org.apache.commons.httpclient.protocol.*;
import javax.net.ssl.*;
import java.net.URL;
import java.io.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
//import org.restlet.ext.json.JsonRepresentation;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link CodefreshBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked.
 *
 * @author Kohsuke Kawaguchi
 */
public class CodefreshBuilder extends Builder {

    private final boolean launch;
    private final String service;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public CodefreshBuilder(String service, Boolean launch) {
        this.launch = launch;
        this.service = service;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public boolean getLaunch() {
        return launch;
    }

    public String getService() {
        return service;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException {

    // temporarily ignore cert check
    // TODO : trust specifically Codefresh's cert
//    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
//        public X509Certificate[] getAcceptedIssuers(){return null;}
//        public void checkClientTrusted(X509Certificate[] certs, String authType){}
//        public void checkServerTrusted(X509Certificate[] certs, String authType){}
//    }};
//
//    // Install the all-trusting trust manager
//    try {
//        SSLContext sc = SSLContext.getInstance("TLS");
//        sc.init(null, trustAllCerts, new SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//    } catch (Exception e) {
//        ;
//    }
        CFProfile = new CFProfile(token);
  }

  //  con.setRequestProperty("x-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NmVlOGYwY2FiNzkwYjA2MDAyODEzYzciLCJhY2NvdW50SWQiOiI1NmVlOGYwY2FiNzkwYjA2MDAyODEzYzgiLCJpYXQiOjE0NjQyNzk1OTgsImV4cCI6MTQ2Njg3MTU5OH0.gTI_1PDjxa7VO3Aq1Ta5fGvElmETwpyPnuvUCmC4-qg");
    
//    InputStreamReader isr = new InputStreamReader(ins);
//    BufferedReader in = new BufferedReader(isr);




        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link CodefreshBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private String cfUser;
        private Secret cfToken;

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
//        public FormValidation doCheckUser(@QueryParameter String value)
//                throws IOException, ServletException {
//            if (value.length() == 0)
//                return FormValidation.error("Please set a name");
//            if (value.length() < 4)
//                return FormValidation.warning("Isn't the name too short?");
//            return FormValidation.ok();
//        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Define Codefresh Integration";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            cfUser = formData.getString("cfUser");
            cfToken = Secret.fromString(formData.getString("cfToken"));

            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }

        public List<String> getServices(){

        }
        public String getCfUser() {
            return cfUser;
        }

        public Secret getCfToken() {
            return cfToken;
        }
        public ListBoxModel doFillServiceNameItems() {
            ListBoxModel items = new ListBoxModel();
            for (String service : getServices()) {
                items.add(service);
            }
            return items;
        }

    }
}
