package org.example.models.symbols_request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CrossMargin {


    @JsonIgnore
    private boolean supportCrossMargin;
    @JsonIgnore
    private int maxLeverage;
    public boolean isSupportCrossMargin() {
        return supportCrossMargin;
    }

    public void setSupportCrossMargin(boolean supportCrossMargin) {
        this.supportCrossMargin = supportCrossMargin;
    }

    public int getMaxLeverage() {
        return maxLeverage;
    }

    public void setMaxLeverage(int maxLeverage) {
        this.maxLeverage = maxLeverage;
    }
}
