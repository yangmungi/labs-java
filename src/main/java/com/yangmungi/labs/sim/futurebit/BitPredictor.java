package com.yangmungi.labs.sim.futurebit;

import java.util.*;

/**
 * Created by Yangmun on 7/10/2016.
 */
public class BitPredictor {
    private BitSet fullHistory = new BitSet();
    private List<Map<BitSet, Integer>> historyAnalysis = new ArrayList<Map<BitSet, Integer>>();
    private int length = 0;

    public boolean getNextBoolean(final boolean inputBoolean) {
        if (inputBoolean) {
            fullHistory.set(length);
        }

        length++;

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



            priorMap.put(subSet, subSetCount);
        }

        return true;
    }

    public static void main(String[] args) {
        final BitPredictor predictor = new BitPredictor();
        final Random random = new Random(100);

        int correct = 0, incorrect = 0;
        boolean predicted = false;
        for (int i = 0; i < 1000; i++) {
            final boolean inputBoolean = random.nextBoolean();
            if (predicted != inputBoolean) {
                incorrect++;
            } else {
                correct++;
            }

            predicted = predictor.getNextBoolean(inputBoolean);
            //System.out.println(predicted);
        }

        System.out.println("Correct:" + correct + "; Incorrect:" + incorrect);
    }
}
