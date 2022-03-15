package com.example.oauth2apiserver.utils;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Slf4j
@Component
public class HttpClientUtils {
    @Autowired
    private ObjectMapper objectMapper;

    public JSONObject httpGetClientParameter(String sendUrl) {
        JSONObject jsonObj = null;

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet httpGet = new HttpGet(sendUrl);
        try {


            HttpResponse HttpResponse = httpClient.execute(httpGet);

            if (HttpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(HttpResponse.getEntity().getContent()));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }

                //응답결과 JSON Parsing
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObj;

    }


    public String httpGetClientString(String sendUrl) {
        String resultString = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(sendUrl);


        HttpHeaders headers = new HttpHeaders();
        httpGet.addHeader("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpGet.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        try {


            HttpResponse HttpResponse = httpClient.execute(httpGet);

            if (HttpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(HttpResponse.getEntity().getContent()));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }
                resultString = outResult.toString();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;

    }

    public JSONObject httpPostClientParameter(String sendUrl, List<NameValuePair> postParameters) {
        JSONObject jsonObj = null;

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(sendUrl);
        try {

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            HttpResponse HttpResponse = httpClient.execute(httpPost);

            if (HttpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(HttpResponse.getEntity().getContent()));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }

                //응답결과 JSON Parsing
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObj;

    }

    public JSONObject httpPostClientJson(String sendUrl, JSONObject sendData) {
        JSONObject jsonObj = null;

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(sendUrl);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(sendData.toString(), "UTF-8");
            httpPost.setHeader("Accept-Charset", "UTF-8");
            httpPost.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(stringEntity);

            HttpResponse HttpResponse = httpClient.execute(httpPost);

            if (HttpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(HttpResponse.getEntity().getContent()));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }

