package com.look_finder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.look_finder.components.parcers.BershkaParcer;
import com.look_finder.components.parcers.UrlCreatorBershka;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import okhttp3.*;

@Service
public class BershkaService {

    private final BershkaParcer parcer;
    private final UrlCreatorBershka urlCreator;

    public BershkaService(BershkaParcer parcer,  UrlCreatorBershka urlCreator) {
        this.parcer = parcer;
        this.urlCreator = urlCreator;
    }

    /**
     * This function gets all Bershkas available products and then sends this
     * information back to our website.
     * First, it fetches Bershkas stocks and parses the response using "UrlCreatorBershka".
     * Second, it fetches real Bershkas endpoints sending all productIds to get all the info and then parses it using "BershkaParcer".
     * @param category string representing the category our user is looking for. Example: "jeans_w" - jeans woman
     * @param sizeD size in Digits because bershka can assign to jeans a digit size(36) and text size(M)
     * @param sizeS size as Text --||--
     * @return parsed JSON with all necessary information for display
     * @throws IOException if we have a problem fetching bershka data throws exception
     * */
    public Object getAndParseJSON(String category, String sizeD, String sizeS) throws IOException {

        // Create url to correct stock
        String url_stock = createUrlStock(category);

        // Create an HTTP client (This client can send requests to endpoints)
        OkHttpClient client = new OkHttpClient();

        // Create a request on our endpoint in Internet to get all available positions from bershka
        // We are adding headers so the endpoint doesn't understand that we are making this request from code
        Request stock_req = new Request.Builder()
                .url(url_stock)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/118.0.0.0 Safari/537.36") // Header to tell that we are Google Chrome
                .header("Accept", "application/json, text/plain, */*") // We are excepting all information from the endpoint
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Referer", "https://www.bershka.com/") // We are "coming" from Bershka website
                .header("Origin", "https://www.bershka.com") // We are "coming" from Bershka website
                .header("Connection", "keep-alive")
                .header("DNT", "1") // Don't track me!
                // These 3 further headers just need to be (-_-)
                .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Fetch-Site", "same-origin")
                .build(); // End building a request

        String response_body;

        // Our Client sends our response, and we are waiting for the answer
        try (Response stock_resp = client.newCall(stock_req).execute()){
            if (!stock_resp.isSuccessful()) { // Check if we got "200" any other = error
                throw new IOException("Unexpected code " + stock_resp.code()); // Tels me if we got bad response code
            } else {
                ResponseBody body = stock_resp.body(); // Get what our request got
                if (body == null) throw new IOException("Empty response body"); // Check if the response body is empty (doesn't really need it, I think)

                // Interesting thing! If we don't say in the header that we except gzip, it won't send any! :)
                response_body = body.string();
            }
        }

        /* We got our JSON from Bershka with stocks
        * Then we send it our component urlCreator
        * (for more explanation how it works, see the component "components/parcers/UrlCreatorBershka")
        * In return from it, we get url (unexpected, yes? :)) with NO duplicates of product! How cool is that!!!
        * */
        String url_products = urlCreator.CreateUrl(response_body, category);

        System.out.println(url_products);

        // Same creating request, but to get all info for products we extracted previously
        Request products_req = new Request.Builder()
                .url(url_products)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/118.0.0.0 Safari/537.36") // Header to tell that we are Google Chrome
                .header("Accept", "application/json, text/plain, */*") // We are excepting all information from the endpoint
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Referer", "https://www.bershka.com/") // We are "coming" from Bershka website
                .header("Origin", "https://www.bershka.com") // We are "coming" from Bershka website
                .header("Connection", "keep-alive")
                .header("DNT", "1") // Don't track me!
                // These 3 further headers just need to be (-_-)
                .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Fetch-Site", "same-origin")
                .build(); // End building a request

        // Same as before. Sends a request and gets a response
        try (Response products_resp = client.newCall(products_req).execute()){
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
        ObjectMapper mapper = new ObjectMapper();
        Object jsonObj = mapper.readValue(response_body, Object.class);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        Files.writeString(filePath, writer.writeValueAsString(jsonObj));

        return parcer.parse(response_body, sizeD, sizeS);
    }

    /**
     * This private function creates url to bershka stocks
     * @param category Depends on the category adds necessary category id
     * @return url to fetch or that we send bad data
     * */
    private String createUrlStock(String category) {
        StringBuilder url = new StringBuilder("https://www.bershka.com/itxrest/2/catalog/store/45109545/40259564/category/");

        switch(category){
            case "jeans_w":
                url.append("1010276029/stock");
                break;
            case "jackets_m":
                url.append("1010193546/stock");
                break;
            default:
                return "ERROR_INVALID_CATEGORY";
        }
        System.out.println(url);
        return url.toString();
    }
}
