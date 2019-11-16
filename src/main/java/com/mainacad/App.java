package com.mainacad;

import com.mainacad.model.Item;
import com.mainacad.service.OlxProductParserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        String url = "https://www.olx.ua/uk/list/q-hp-omen/";




        try {
//            String keyword = URLEncoder.encode(args[0], "UTF-8");
//            for (int i=1; i<args.length; i++) {
//                keyword += "+"+ URLEncoder.encode(args[i], "UTF-8");
//            }
//            String startUrlPart = BASE_URL + "/uk/list/q-";
//            String finishUrlPart = keyword;

            Document document = null;
            document = Jsoup.connect(url).get();
            Element productGallery = document.getElementsByClass("fixed offers breakword redesigned").first();
//            Elements productElements = productGallery.getElementsByClass("wrap");
//            productElements = productGallery.getElementsByClass("offer-wrapper");
            Elements productElements = productGallery.getElementsByTag("A");

            System.out.println(productElements.first().text());


            ArrayList<String> downServers = new ArrayList<>();
            Element table = productGallery.select("table").get(0); //select the first table.
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                Element row = rows.get(i);
                Elements cols = row.select("td");
                Element table2 = cols.select("table").get(0);
                Elements rows2 = table.select("tr");
                for (int j = 0; j < rows2.size(); j++) { //first row is the col names so skip it.
                    Element row3 = rows2.get(j);
                    Elements cols3 = row3.select("td");


                System.out.println(cols3.first().text());}
                         }
//
//            Set<String> itemlinks = new HashSet<>();
//            cols.forEach(it -> itemlinks.add(it.attr("href")));
//            System.out.println("itemlinks = " + itemlinks);

//



//
//            Elements productElements = productGallery.getElementsByAttributeValue("data-qaid", "product_name");
//            Set<String> itemlinks = new HashSet<>();
//            productElements.forEach(it -> itemlinks.add(it.attr("href")));
//            System.out.println("itemlinks = " + itemlinks);

        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


//        // test product
//        List<Item> items = Collections.synchronizedList(new ArrayList<>());
//        String url = "https://www.olx.ua/uk/obyavlenie/zimny-kombenzon-dlya-hoopchikv-vd-0-3-ms-IDG6fqC.html?sd=1#c9b599c6d1;promoted";
//        Document document = null;
//        OlxProductParserService olxProductParserService = new OlxProductParserService(items, url);
//        olxProductParserService.start();
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if (!items.isEmpty()) {
//            LOG.info("\n" + items.get(0).toString());
//        }

    }
}

