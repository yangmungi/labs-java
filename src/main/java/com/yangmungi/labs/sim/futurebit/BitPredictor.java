package com.yangmungi.labs.sim.futurebit;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        int totalFalseCount = 0;

        for (int i = 0; i < length; i++) {
//            int multiplier = i + 1;
            int trueCount = truePredictions[i];

//            totalTrueCount += (trueCount + 1) * multiplier;
            totalTrueCount += (trueCount + 1);

            int falseCount = falsePredictions[i];
//            totalFalseCount += (falseCount + 1) * multiplier;
            totalFalseCount += (falseCount + 1);

            System.out.print("(" + i + " T:" + trueCount + " F:" + falseCount + ")");
        }


        // resolution
//        final int totalCount = totalTrueCount + totalFalseCount;
//        final int nextNumber = random.nextInt(totalCount);
//        return nextNumber < totalFalseCount;
        return totalFalseCount < totalTrueCount;
    }

    public static void main(String[] args) {
        final Random random = new Random(100);
        final BitPredictor predictor = new BitPredictor(random);

        final AtomicInteger counterMessage = new AtomicInteger(0);
        final Timer timer = new Timer(true);
        TimerTask printer = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Processed:" + counterMessage);
            }
        };

        //timer.scheduleAtFixedRate(printer, 0L, 1000L);

        int correct = 0, incorrect = 0;
        boolean predicted = false;
        for (int i = 0; i < 100; i++) {
            final boolean inputBoolean = random.nextBoolean();
            if (predicted != inputBoolean) {
                incorrect++;
            } else {
                correct++;
            }

            System.out.println("P:" + predicted + " I:" + inputBoolean);

            predicted = predictor.getNextBoolean(inputBoolean);

            counterMessage.lazySet(i);
        }

        System.out.println("=============");
        System.out.println("Correct:" + correct);
        System.out.println("Incorrect:" + incorrect);
    }
}
