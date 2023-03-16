package org.example;

import org.example.util.Constants;

import java.util.*;

public class MarketData {
    public static MarketData INSTANCE = new MarketData();



    private final HashMap<String, TradingPair> tradingPairsMap;
    private final List<Triangle> triangles;
    private final HashMap<TradingPair, BookEntry> dataTable;

    private MarketData() {
        //tradingPairs = new ArrayList<>();
        tradingPairsMap = new HashMap<>();
        triangles = new LinkedList<>();
        dataTable = new HashMap<>();
    }

    public void addPair(TradingPair pair) {
        String name = String.format("%s_%s", pair.source, pair.destination);
        if (!tradingPairsMap.containsKey(name)) {
            tradingPairsMap.put(name, pair);
        }
    }

    public TradingPair findTradingPairBetween(Asset first, Asset second) {
        String name1 = String.format("%s_%s", first.getName(), second.getName());
        String name2 = String.format("%s_%s", second.getName(), first.getName());
        if (tradingPairsMap.containsKey(name1)) {
            return tradingPairsMap.get(name1);
        } else return tradingPairsMap.getOrDefault(name2, null);

    }

    public TradingPair findTradingPairBySymbol(String symbol) {
        return tradingPairsMap.getOrDefault(symbol, null);
    }

    public void initializeHashtable() {
        if (dataTable.size() < tradingPairsMap.size()) {


            for (TradingPair pair : tradingPairsMap.values()) {
                BookEntry stub = new BookEntry(pair, 0, 0, 0, 0);
                stub.setTimestampWhenUpdated(0);
                dataTable.put(pair, stub);
            }
            System.out.println("Hashtable initialized");
        } else {
            System.out.println("Hashtable already initialized");
        }
    }

    public void initializeTriangles() {
        if (triangles.size() > 0) {
            System.out.println("Triangles already initialized");
            return;
        }

        Asset BTC = Constants.BTC;
        for (TradingPair pair : tradingPairsMap.values()) {
            Asset source = pair.getSource();
            Asset destination = pair.getDestination();
            if (!source.equals(BTC) && !destination.equals(BTC)) {
                TradingPair first = findTradingPairBetween(BTC, source);
                TradingPair second = pair;
                TradingPair third = findTradingPairBetween(BTC, destination);
                if (first != null && third != null) {
                    Triangle triangle = new Triangle(first, second, third);
                    triangles.add(triangle);
                }
            }
        }

        System.out.println("Triangles initialized");

    }

    public void initialize() {
        initializeHashtable();
        initializeTriangles();
    }


    public List<Triangle> getTriangles() {
        return triangles;
    }

    public HashMap<TradingPair, BookEntry> getDataTable() {
        return dataTable;
    }

    public void setBookEntryAtTradingPair(TradingPair pair, BookEntry entry) {

        BookEntry oldEntry = dataTable.get(pair);

        oldEntry.setTimestampWhenUpdated(System.currentTimeMillis());
        oldEntry.setDestinationAmount(entry.getDestinationAmount());
        oldEntry.setDestinationPrice(entry.getDestinationPrice());
        oldEntry.setSourceAmount(entry.getSourceAmount());
        oldEntry.setSourcePrice(entry.getSourcePrice());
    }

    public void setBookEntryAtTradingPair(TradingPair pair, double bidPrice, double bidAmount, double askPrice, double askAmount) {

        BookEntry oldEntry = dataTable.get(pair);
        oldEntry.setTimestampWhenUpdated(System.currentTimeMillis());
        oldEntry.setDestinationAmount(askAmount);
        oldEntry.setDestinationPrice(askPrice);
        oldEntry.setSourceAmount(bidAmount);
        oldEntry.setSourcePrice(bidPrice);
    }

    public BookEntry getBookEntryByPair(Asset asset1, Asset asset2) {
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        return dataTable.get(pair);
    }

    public List<List<Triangle>> getProfitableTrianglesThatIncludeTradingPair(TradingPair pair) {
        List<Triangle> profitableTriangles = new LinkedList<>();
        List<Triangle> profitableReversedTriangles = new LinkedList<>();

        for (Triangle triangle : triangles) {
            if (triangle.getSecond().equals(pair) || triangle.getFirst().equals(pair) || triangle.getThird().equals(pair)) {
                if (triangle.isProfitable()) {
                    profitableTriangles.add(triangle);
                } else if (triangle.isProfitableWhenReversed()) {
                    profitableReversedTriangles.add(triangle);
                }
            }
        }
        return List.of(profitableTriangles, profitableReversedTriangles);
    }

    public double getGreaterPrice(Asset asset1, Asset asset2) {
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        double price1 = dataTable.get(pair).getDestinationPrice();
        double price2 = dataTable.get(pair).getSourcePrice();
        return Math.max(price1, price2);
    }

    public double getLesserPrice(Asset asset1, Asset asset2) {
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        double price1 = dataTable.get(pair).getDestinationPrice();
        double price2 = dataTable.get(pair).getSourcePrice();
        return Math.min(price1, price2);
    }

    public double getAmountFromPair(Asset source, Asset destination) {
        TradingPair pair = findTradingPairBetween(source, destination);
        if (pair.getSource().equals(source)) {
            return dataTable.get(pair).getDestinationAmount();
        } else {
            return dataTable.get(pair).getSourceAmount();
        }
    }

    public double getAskAmount(Asset base, Asset quote) {
        TradingPair pair = findTradingPairBetween(base, quote);
        if (pair.getSource().equals(base)) {
            return dataTable.get(pair).getDestinationAmount();
        } else {
            return dataTable.get(pair).getSourceAmount();
        }

    }

    public double getBidAmount(Asset base, Asset quote) {
        TradingPair pair = findTradingPairBetween(base, quote);
        if (pair.getSource().equals(base)) {
            return dataTable.get(pair).getSourceAmount();
        } else {
            return dataTable.get(pair).getDestinationAmount();
        }

    }
}
