package org.example;

import org.example.util.Constants;

import java.util.*;

public class MarketData {
    public static MarketData INSTANCE = new MarketData();


    private final HashMap<String, TradingPair> tradingPairsMap;
    private final List<Triangle> triangles;
    private final HashMap<TradingPair, BookEntry> dataTable;

    private final HashMap<String, List<Triangle>> trianglesByPair;

    private MarketData() {
        //tradingPairs = new ArrayList<>();
        //test
        tradingPairsMap = new HashMap<>();
        triangles = new LinkedList<>();
        dataTable = new HashMap<>();
        trianglesByPair = new HashMap<>();
    }

    public void addPair(TradingPair pair) {
        String name = pair.source + "_" + pair.destination;
        if (!tradingPairsMap.containsKey(name)) {
            tradingPairsMap.put(name, pair);
        }
    }

    public TradingPair findTradingPairBetween(Asset first, Asset second) {
        String firstAssetName = first.getName();
        String secondAssetName = second.getName();

        String name1 = firstAssetName + "_" + secondAssetName;
        String name2 = secondAssetName + "_" + firstAssetName;
        if (tradingPairsMap.containsKey(name1)) {
            return tradingPairsMap.get(name1);
        } else return tradingPairsMap.getOrDefault(name2, null);

    }

    public TradingPair findTradingPairBySymbol(String symbol) {
        return tradingPairsMap.getOrDefault(symbol, null);
    }

    public void addTriangleToHashMap(String key, Triangle triangle) {
        if (trianglesByPair.containsKey(key)) {
            List<Triangle> list = trianglesByPair.get(key);
            if (!list.contains(triangle)) {
                list.add(triangle);
            }
            trianglesByPair.put(key, list);

        } else {
            List<Triangle> list = new ArrayList<>();
            list.add(triangle);
            trianglesByPair.put(key, list);
        }
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
        if (trianglesByPair.size() > 0) {
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
                    addTriangleToHashMap(triangle.getFirst().getSymbol(), triangle);
                    addTriangleToHashMap(triangle.getSecond().getSymbol(), triangle);
                    addTriangleToHashMap(triangle.getThird().getSymbol(), triangle);
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
        oldEntry.setAskAmount(entry.getAskAmount());
        oldEntry.setAskPrice(entry.getAskPrice());
        oldEntry.setBidAmount(entry.getBidAmount());
        oldEntry.setBidPrice(entry.getBidPrice());
    }

    public void setBookEntryAtTradingPair(TradingPair pair, double bidPrice, double bidAmount, double askPrice, double askAmount) {

        BookEntry oldEntry = dataTable.get(pair);
        oldEntry.setTimestampWhenUpdated(System.currentTimeMillis());
        oldEntry.setAskAmount(askAmount);
        oldEntry.setAskPrice(askPrice);
        oldEntry.setBidAmount(bidAmount);
        oldEntry.setBidPrice(bidPrice);
    }

    public BookEntry getBookEntryByPair(Asset asset1, Asset asset2) {
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        return dataTable.get(pair);
    }

    public List<Triangle> getTrianglesHavingEdge(String symbol) {
        return trianglesByPair.get(symbol);
    }

    public List<List<Triangle>> getProfitableTriangleHavingEdge(TradingPair pair) {
        List<Triangle> profitableTriangles = new LinkedList<>();
        List<Triangle> profitableReversedTriangles = new LinkedList<>();

        for (Triangle triangle : getTrianglesHavingEdge(pair.getSymbol())) {

            if (triangle.isProfitable()) {
                profitableTriangles.add(triangle);
                break;
            } else if (triangle.isProfitableWhenReversed()) {
                profitableReversedTriangles.add(triangle);
                break;
            }
        }
        return List.of(profitableTriangles, profitableReversedTriangles);
    }

    public double getAskPrice(Asset asset1, Asset asset2) {
        //greater price
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        return dataTable.get(pair).getAskPrice();
    }

    public double getBidPrice(Asset asset1, Asset asset2) {
        //lesser price
        TradingPair pair = findTradingPairBetween(asset1, asset2);
        return dataTable.get(pair).getBidPrice();
    }

    public double getAmountFromPair(Asset source, Asset destination) {
        TradingPair pair = findTradingPairBetween(source, destination);
        if (pair.getSource().equals(source)) {
            return dataTable.get(pair).getAskAmount();
        } else {
            return dataTable.get(pair).getBidAmount();
        }
    }

    public double getAskAmount(Asset base, Asset quote) {
        TradingPair pair = findTradingPairBetween(base, quote);
        if (pair.getSource().equals(base)) {
            return dataTable.get(pair).getAskAmount();
        } else {
            return dataTable.get(pair).getAskAmount();
        }

    }

    public double getBidAmount(Asset base, Asset quote) {
        TradingPair pair = findTradingPairBetween(base, quote);
        if (pair.getSource().equals(base)) {
            return dataTable.get(pair).getBidAmount();
        } else {
            return dataTable.get(pair).getBidAmount();
        }

    }
}
