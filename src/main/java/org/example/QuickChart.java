package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

public class QuickChart {
    private Integer width;
    private Integer height;
    private Double devicePixelRatio;
    private String backgroundColor;
    private String key;
    private String version;
    private String config;
    private String scheme;
    private String host;
    private Integer port;

    public QuickChart() {
        this("https", "quickchart.io", 443);
    }

    public QuickChart(String scheme, String host, Integer port) {
        this.width = 500;
        this.height = 300;
        this.devicePixelRatio = 1.0;
        this.backgroundColor = "transparent";
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getDevicePixelRatio() {
        return this.devicePixelRatio;
    }

    public void setDevicePixelRatio(double devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getUrl() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(this.scheme);
        builder.setHost(this.host);
        if (this.port != 80 && this.port != 443) {
            builder.setPort(this.port);
        }

        builder.setPath("/chart");
        builder.addParameter("w", this.width.toString());
        builder.addParameter("h", this.height.toString());
        builder.addParameter("devicePixelRatio", this.devicePixelRatio.toString());
        if (!this.backgroundColor.equals("transparent")) {
            builder.addParameter("bkg", this.backgroundColor);
        }

        builder.addParameter("c", this.config);
        if (this.key != null && !this.key.isEmpty()) {
            builder.addParameter("key", this.key);
        }

        if (this.version != null && !this.version.isEmpty()) {
            builder.addParameter("v", this.version);
        }

        return builder.toString();
    }

    private String getPostJson() {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("width", this.width.toString());
        jsonBuilder.put("height", this.height.toString());
        jsonBuilder.put("devicePixelRatio", this.devicePixelRatio.toString());
        if (!this.backgroundColor.equals("transparent")) {
            jsonBuilder.put("backgroundColor", this.backgroundColor);
        }

        jsonBuilder.put("chart", this.config);
        if (this.key != null && !this.key.isEmpty()) {
            jsonBuilder.put("key", this.key);
        }

        if (this.version != null && !this.version.isEmpty()) {
            jsonBuilder.put("version", this.version);
        }

        return jsonBuilder.toString();
    }

    private HttpEntity executePost(String path) throws IOException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(this.scheme);
        uriBuilder.setHost(this.host);
        if (this.port != 80 && this.port != 443) {
            uriBuilder.setPort(this.port);
        }

        uriBuilder.setPath(path);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uriBuilder.toString());
        StringEntity entity = new StringEntity(this.getPostJson(), "utf-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String detailedError = "";
        if (response.containsHeader("x-quickchart-error")) {
            detailedError = "\n\n" + response.getFirstHeader("x-quickchart-error").getValue();
        }

        if (statusCode != 200) {
            throw new RuntimeException("Received invalid status code " + statusCode + " from API endpoint" + detailedError);
        } else {
            HttpEntity ret = response.getEntity();
            return ret;
        }
    }

    public String getShortUrl() {
        try {
            HttpEntity entity = this.executePost("/chart/create");
            String rawResponse = EntityUtils.toString(entity);
            JSONTokener tokener = new JSONTokener(rawResponse);
            JSONObject jsonResponse = new JSONObject(tokener);
            return jsonResponse.getString("url");
        } catch (Exception var5) {
            return null;
        }
    }

    public byte[] toByteArray() {
        try {
            HttpEntity entity = this.executePost("/chart");
            return EntityUtils.toByteArray(entity);
        } catch (IOException var2) {
            return null;
        }
    }

    public String toDataUrl() {
        try {
            HttpEntity entity = this.executePost("/chart");
            return "data:image/png;base64," + Base64.getEncoder().encode(EntityUtils.toByteArray(entity));
        } catch (IOException var2) {
            return null;
        }
    }

    public void toFile(String filePath) throws IOException {
        HttpEntity entity = this.executePost("/chart");
        BufferedHttpEntity entityBuffer = new BufferedHttpEntity(entity);
        FileOutputStream os = new FileOutputStream(new File(filePath));
        entityBuffer.writeTo(os);
    }
}