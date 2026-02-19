package com.look_finder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.look_finder.components.bershka.BershkaParcer;
import com.look_finder.components.bershka.UrlCreatorBershka;
import com.look_finder.components.bershka.CategoryIdFinderBershka;
import com.look_finder.components.bershka.BershkaSizeSelector;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

import okhttp3.*;

@Service
public class BershkaService {

    private final BershkaParcer parcer;
    private final UrlCreatorBershka urlCreator;
    private final CategoryIdFinderBershka idFinder;
    private final BershkaSizeSelector  sizeSelector;

    private final CookieJar cookieJar = new CookieJar() {
        private final Map<String, List<Cookie>> store = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            store.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return store.getOrDefault(url.host(), Collections.emptyList());
        }
    };

    private final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(Duration.ofSeconds(20))
            .readTimeout(Duration.ofSeconds(40))
            .writeTimeout(Duration.ofSeconds(20))
            .followRedirects(true)
            .build();

    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/118.0.0.0 Safari/537.36";
    // =========================

    public BershkaService(BershkaParcer parcer,  UrlCreatorBershka urlCreator,   CategoryIdFinderBershka idFinder, BershkaSizeSelector sizeSelector) {
        this.parcer = parcer;
        this.urlCreator = urlCreator;
        this.idFinder = idFinder;
        this.sizeSelector = sizeSelector;
    }

    /**
     * This function gets all Bershkas available products and then sends this
     * information back to our website.
     * First, it fetches Bershkas stocks and parses the response using "UrlCreatorBershka".
     * Second, it fetches real Bershkas endpoints sending all productIds to get all the info and then parses it using "BershkaParcer".
     * @param category_ string representing the category our user is looking for. Example: "jeans_w" - jeans woman
     * @return parsed JSON with all necessary information for display
     * @throws IOException if we have a problem fetching bershka data throws exception
     * */
    public List<List<Map<String, Object>>> getAndParseBershkaJSON(String category_, String sex, int bust, int waist, int hip) throws IOException {

        List<List<Map<String, Object>>> jsons_for_shuffle = new ArrayList<>();

        List<String> categorys = new ArrayList<>(Arrays.asList(category_.split("\\+")));

        warmUpSession();

        ObjectMapper mapper = new ObjectMapper();
        for (String category : categorys) {
            String temp = idFinder.findCategoryId(category, sex);
            String[] parts = temp.split("\\+");
            String cat_id = parts[0];
            String orientation = parts[1];

            // Create url to correct stock
            String url_stock = createUrlStock(cat_id);
            // Create an HTTP client (This client can send requests to endpoints)
            OkHttpClient client = new OkHttpClient();

            // Create a request on our endpoint in Internet to get all available positions from bershka
            // We are adding headers so the endpoint doesn't understand that we are making this request from code
            Request stock_req = new Request.Builder()
                    .url(url_stock)
                    .header("User-Agent", UA)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Referer", "https://www.bershka.com/")
                    .header("Origin", "https://www.bershka.com")
                    .header("Connection", "keep-alive")
                    .header("DNT", "1")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Fetch-Site", "same-origin")
                    // CHANGED: добавим браузерные CORS заголовки (часто помогают)
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .build();

            String response_body;
            // Our Client sends our response, and we are waiting for the answer
            try (Response stock_resp = client.newCall(stock_req).execute()) {
                if (!stock_resp.isSuccessful()) { // Check if we got "200" any other = error
                    throw new IOException("Unexpected code " + stock_resp.code()); // Tels me if we got bad response code
                } else {
                    ResponseBody body = stock_resp.body(); // Get what our request got
                    if (body == null)
                        throw new IOException("Empty response body"); // Check if the response body is empty (doesn't really need it, I think)

                    // Interesting thing! If we don't say in the header that we except gzip, it won't send any! :)
                    response_body = body.string();
                }
            }

            /* We got our JSON from Bershka with stocks
             * Then we send it our component urlCreator
             * (for more explanation how it works, see the component "components/parcers/UrlCreatorBershka")
             * In return from it, we get url (unexpected, yes? :)) with NO duplicates of product! How cool is that!!!
             * */
            String url_products = urlCreator.CreateUrl(response_body, cat_id);


            // Same creating request, but to get all info for products we extracted previously
            Request products_req = new Request.Builder()
                    .url(url_products)
                    .header("User-Agent", UA)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Referer", "https://www.bershka.com/")
                    .header("Origin", "https://www.bershka.com")
                    .header("Connection", "keep-alive")
                    .header("DNT", "1")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Fetch-Site", "same-origin")
                    // CHANGED: добавим браузерные CORS заголовки
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .build();

            // Same as before. Sends a request and gets a response
            try (Response products_resp = client.newCall(products_req).execute()) {
                if (!products_resp.isSuccessful()) {
                    throw new IOException("Unexpected code " + products_resp.code());
                } else {
                    ResponseBody body = products_resp.body();
                    if (body == null) throw new IOException("Empty response body");

                    response_body = body.string();
                }
            }

            // Saving JSON just for a check
            Path jsonDir = Path.of("src/main/resources/json");
            Files.createDirectories(jsonDir);

            Path filePath = jsonDir.resolve("original_bershka.json");

            // save JSON
            mapper = new ObjectMapper();
            Object jsonObj = mapper.readValue(response_body, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            Files.writeString(filePath, writer.writeValueAsString(jsonObj));

            temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] sizes = temp.split("\\+");
            String error = sizes[0];
            String sizeD = sizes[1];
            String sizeS = sizes[2];

            List<Map<String, Object>> parsed_json = parcer.parse(response_body, sizeD, sizeS, error);

            jsons_for_shuffle.add(parsed_json);
        }

        return jsons_for_shuffle;
    }

    private void warmUpSession() {
        Request warm = new Request.Builder()
                .url("https://www.bershka.com/")
                .header("User-Agent", UA)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
                .build();

        try (Response resp = client.newCall(warm).execute()) {
            ResponseBody body = resp.body();
            if (body != null) body.close();
        } catch (Exception ignored) {
            // если не получилось — просто продолжаем, основной запрос сам покажет код
        }
    }

    /**
     * This private function creates url to bershka stocks
     * @param category_id Specific category id
     * @return url to fetch or that we send bad data
     * */
    private String createUrlStock(String category_id) {
        StringBuilder url = new StringBuilder("https://www.bershka.com/itxrest/2/catalog/store/45109545/40259564/category/");

        url.append(category_id);

        url.append("/stock");
        return url.toString();
    }
}
