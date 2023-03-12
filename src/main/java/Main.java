import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.util.Arrays;

public class Main {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=v4CL85iEbLX7p3xzLzuobt6FUOOVTAlCtX90hAuv");

        CloseableHttpResponse response = httpClient.execute(request);

        JsonToJavaObjects jsonToJavaObjects = mapper.readValue(response.getEntity().getContent(), JsonToJavaObjects.class);
        System.out.println(jsonToJavaObjects);

        HttpGet request2 = new HttpGet(jsonToJavaObjects.getUrl());

        CloseableHttpResponse response2 = httpClient.execute(request2);
        String[] arrayOfUrl = jsonToJavaObjects.getUrl().split("/");
        String nameOfFile = arrayOfUrl[arrayOfUrl.length - 1];
        System.out.println(nameOfFile);

        HttpEntity entity = response2.getEntity();
        FileOutputStream fos = new FileOutputStream(nameOfFile);
        entity.writeTo(fos);
        fos.close();

    }
}
