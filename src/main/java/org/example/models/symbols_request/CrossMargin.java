package org.example.models.symbols_request;

public class CrossMargin {
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

    boolean supportCrossMargin;
    int maxLeverage;
}
