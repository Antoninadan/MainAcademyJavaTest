package com.mainacad.service;

import com.mainacad.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class OlxNavigationParserService extends Thread {
    private static final Logger LOG = Logger.getLogger(OlxNavigationParserService.class.getName());

    private final List<Item> items;
    private final List<Thread> threads;
    private final String url;

    public OlxNavigationParserService(List<Item> items, String url, List<Thread> threads) {
        this.items = items;
        this.url = url;
        this.threads = threads;
    }

    @Override
    public void run() {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            List<String> pageLinks = getPageLinks(document);
            for (String pageLink:pageLinks) {
                List<String> itemLinks = getItemLinks(pageLink, 1);
                for(String itemLink:itemLinks){
                    OlxProductParserService olxProductParserService = new OlxProductParserService(items, itemLink);
                    threads.add(olxProductParserService);
                    olxProductParserService.start();
                }
            }
        } catch (Exception e) {
            LOG.severe("Products were not extracted by URL " + url);
        }
    }

    public List<String> getPageLinks(Document document) {
        List<String> links = getPaginationPagesLinks(document);
        links.add(url);
        return links;
    }

    private List<String> getPaginationPagesLinks(Document document) {
        List<String> paginationPagesLinks = new ArrayList<>();
        try {
            Element lastPageElement = document.getElementsByClass("item fleft").last();
            if (lastPageElement != null) {
                Integer lastPage = Integer.valueOf(lastPageElement.getElementsByTag("SPAN").last().text());
                for (int i = 2; i <= lastPage; i++) {
                    String nextPageUrl = url + "?page=" + i;
                    paginationPagesLinks.add(nextPageUrl);
                }
            }
        } catch (Exception e) {
            LOG.severe("Pages were not extracted by URL " + url);
        }
        return paginationPagesLinks;
    }

    private List<String> getItemLinks(String url, int maxItemsPerPage) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Element productGallery = document.getElementsByClass("fixed offers breakword redesigned").first();
            Element table = productGallery.select("table").get(0);
            Elements rows = table.getElementsByClass("wrap");

            Set<String> itemlinks = new HashSet<>();
            int counter = 0;
            for (int i = 1; i < rows.size(); i++) {
                if (counter > maxItemsPerPage) {
                    break;
                }
                Element element = table.getElementsByClass("wrap").get(i);
                String cols = element.select("td").get(1).getElementsByTag("A").attr("href");
                itemlinks.add(cols);
                counter++;
            }
            LOG.info("itemlinks from one page = " + itemlinks);
            return new ArrayList<>(itemlinks);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}