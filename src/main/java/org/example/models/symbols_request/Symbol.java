package org.example.models.symbols_request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Symbol {

    @Expose

    private String symbol;
    @Expose

    private String baseCurrencyName;
    @Expose
    private String quoteCurrencyName;
    private String displayName;
    @Expose
    private String state;
    private Long visibleStartTime;
    private Long tradableStartTime;
    private CrossMargin crossMargin;
    @Expose
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
