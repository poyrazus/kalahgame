package com.classcastexception.interview.kalahgame.model;

import java.util.List;

public class Player {

    private String    title;
    private final Pit kalah;
    private final int minPitIndex;
    private final int maxPitIndex;

    public Player(final Pit kalah, final String title, final int minPitIndex, final int maxPitIndex) {
        this.kalah = kalah;
        this.title = title;
        this.minPitIndex = minPitIndex;
        this.maxPitIndex = maxPitIndex;
    }

    public boolean ownPit(final int index) {
        return (index >= this.minPitIndex) && (index <= this.maxPitIndex);
    }

    public int totalStoneCountInOwnPits(final List<Pit> pits) {
        return pits.stream()
                   .filter(x -> (x.getIndex() >= this.minPitIndex) && (x.getIndex() <= this.maxPitIndex))
                   .mapToInt(x -> x.getStoneCount())
                   .sum();
    }

    public Player whoWins(final Player other) {
        if (this.getKalah().getStoneCount() > other.getKalah().getStoneCount()) {
            return this;
        }
        return other;
    }

    // getters and setters
    public Pit getKalah() {
        return this.kalah;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
