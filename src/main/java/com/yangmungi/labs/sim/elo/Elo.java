package com.yangmungi.labs.sim.elo;

import java.util.*;

public class Elo {
    public static void main(String[] args) {
        final Random random = new Random();

        // create players
        final Set<Player> allPlayers = new HashSet<Player>();

        final int numberOfPlayers = 100000;

        final int skillBase = 1000;

        // talent distribution
        for (int i = 0; i < numberOfPlayers; i++) {
            final double talentBias = random.nextGaussian();
            final long talentFactor = Math.round(talentBias * skillBase);
            final Player player = new Player((int) talentFactor);
            allPlayers.add(player);
        }

        // not all players would be playing all the time
        final double playFactorModifier = 0.5;

        // there might be a better way to simulate players playing
        // players would play in a streak

        final int playPeriods = 100;
        for (int playPeriod = 0; playPeriod < playPeriods; playPeriod++) {
            // determine simultaneous queue volume
            final List<Player> playersPlaying = new ArrayList<Player>();
            final double playFactor = random.nextGaussian() + playFactorModifier;
            for (Player player : allPlayers) {
                final double playerPlayFactor = random.nextGaussian();
                if (playerPlayFactor > playFactor) {
                    playersPlaying.add(player);
                }
            }

            // sub-period player availability for match making
            System.out.println("players playing:" + playersPlaying.size());

            // match making
            // what is the goal of match making?
            int currentAvailablePlayers = playersPlaying.size();

            final int victoryScoreGap = 10;
            final int maximumScore = 100;

            while (currentAvailablePlayers > 12) {
                Team red = new Team();
                Team blue = new Team();
                boolean nextIsRed = true;

                while (blue.canAddPlayers()) {
                    final int playerSelected = random.nextInt(currentAvailablePlayers);
                    final Player player = playersPlaying.remove(playerSelected);

                    if (nextIsRed) {
                        red.addPlayer(player);
                    } else {
                        blue.addPlayer(player);
                    }

                    nextIsRed = !nextIsRed;

                    currentAvailablePlayers--;
                }

                // simulate match

                int redScore = 0;
                int blueScore = 0;

                while (redScore - blueScore < victoryScoreGap && blueScore - redScore < victoryScoreGap && redScore + blueScore < maximumScore) {
                    // matches have rounds
                    // each round, player talent and randomness
                    final List<Player> redPlayers = red.getPlayers();

                    int redRoundSkill = 0;
                    for (Player player : redPlayers) {
                        final int talent = player.getTalent();
                        final int skill = player.getSkill();
                        final int fluctuation = random.nextInt(500);

                        redRoundSkill += talent + fluctuation + skill;
                    }

                    final List<Player> bluePlayers = blue.getPlayers();

                    int blueRoundSkill = 0;
                    for (Player player : bluePlayers) {
                        final int talent = player.getTalent();
                        final int skill = player.getSkill();
                        final int fluctuation = random.nextInt(500);

                        blueRoundSkill += talent + fluctuation + skill;
                    }

                    if (redRoundSkill > blueRoundSkill) {
                        redScore++;
                    } else if (redRoundSkill < blueRoundSkill) {
                        blueScore++;
                    }

                    // winning by X rounds results in victory
                }

                final String scoreDebug = "red(" + red.getTotalTalent() + "):" + redScore + " blue(" + blue.getTotalTalent() + "):" + blueScore;

                // what should skill look like?
                // it should increase slowly, increase quickly, plateau, increase slowly?

                // here is the feedback for the match-making system...
                if (redScore - blueScore < victoryScoreGap) {
                    System.out.println("red team wins " + scoreDebug);
                } else if (blueScore - redScore < victoryScoreGap) {
                    System.out.println("blue team wins " + scoreDebug);
                } else {
                    System.out.println("draw " + scoreDebug);
                }
            }
        }

    }

    private static class Player {
        private final int talent;
        private int experience = 0;
        private int skill;

        private Player(int talent) {
            this.talent = talent;
        }

        public int getTalent() {
            return talent;
        }

        public int getSkill() {
            return skill;
        }
    }

    private static class Team {
        private final List<Player> players = new ArrayList<Player>(6);

        public void addPlayer(Player player) {
            players.add(player);
        }

        public boolean canAddPlayers() {
            return players.size() < 6;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public int getTotalTalent() {
            int total = 0;
            for (Player player : players) {
                final int talent = player.getTalent();
                total += talent;
            }

            return total;
        }
    }
}
