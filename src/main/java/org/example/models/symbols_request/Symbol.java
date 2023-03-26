package org.example.models.symbols_request;

import com.google.gson.annotations.Expose;
import io.protostuff.Tag;

public class Symbol {

    @Expose
    @Tag(1)
    private String symbol;
    @Expose
    @Tag(2)
    private String baseCurrencyName;
    @Expose
    @Tag(3)
    private String quoteCurrencyName;
    @Tag(value = "a", alias = "displayName")
    private String displayName;
    @Expose
    @Tag(5)
    private String state;
    @Tag(6)
    private Long visibleStartTime;
    @Tag(7)
    private Long tradableStartTime;
    @Tag(8)
    private CrossMargin crossMargin;
    @Expose
    @Tag(9)
    private SymbolTradeLimit symbolTradeLimit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public String getQuoteCurrencyName() {
        return quoteCurrencyName;
    }

    public void setQuoteCurrencyName(String quoteCurrencyName) {
        this.quoteCurrencyName = quoteCurrencyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getVisibleStartTime() {
        return visibleStartTime;
    }

    public void setVisibleStartTime(Long visibleStartTime) {
        this.visibleStartTime = visibleStartTime;
    }

    public Long getTradableStartTime() {
        return tradableStartTime;
    }

    public void setTradableStartTime(Long tradableStartTime) {
        this.tradableStartTime = tradableStartTime;
    }

    public CrossMargin getCrossMargin() {
        return crossMargin;
    }

    public void setCrossMargin(CrossMargin crossMargin) {
        this.crossMargin = crossMargin;
    }

    public SymbolTradeLimit getSymbolTradeLimit() {
        return symbolTradeLimit;
    }

    public void setSymbolTradeLimit(SymbolTradeLimit symbolTradeLimit) {
        this.symbolTradeLimit = symbolTradeLimit;
    }


}
