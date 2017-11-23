package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.TreeMap;


public class Main {

    private static int numErrors = 0;

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();

        ArrayList<Item> items = new ArrayList<>();

        ItemParser itemParser = new ItemParser();
        ArrayList<String> lines = itemParser.parseRawDataIntoStringArray(output);
        for(String line : lines) {
            try {
                items.add(itemParser.parseStringIntoItem(line));
            } catch (ItemParseException iae) {
                numErrors++;
            }
        }
        System.out.println(printOutput(items));
    }

    public static String printOutput(ArrayList<Item> items) {
        LinkedHashMap<String, TreeMap<Double, Integer>> priceTreeMaps = new LinkedHashMap<>();

        for(Item item : items) {
            if(priceTreeMaps.get(item.getName()) == null) {
                priceTreeMaps.put(item.getName(), new TreeMap<>(Collections.reverseOrder()));
            }
            addOneToTotal(priceTreeMaps.get(item.getName()), item);
        }

        String formattedOutput = "";
        StringBuilder stringBuilder = new StringBuilder();

        StringBuilder mainBreak = new StringBuilder();
        StringBuilder smallBreak = new StringBuilder();
        for(int i = 0; i < 13; i++) {
            mainBreak.append("=");
            smallBreak.append("-");
        }
        String mainBreakLine = mainBreak.toString();
        String smallBreakLine = smallBreak.toString();

        for(String name : priceTreeMaps.keySet()) {
            TreeMap<Double, Integer> prices = priceTreeMaps.get(name);
            int numTotal = prices.values().stream().reduce(0, Integer::sum);
            stringBuilder.append(String.format("name: %7s\tseen: %d times\n", name, numTotal));
            stringBuilder.append(String.format("%s\t%s\n", mainBreakLine, mainBreakLine));

            for(Double price : prices.keySet()) {
                stringBuilder.append(String.format("Price: %6s\tseen: %d times\n", price, prices.get(price)));
                stringBuilder.append(String.format("%s\t%s\n", smallBreakLine, smallBreakLine));
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append(String.format("Errors\t\t\tseen: %d times\n", numErrors));
        formattedOutput = stringBuilder.toString();
        return formattedOutput;
    }

    public static void addOneToTotal(TreeMap<Double, Integer> prices, Item item) {
        if(prices.containsKey(item.getPrice())) {
            prices.put(item.getPrice(), prices.get(item.getPrice()) + 1);
        }
        else {
            prices.put(item.getPrice(), 1);
        }
    }
}
