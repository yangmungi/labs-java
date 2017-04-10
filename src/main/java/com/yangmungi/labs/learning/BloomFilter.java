package com.yangmungi.labs.learning;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * Created by yangmungi on 4/9/17.
 */
public class BloomFilter<Key> {
    // m bits array
    // k hash functions
    // k << m
    // optimal k = (m / n) * ln(2)

    // a bloom filter is like a HashSet, but without the actual Set...?

    private final int hashMethodsSize;
    private final MessageDigest[] hashMethods;

    private final BitSet lookupBitSet;

    public static <KeyType> BloomFilter<KeyType> buildDefault() throws NoSuchAlgorithmException {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        final MessageDigest[] messageDigests = new MessageDigest[]{md5, sha256};

        return new BloomFilter<KeyType>(messageDigests, 200000000);
    }

    public BloomFilter(MessageDigest[] hashMethods, int bits) {
        this.hashMethods = hashMethods;
        hashMethodsSize = hashMethods.length;
        lookupBitSet = new BitSet(bits);
    }

    boolean definitelyDoesNotContain(Key key) {
        final int[] bitPositions = getHashPositions(key);

        boolean definitelyNotInSet = true;
        for (int bitPosition : bitPositions) {
            final boolean isSet = lookupBitSet.get(bitPosition);
            if (isSet) {
                definitelyNotInSet = false;
                break;
            }
        }

        return definitelyNotInSet;
    }

    void insert(Key key) {
        final int[] hashPositions = getHashPositions(key);
        for (int hashPosition : hashPositions) {
            lookupBitSet.set(hashPosition);
        }
    }

    private int[] getHashPositions(Key key) {
        final int hashCode = key.hashCode();
        final Class<?> keyClass = key.getClass();
        final String canonicalName = keyClass.getCanonicalName();
        final String hashSource = canonicalName + "#" + hashCode;

        final int[] bitPositions = new int[hashMethodsSize];
        final byte[] hashSourceBytes = hashSource.getBytes();
        for (int i = 0; i < hashMethods.length; i++) {
            final MessageDigest hashMethod = hashMethods[i];
            final byte[] digest = hashMethod.digest(hashSourceBytes);
            // truncate digest
            // TODO check endianness
            int bitPosition = 0;
            if (digest.length >= 4) {
                bitPosition += digest[0];
                bitPosition += digest[1] << 8;
                bitPosition += digest[2] << 16;
                bitPosition += digest[3] << 24;
            }

            bitPositions[i] = Math.abs(bitPosition);
        }
        return bitPositions;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        BloomFilter<String> bloomFilter = BloomFilter.buildDefault();
        bloomFilter.insert("hi");
        System.out.println(bloomFilter.definitelyDoesNotContain("hi"));
        System.out.println(bloomFilter.definitelyDoesNotContain("bye"));

    }
}
