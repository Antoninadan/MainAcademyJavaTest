package com.mainacad;

import com.mainacad.model.Item;
import com.mainacad.service.OlxNavigationParserService;
import com.mainacad.service.OlxProductParserService;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    private static final String BASE_URL = "https://www.olx.ua/";

    public static void main(String[] args) {

        if (args.length == 0) {
            LOG.warning("You didnot input any keyword!");
            return;
        }

        List<Item> items = Collections.synchronizedList(new ArrayList<>());
        try {
            String keyword = URLEncoder.encode(args[0], "UTF-8");
            for (int i = 1; i < args.length; i++) {
                keyword += "+" + URLEncoder.encode(args[i], "UTF-8");
            }
            String url = BASE_URL + "/uk/list/q-" + keyword;

            // test without threads!!!!!
            OlxNavigationParserService olxNavigationParserService = new OlxNavigationParserService(items, url);
            olxNavigationParserService.navigate();
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }


//        // Test some product
        String url = "https://www.olx.ua/uk/obyavlenie/zimny-kombenzon-dlya-hoopchikv-vd-0-3-ms-IDG6fqC.html?sd=1#c9b599c6d1;promoted";
        Document document = null;
        OlxProductParserService olxProductParserService = new OlxProductParserService(items, url);
        olxProductParserService.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!items.isEmpty()) {
            LOG.info("\n" + items.get(0).toString());
        }

    }
}

