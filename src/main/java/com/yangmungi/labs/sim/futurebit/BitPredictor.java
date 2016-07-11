package com.yangmungi.labs.sim.futurebit;

import java.util.*;

/**
 * Created by Yangmun on 7/10/2016.
 */
public class BitPredictor {
    private final Random random;

    private BitSet fullHistory = new BitSet();
    private List<Map<BitSet, Integer>> historyAnalysis = new ArrayList<Map<BitSet, Integer>>();
    private int length = 0;

    public BitPredictor(Random random) {
        this.random = random;
    }

    public boolean getNextBoolean(final boolean inputBoolean) {
        if (inputBoolean) {
            fullHistory.set(length);
        }

        length++;

        int[] truePredictions = new int[length];
        int[] falsePredictions = new int[length];


        for (int subLength = 1; subLength <= length; subLength++) {
            final int from = length - subLength;
            final BitSet subSet = fullHistory.get(from, length);

            final int subLengthIndex = subLength - 1;
            final Map<BitSet, Integer> priorMap;
            if (historyAnalysis.size() < subLength) {
                priorMap = new HashMap<BitSet, Integer>();
                historyAnalysis.add(subLengthIndex, priorMap);
            } else {
                priorMap = historyAnalysis.get(subLengthIndex);
            }

            final int subSetCount;
            if (priorMap.containsKey(subSet)) {
                subSetCount = priorMap.get(subSet) + 1;
            } else {
                subSetCount = 1;
            }

            final int otherSubSetCount;
            final BitSet priorSubSet = subSet.get(0, subLengthIndex);
            final int priorSubSetLength = priorSubSet.length();
            // redundant variable for naming sake
            final BitSet otherSubSet = priorSubSet;
            otherSubSet.set(priorSubSetLength, !inputBoolean);
            if (priorMap.containsKey(otherSubSet)) {
                otherSubSetCount = priorMap.get(otherSubSet);
            } else {
                otherSubSetCount = 0;
            }

            if (inputBoolean) {
                truePredictions[subLengthIndex] = subSetCount;
                falsePredictions[subLengthIndex] = otherSubSetCount;
            } else {
                truePredictions[subLengthIndex] = otherSubSetCount;
                falsePredictions[subLengthIndex] = subSetCount;
            }

            priorMap.put(subSet, subSetCount);
        }

        // could save time by summing without saving all counts but I'm not actually
        // entirely sure if that's what I want to do
        int totalTrueCount = 0;
        for (int i = 0; i < truePredictions.length; i++) {
            int trueCount = truePredictions[i];
            //totalTrueCount += (trueCount + 1) * (i + 1);
            totalTrueCount += (trueCount + 1);
        }

        int totalFalseCount = 0;
        for (int i = 0; i < falsePredictions.length; i++) {
            int falseCount = falsePredictions[i];
            //totalFalseCount += (falseCount + 1) * (i + 1);
            totalFalseCount += (falseCount + 1);
        }

        final int totalCount = totalTrueCount + totalFalseCount;
        final int nextNumber = random.nextInt(totalCount);
        return nextNumber < totalTrueCount;
    }

    public static void main(String[] args) {
        final Random random = new Random(100);
        final BitPredictor predictor = new BitPredictor(random);

        int correct = 0, incorrect = 0;
        boolean predicted = false;
        for (int i = 0; i < 3000; i++) {
            final boolean inputBoolean = random.nextBoolean();
            if (predicted != inputBoolean) {
                incorrect++;
            } else {
                correct++;
            }

            predicted = predictor.getNextBoolean(inputBoolean);
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            //System.out.println(predicted);
        }

        System.out.println("Correct:" + correct + "; Incorrect:" + incorrect);
    }
}
