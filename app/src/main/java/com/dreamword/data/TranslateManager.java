package com.dreamword.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslateManager {
    private static final String TAG = "TranslateManager";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    private static final String YOUDAO_SUGGEST_URL = "https://dict.youdao.com/suggest";
    private static final String BAIDU_SUG_URL = "https://fanyi.baidu.com/sug";
    private static final String TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single";
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fff\\u3400-\\u4dbf]");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z\\s\\-'.]*$");

    public interface TranslateCallback {
        void onSuccess(String english, String chinese);
        void onError(String error);
    }

    public interface BatchTranslateCallback {
        void onSuccess(List<WordPair> results);
        void onError(String error);
    }

    public static class WordPair {
        public String english;
        public String chinese;

        public WordPair(String english, String chinese) {
            this.english = english;
            this.chinese = chinese;
        }
    }

    public static String detectLanguage(String text) {
        text = text.trim();
        if (text.isEmpty()) return "unknown";

        boolean hasChinese = CHINESE_PATTERN.matcher(text).find();
        boolean hasEnglish = text.matches(".*[A-Za-z].*");

        if (hasChinese && !hasEnglish) return "zh";
        if (hasEnglish && !hasChinese) return "en";
        return "unknown";
    }

    public static void translate(String text, TranslateCallback callback) {
        String lang = detectLanguage(text);
        if ("zh".equals(lang)) {
            translateChineseToEnglish(text, callback);
        } else if ("en".equals(lang)) {
            translateEnglishToChinese(text, callback);
        } else {
            callback.onError("无法识别语言，请输入纯中文或纯英文");
        }
    }

    public static void translateChineseToEnglish(String chinese, TranslateCallback callback) {
        new TranslateTask(chinese, "zh", "en", callback).execute();
    }

    public static void translateEnglishToChinese(String english, TranslateCallback callback) {
        new TranslateTask(english, "en", "zh", callback).execute();
    }

    private static class TranslateTask extends AsyncTask<Void, Void, String> {
        private final String text;
        private final String from;
        private final String to;
        private final TranslateCallback callback;
        private String result;
        private String error;

        TranslateTask(String text, String from, String to, TranslateCallback callback) {
            this.text = text;
            this.from = from;
            this.to = to;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if ("zh".equals(from)) {
                    result = fetchFromYoudao(text);
                    if (result == null) result = fetchFromBaidu(text);
                    if (result == null) result = fetchFromGoogle(text, "zh-CN", "en");
                } else {
                    result = fetchFromYoudao(text);
                    if (result == null) result = fetchFromBaidu(text);
                    if (result == null) result = fetchFromGoogle(text, "en", "zh-CN");
                }
                if (result != null && !result.isEmpty()) {
                    return "success";
                }
                error = "翻译失败，请检查网络";
            } catch (Exception e) {
                Log.e(TAG, "Translate error", e);
                error = "翻译失败：" + e.getMessage();
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String status) {
            if ("success".equals(status)) {
                if ("zh".equals(from)) {
                    callback.onSuccess(result, text);
                } else {
                    callback.onSuccess(text, result);
                }
            } else {
                callback.onError(error);
            }
        }
    }

    private static String fetchFromYoudao(String text) throws IOException {
        String urlStr = YOUDAO_SUGGEST_URL + "?q=" + URLEncoder.encode(text, "UTF-8") + "&num=5&doctype=json";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            JsonObject data = json.getAsJsonObject("data");
            if (data != null) {
                JsonArray entries = data.getAsJsonArray("entries");
                if (entries != null && !entries.isEmpty()) {
                    JsonObject entry = entries.get(0).getAsJsonObject();
                    String explain = entry.get("explain").getAsString();
                    return extractFirstMeaning(explain);
                }
            }
        }
        return null;
    }

    private static String fetchFromBaidu(String text) throws IOException {
        String urlStr = BAIDU_SUG_URL;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoOutput(true);

        String params = "kw=" + URLEncoder.encode(text, "UTF-8");
        conn.getOutputStream().write(params.getBytes("UTF-8"));

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            if (json.get("errno").getAsInt() == 0) {
                JsonArray data = json.getAsJsonArray("data");
                if (data != null && !data.isEmpty()) {
                    JsonObject item = data.get(0).getAsJsonObject();
                    String v = item.get("v").getAsString();
                    return extractFirstMeaning(v);
                }
            }
        }
        return null;
    }

    private static String fetchFromGoogle(String text, String from, String to) throws IOException {
        String urlStr = TRANSLATE_URL + "?client=gtx&sl=" + from + "&tl=" + to + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JsonArray json = JsonParser.parseString(sb.toString()).getAsJsonArray();
            if (json.size() > 0) {
                JsonArray translations = json.get(0).getAsJsonArray();
                StringBuilder result = new StringBuilder();
                for (JsonElement element : translations) {
                    JsonArray pair = element.getAsJsonArray();
                    if (pair.size() > 0) {
                        result.append(pair.get(0).getAsString());
                    }
                }
                return result.toString().trim();
            }
        }
        return null;
    }

    private static String extractFirstMeaning(String text) {
        text = text.replaceAll("^\\[[^\\]]+\\]", "").trim();
        text = text.replaceAll("^[a-z]+\\.\\s*", "").trim();
        String[] parts = text.split("[;；]");
        return parts[0].trim();
    }
}