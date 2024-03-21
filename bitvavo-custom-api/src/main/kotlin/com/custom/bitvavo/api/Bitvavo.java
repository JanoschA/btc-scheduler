package com.custom.bitvavo.api;

import java.net.*;
import javax.net.ssl.*;
import java.io.*;

import org.json.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import org.apache.commons.io.IOUtils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Bitvavo {
    String apiKey;
    String apiSecret;
    String restUrl;
    boolean debugging;
    int window;
    volatile int rateLimitRemaining = 1000;
    volatile long rateLimitReset = 0;
    volatile boolean rateLimitThreadStarted = false;

    public Bitvavo(String apiKey, String apiSecret, String restUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.restUrl = restUrl;
        this.debugging = false;
    }

    /**
     * Places an order on the exchange
     * @param market The market for which the order should be created
     * @param side is this a buy or sell order
     * @param orderType is this a limit or market order
     * @param body optional body parameters: limit:(amount, price, postOnly), market:(amount, amountQuote, disableMarketProtection)
     *                                       stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
     *                                       stopLossLimit/takeProfitLimit:(amount, price, postOnly, triggerType, triggerReference, triggerAmount)
     *                                       all orderTypes: timeInForce, selfTradePrevention, responseRequired
     * @return JSONObject response, get status of the order through response.getString("status")
     */
    public JSONObject placeOrder(String market, String side, String orderType, JSONObject body) {
        body.put("market", market);
        body.put("side", side);
        body.put("orderType", orderType);
        return privateRequest(body);
    }

    private String createSignature(long timestamp, JSONObject body) {
        if (Objects.isNull(this.apiSecret) || this.apiSecret.isEmpty() || Objects.isNull(this.apiKey) || this.apiKey.isEmpty()) {
            errorToConsole("The API key or secret has not been set. Please pass the key and secret when instantiating the bitvavo object.");
            return "";
        }

        try {
            String result = timestamp + "POST" + "/v2" + "/order";
            if(!body.isEmpty()) {
                result = result + bodyToJsonString(body);
            }
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(this.apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(result.getBytes(StandardCharsets.UTF_8))));
        }
        catch(Exception ex) {
            errorToConsole("Caught exception in createSignature " + ex);
            return "";
        }
    }

    private String bodyToJsonString(JSONObject body) {
        Iterator<String> keys = body.keys();
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340);
        StringBuilder jsonString = new StringBuilder("{");
        boolean first = true;

        while(keys.hasNext()) {
            String key = keys.next();
            if (!first) {
                jsonString.append(",");
            } else {
                first = false;
            }

            if ((body.get(key) instanceof Double) || (body.get(key) instanceof Float)) {
                jsonString.append("\"").append(key).append("\":").append(df.format(body.get(key)));
            } else if ((body.get(key) instanceof Integer) || (body.get(key) instanceof Long)) {
                jsonString.append("\"").append(key).append("\":").append(body.get(key).toString());
            } else if (body.get(key) instanceof Boolean) {
                jsonString.append("\"").append(key).append("\":").append(body.get(key));
            } else {
                jsonString.append("\"").append(key).append("\":\"").append(body.get(key).toString()).append("\"");
            }
        }
        jsonString.append("}");
        return jsonString.toString();
    }

    private void debugToConsole(String message) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if(this.debugging) {
            System.out.println(sdf.format(cal.getTime()) + " DEBUG: " + message);
        }
    }

    private void errorToConsole(String message) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(cal.getTime()) + " ERROR: " + message);
    }

    private void errorRateLimit(JSONObject response) {
        if (response.getInt("errorCode") == 105) {
            rateLimitRemaining = 0;
            String message = response.getString("error");
            String placeHolder = message.split(" at ")[1].replace(".", "");
            rateLimitReset = Long.parseLong(placeHolder);
            if (!rateLimitThreadStarted) {
                new Thread(() -> {
                    try {
                        long timeToWait = rateLimitReset - System.currentTimeMillis();
                        rateLimitThreadStarted = true;
                        debugToConsole("We are waiting for " + ((int) timeToWait / 1000) + " seconds, untill the rate limit ban will be lifted.");
                        Thread.sleep(timeToWait);
                    } catch (InterruptedException ie) {
                        errorToConsole("Got interrupted while waiting for the rate limit ban to be lifted.");
                    }
                    rateLimitThreadStarted = false;
                    if (System.currentTimeMillis() >= rateLimitReset) {
                        debugToConsole("Rate limit ban has been lifted, resetting rate limit to 1000.");
                        rateLimitRemaining = 1000;
                    }
                }).start();
            }
        }
    }

    private void updateRateLimit(Map<String,List<String>> response) {
        String remainingHeader = response.get("bitvavo-ratelimit-remaining").get(0);
        String resetHeader = response.get("bitvavo-ratelimit-resetat").get(0);
        if(remainingHeader != null) {
            rateLimitRemaining = Integer.parseInt(remainingHeader);
        }
        if(resetHeader != null) {
            rateLimitReset = Long.parseLong(resetHeader);
            if (!rateLimitThreadStarted) {
                new Thread(() -> {
                    try {
                        long timeToWait = rateLimitReset - System.currentTimeMillis();
                        rateLimitThreadStarted = true;
                        debugToConsole("We started a thread which waits for " + ((int) timeToWait / 1000) + " seconds, untill the rate limit will be reset.");
                        Thread.sleep(timeToWait);
                    } catch (InterruptedException ie) {
                        errorToConsole("Got interrupted while waiting for the rate limit to be reset.");
                    }
                    rateLimitThreadStarted = false;
                    if (System.currentTimeMillis() >= rateLimitReset) {
                        debugToConsole("Resetting rate limit to 1000.");
                        rateLimitRemaining = 1000;
                    }
                }).start();
            }
        }
    }

    private JSONObject privateRequest(JSONObject body) {
        try {
            long timestamp = System.currentTimeMillis();
            String signature = createSignature(timestamp, body);
            URL url = new URL(this.restUrl + "/order");
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();

            httpsCon.setRequestMethod("POST");
            httpsCon.setRequestProperty("bitvavo-access-key", this.apiKey);
            httpsCon.setRequestProperty("bitvavo-access-signature", signature);
            httpsCon.setRequestProperty("bitvavo-access-timestamp", String.valueOf(timestamp));
            httpsCon.setRequestProperty("bitvavo-access-window", String.valueOf(this.window));
            httpsCon.setRequestProperty("content-type", "application/json");
            if(!body.isEmpty()) {
                httpsCon.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsCon.getOutputStream());
                outputStreamWriter.write(body.toString());
                outputStreamWriter.flush();
            }


            int responseCode = httpsCon.getResponseCode();

            InputStream inputStream;
            if(responseCode == 200) {
                inputStream = httpsCon.getInputStream();
                updateRateLimit(httpsCon.getHeaderFields());
            }
            else {
                inputStream = httpsCon.getErrorStream();
            }
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "utf-8");
            String result = writer.toString();

            JSONObject response = new JSONObject(result);
            if (result.contains("errorCode")) {
                errorRateLimit(response);
                throw new RuntimeException("Error in request to bitvavo: " + result);
            }

            return response;
        }
        catch(Exception ex) {
            errorToConsole("Caught exception in privateRequest " + ex);
            return new JSONObject();
        }
    }
}
