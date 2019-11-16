package com.mainacad.service;

import com.mainacad.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

public class OlxProductParserService extends Thread {
    private static final Logger LOG = Logger.getLogger(OlxProductParserService.class.getName());

    private final List<Item> items;
    private final String url;

    public OlxProductParserService(List<Item> items, String url) {
        this.items = items;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
            String itemId = extractItemId(document);
            String name = extractName(document);
            BigDecimal price = extractPrice(document);
            String imageUrl = extractImageUrl(document);

            Item item = new Item(itemId, name, url, imageUrl, price);
            items.add(item);
        } catch (IOException e) {
            LOG.severe(String.format("Item by URL %s was not extracted", url));
        }
    }

    private String extractItemId(Document document) {
        String result = "";
        try {
            Element productInfo = document.getElementsByClass("offer-titlebox").first();
            result = productInfo.getElementsByTag("SMALL").first().text().replaceAll("\\D+","");
            return result;
        } catch (Exception e) {
            LOG.severe(String.format("Item id by URL %s was not extracted", url));
        }
        return result;
    }

    private String extractName(Document document) {
        String result = "";
        try {
            Element productInfo = document.getElementsByClass("offer-titlebox").first();
            result = productInfo.getElementsByTag("H1").first().text();
            return result;
        } catch (Exception e) {
            LOG.severe(String.format("Item product name by URL %s was not extracted", url));
        }
        return result;
    }

    private BigDecimal extractPrice(Document document) {
        BigDecimal result = null;
        try {

            Element product = document.getElementsByClass("xxxx-large not-arranged").first();
            String resultAsText = product.text();
            resultAsText = resultAsText.replaceAll("[^0-9,]", "");
            resultAsText = resultAsText.replaceAll("[,]", ".");
            result = new BigDecimal(resultAsText).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.severe(String.format("Item price by URL %s was not extracted", url));
        }
        return result;
    }

    private String extractImageUrl(Element productInfo) {
        String result = "";
        try {
            result = productInfo.getElementsByAttributeValue("property", "og:image").first().attr("content");
            return result;
        } catch (Exception e) {
            LOG.severe(String.format("Item image url by URL %s was not extracted", url));
        }
        return result;
    }

}
