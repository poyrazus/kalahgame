package com.classcastexception.interview.kalahgame.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.classcastexception.interview.kalahgame.model.Pit;
import com.classcastexception.interview.kalahgame.model.Player;

@Named
@SessionScoped
public class GamePlay implements Serializable {

    private static final long serialVersionUID = 4948815599564758915L;
    // Made pit count constant; all index calculations use this.
    // In case when someone comes and says: "Let's play with 14 pits instead of 12, changing this value is enough
    private static final int  PIT_COUNT        = 12;

    private Player            player1;
    private Player            player2;
    private Player            currentPlayer;
    private Player            opponent;

    private List<Pit>         pits;
    private List<Pit>         firstRowPits;
    private List<Pit>         secondRowPits;
    private Pit               selectedPit;

    private int               numberOfStones   = 6;

    private boolean           gameStarted;
    private String            notificationMessage;

    public void createGame() {
        // Initialize all pits, kalahs are also thought as pits
        this.pits = new ArrayList<>();
        for (int i = 0; i < (PIT_COUNT + 2); i++) {
            this.pits.add(new Pit(this.numberOfStones, i));
        }

        // Same pit initialization as above, Java 8 way
        // this.pits = IntStream.range(0, PIT_COUNT + 2).mapToObj(i -> new Pit(this.numberOfStones, i)).collect(Collectors.toList());

        // These are kalahs
        this.pits.get(PIT_COUNT / 2).clearStones();
        this.pits.get(PIT_COUNT + 1).clearStones();

        // Here, separating first row and second row pits to be able to display them in the grid easily
        this.firstRowPits = new ArrayList<>();
        // To be able to display first row in reverse order
        for (int i = (PIT_COUNT / 2) - 1; i >= 0; i--) {
            this.firstRowPits.add(this.pits.get(i));
        }
        this.secondRowPits = this.pits.subList((PIT_COUNT / 2) + 1, this.pits.size() - 1);

        this.player1 = new Player(this.pits.get(PIT_COUNT / 2), "Player 1", 0, (PIT_COUNT / 2) - 1);
        this.player2 = new Player(this.pits.get(PIT_COUNT + 1), "Player 2", (PIT_COUNT / 2) + 1, PIT_COUNT);

        this.gameStarted = true;
        this.currentPlayer = this.player1;
        this.opponent = this.player2;
        this.notificationMessage = "Game Started. " + this.currentPlayer.getTitle() + " moves.";
    }

    private void togglePlayer() {
        this.opponent = this.currentPlayer;
        this.currentPlayer = this.currentPlayer.equals(this.player1) ? this.player2 : this.player1;
        this.notificationMessage = this.currentPlayer.getTitle() + " moves.";
    }

    public void move() {
        int stonesToDistribute = this.selectedPit.getStoneCount();
        this.selectedPit.clearStones();

        Pit nextPit = this.selectedPit;
        // Iterate until the stone count is 1, with the last stone we will decide what will be the next move
        while (stonesToDistribute > 1) {
            nextPit = this.nextPit(nextPit);
            nextPit.incrementStoneCount();
            stonesToDistribute--;
        }
        nextPit = this.nextPit(nextPit);

        // Last stone conditions
        if (nextPit.equals(this.currentPlayer.getKalah())) {
            this.currentPlayer.getKalah().incrementStoneCount();
            this.notificationMessage = this.currentPlayer.getTitle() + " continues.";
        } else if ((nextPit.getStoneCount() == 0) && this.currentPlayer.ownPit(nextPit.getIndex())) {
            this.currentPlayer.getKalah().incrementStoneCount(this.pits.get(PIT_COUNT - nextPit.getIndex()).getStoneCount() + 1);
            this.pits.get(PIT_COUNT - nextPit.getIndex()).clearStones();
            this.togglePlayer();
        } else {
            nextPit.incrementStoneCount();
            this.togglePlayer();
        }
        if (this.gameOver()) {
            this.notificationMessage = "Game Over. " + this.player1.whoWins(this.player2).getTitle() + " wins!";
        }

    }

    private Pit nextPit(final Pit pit) {
        int nextIndex = (pit.getIndex() + 1) % (PIT_COUNT + 2);
        if (nextIndex == this.opponent.getKalah().getIndex()) {
            nextIndex = (pit.getIndex() + 2) % (PIT_COUNT + 2);
        }
        return this.pits.get(nextIndex);
    }

    private boolean gameOver() {
        final int player1TotalStoneCount = this.player1.totalStoneCountInOwnPits(this.pits);
        final int player2TotalStoneCount = this.player2.totalStoneCountInOwnPits(this.pits);
        if (player1TotalStoneCount == 0) {
            this.player2.getKalah().incrementStoneCount(player2TotalStoneCount);
            return true;
        }
        if (player2TotalStoneCount == 0) {
            this.player1.getKalah().incrementStoneCount(player1TotalStoneCount);
            return true;
        }
        return false;
    }

    public boolean pitDisabled(final Pit pit) {
        if (pit.getStoneCount() == 0) {
            return true;
        }
        if (!this.currentPlayer.ownPit(pit.getIndex())) {
            return true;
        }
        return false;
    }

    // getters and setters
    public int getNumberOfStones() {
        return this.numberOfStones;
    }

    public void setNumberOfStones(final int numberOfStones) {
        this.numberOfStones = numberOfStones;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public void setPlayer1(final Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public void setPlayer2(final Player player2) {
        this.player2 = player2;
    }

    public boolean isGameStarted() {
        return this.gameStarted;
    }

    public Pit getSelectedPit() {
        return this.selectedPit;
    }

    public void setSelectedPit(final Pit selectedPit) {
        this.selectedPit = selectedPit;
    }

    public String getNotificationMessage() {
        return this.notificationMessage;
    }

    public void setNotificationMessage(final String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public List<Pit> getFirstRowPits() {
        return this.firstRowPits;
    }

    public void setFirstRowPits(final List<Pit> firstRowPits) {
        this.firstRowPits = firstRowPits;
    }

    public List<Pit> getSecondRowPits() {
        return this.secondRowPits;
    }

    public void setSecondRowPits(final List<Pit> secondRowPits) {
        this.secondRowPits = secondRowPits;
    }
}
