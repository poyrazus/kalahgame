package com.classcastexception.interview.kalahgame.model;

public class Pit {
    private int stoneCount;
    private int index;

    public Pit(final int stoneCount, final int index) {
        this.stoneCount = stoneCount;
        this.index = index;
    }

    public void incrementStoneCount() {
        this.stoneCount++;
    }

    public void incrementStoneCount(final int increment) {
        this.stoneCount += increment;
    }

    public void clearStones() {
        this.stoneCount = 0;
    }

    // getters and setters
    public int getIndex() {
        return this.index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public int getStoneCount() {
        return this.stoneCount;
    }

    public void setStoneCount(final int stoneCount) {
        this.stoneCount = stoneCount;
    }

    @Override
    public String toString() {
        return "Pit [index=" + this.index + ", stoneCount=" + this.stoneCount + "]";
    }
}