                //응답결과 JSON Parsing
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObj;

    }

    public JSONObject httpPostClientForm(String sendUrl, JSONObject sendData) {
        JSONObject jsonObj = null;

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(sendUrl);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(sendData.toString(), "UTF-8");
            httpPost.setHeader("Accept-Charset", "UTF-8");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);

            HttpResponse HttpResponse = httpClient.execute(httpPost);

            if (HttpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(HttpResponse.getEntity().getContent()));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }

                log.info("요청 결과 [" + outResult.toString() + "]");
                //응답결과 JSON Parsing
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObj;

    }

    public JSONObject httpsPostClientJson(String sendUrl, String sendData) {
        JSONObject jsonObj = null;
        URL url = null;
        HttpsURLConnection conn = null;
        OutputStream os = null;
        try {
            url = new URL(sendUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            os = conn.getOutputStream();
            os.write(sendData.getBytes("UTF-8"));
            os.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine = null;
            StringBuffer outResult = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            //응답결과 JSON Parsing
            JSONParser jsonParser = new JSONParser();
            jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            in.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return jsonObj;
    }

    public JSONObject httpsGetAsanJson(final String url, final String json, final String reqAddr) {

        JSONObject jsonObj = null;

        StringEntity userEntity = new StringEntity(json, "UTF-8");
        HttpPost httpPost = new HttpPost(reqAddr + url);
        httpPost.setEntity(userEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
        CloseableHttpResponse response = null;

        String body = "";

        try {
            response = getHttpClient().execute(httpPost);
            body = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ConnectionClosedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int responseCode = response.getStatusLine().getStatusCode();

        log.info("response responseCode :{}", responseCode);
        try {

            if (responseCode == 200) {
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(body);
            } else {
                JSONObject tempObj = new JSONObject();
                JSONObject tempBody = new JSONObject();
                JsonResponse jsonResponse;
                jsonResponse = objectMapper.readValue(body, JsonResponse.class);

                List<String> errors = new ArrayList<>();
                log.info("response body :{}", jsonResponse.getErrors());
                errors = jsonResponse.getErrors();
                String errMsg = "";
                for (String err : errors) {
                    errMsg += err;
                }
                tempObj.put("bodyType", "OBJECT");
                tempBody.put("returnCd", "0001");
                tempBody.put("returnMsg", errMsg);
                tempObj.put("body", tempBody);
                jsonObj = tempObj;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            response.close();
//        }


        return jsonObj;
    }

    private CloseableHttpClient getHttpClient()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        if (true) {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
//                .setDefaultConnectionConfig(connectionConfig)
//                    .evictIdleConnections(3000L, TimeUnit.MILLISECONDS)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .setMaxConnTotal(500)
                    .setMaxConnPerRoute(50)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                    .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                    .build();
            return createSSLClientBuilder().build();
        } else {
            return HttpClients.createDefault();
        }
    }

    private HttpClientBuilder createSSLClientBuilder()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        HttpClientBuilder clientBuilder = HttpClients.custom();

        //인증서 유효성체크 비활성화.
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustStrategy() {
                    public boolean isTrusted(X509Certificate[] paramCertificate, String paramString) throws CertificateException {
                        return true;
                    }
                }).build();

        clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).setSSLContext(sslcontext);
        return clientBuilder;
    }

    public JSONObject httpsGetSKPayJson(String sendUrl, String mxId, String basicAuthKey) {
        JSONObject jsonObj = null;

        URL url = null;
        HttpsURLConnection con = null;
        int responseCode = 0;

        String encoding = null;
        try {
            encoding = Base64.getEncoder().encodeToString((mxId + ":" + basicAuthKey).getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            url = new URL(sendUrl);
            con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + encoding);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            responseCode = con.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String inputLine = null;
                StringBuffer outResult = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    outResult.append(inputLine);
                }
                //응답결과 JSON Parsing
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
        }
        return jsonObj;
    }

    public JSONObject httpsPrivateSSL(String sendUrl, String apiKey, String sendData) {
        JSONObject jsonObj = null;
        URL url = null;
        HttpsURLConnection conn = null;
        OutputStream os = null;
        try {
            url = new URL(sendUrl);
            ignoreSSl();
            conn = (HttpsURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            conn.setRequestProperty("apiKey", apiKey);
            conn.setRequestProperty("reqKey", "");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            os = conn.getOutputStream();
            os.write(sendData.getBytes("UTF-8"));
            os.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine = null;
            StringBuffer outResult = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            //응답결과 JSON Parsing
            JSONParser jsonParser = new JSONParser();
            jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            in.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return jsonObj;
    }

    public JSONObject httpsPostClientForm(String sendUrl, JSONObject sendData) {
        JSONObject jsonObj = null;
        URL url = null;
        HttpsURLConnection conn = null;
        OutputStream os = null;
        try {
            url = new URL(sendUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            os = conn.getOutputStream();
            os.write(sendData.toString().getBytes("UTF-8"));
            os.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {

            InputStream inputStream;
            int status = conn.getResponseCode();

            if (status != HttpsURLConnection.HTTP_OK) {
                inputStream = conn.getErrorStream();
            } else {
                inputStream = conn.getInputStream();
            }
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String inputLine = null;
            StringBuffer outResult = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            log.info("MediAge 요청 결과 [" + outResult.toString() + "]");

            //응답결과 JSON Parsing
            JSONParser jsonParser = new JSONParser();
            jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            in.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return jsonObj;
    }

    public JSONObject httpsPostBnkForm(String sendUrl, JSONObject sendData) {
        JSONObject jsonObj = null;
        URL url = null;
        HttpsURLConnection conn = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        try {
            url = new URL(sendUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("params", sendData.toString()));

            os = conn.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine = null;
            StringBuffer outResult = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            //응답결과 JSON Parsing
            JSONParser jsonParser = new JSONParser();
            jsonObj = (JSONObject) jsonParser.parse(outResult.toString());

            in.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return jsonObj;
    }

    private String getValueByKey(List<NameValuePair> _list, String key) {
        for (NameValuePair nvPair : _list) {
            if (nvPair.getName().equals(key)) {
                return nvPair.getValue().toString();
            }
        }
        return null;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private static void ignoreSSl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
//                HostnameVerifier hv =
//                        HttpsURLConnection.getDefaultHostnameVerifier();
//                return hv.verify(hostname, session);
                return true;
            }
        };
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }


}
