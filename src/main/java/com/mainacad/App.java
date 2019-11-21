package com.mainacad;

import com.mainacad.model.Item;
import com.mainacad.service.OlxNavigationParserService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    private static final String BASE_URL = "https://www.olx.ua";

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.warning("You didnot input any keyword!");
            return;
        }

        List<Thread> threads = new ArrayList<>();
        List<Item> items = Collections.synchronizedList(new ArrayList<>());

        LOG.info("Parser started!!!");

        try {
            String keyword = URLEncoder.encode(args[0], "UTF-8");
            for (int i = 1; i < args.length; i++) {
                keyword += "+" + URLEncoder.encode(args[i], "UTF-8");
            }
            String url = BASE_URL + "/uk/list/q-" + keyword;

            OlxNavigationParserService olxNavigationParserService = new OlxNavigationParserService(items, url, threads);
            threads.add(olxNavigationParserService);
            olxNavigationParserService.run();
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        boolean threadsFinished;
        do {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadsFinished = checkThreads(threads);
        } while (threadsFinished);

        LOG.info("Parser finished!!! " + items.size() + " were extracted!");
    }

    private static boolean checkThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            if (thread.isAlive() || thread.getState().equals(Thread.State.NEW)) {
                return true;
            }
        }
        return false;
    }
}

