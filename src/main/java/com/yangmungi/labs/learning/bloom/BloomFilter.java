package com.yangmungi.labs.learning.bloom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by yangmungi on 4/9/17.
 */
public class BloomFilter<Key> {
    public static final int DEFAULT_BITS = 100000000;
    // m bits array
    // k hash functions
    // k << m
    // optimal k = (m / n) * ln(2)

    // a bloom filter is like a HashSet, but without the actual Set...?

    private final int hashMethodsSize;
    private final Hasher[] hashMethods;

    private final BitSet[] lookupBitSets;

    public static <KeyType> BloomFilter<KeyType> buildDefault() throws NoSuchAlgorithmException {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final Hasher md5Hasher = new MessageDigestHasher(md5);

        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        final Hasher sha256Hasher = new MessageDigestHasher(sha256);

        final Checksum crc32 = new CRC32();
        final Hasher crc32Hasher = new ChecksumHasher(crc32);

        final Hasher[] hashMethods = {md5Hasher, sha256Hasher, crc32Hasher};
        return new BloomFilter<KeyType>(hashMethods, DEFAULT_BITS);
    }

    public BloomFilter(Hasher[] hashMethods, int bits) {
        this.hashMethods = hashMethods;
        hashMethodsSize = hashMethods.length;
        lookupBitSets = new BitSet[hashMethodsSize];
        for (int i = 0; i < hashMethodsSize; i++) {
            lookupBitSets[i] = new BitSet(bits);
        }
    }

    boolean definitelyDoesNotContain(Key key) {
        final int[] bitPositions = getHashPositions(key);

        int hashMatches = 0;
        for (int i = 0; i < bitPositions.length; i++) {
            int bitPosition = bitPositions[i];
            final boolean isSet = lookupBitSets[i].get(bitPosition);
            if (isSet) {
                hashMatches++;
            }
        }

        return hashMatches < hashMethodsSize;
    }

    void insert(Key key) {
        final int[] hashPositions = getHashPositions(key);
        for (int i = 0; i < hashPositions.length; i++) {
            int hashPosition = hashPositions[i];
            lookupBitSets[i].set(hashPosition);
        }
    }

    private int[] getHashPositions(Key key) {
        final int hashCode = key.hashCode();
        final Class<?> keyClass = key.getClass();
        final String canonicalName = keyClass.getCanonicalName();
        final String hashSource = canonicalName + "#" + hashCode;

        final int[] bitPositions = new int[hashMethodsSize];
        for (int i = 0; i < hashMethods.length; i++) {
            final BitSet lookupBitSet = lookupBitSets[i];
            final int size = lookupBitSet.size();
            final Hasher hashMethod = hashMethods[i];
            final int hash = hashMethod.buildHash(hashSource);
            // ignore modulus bias?
            final int bitPosition = hash % size;
            final int absBitPosition = Math.abs(bitPosition);
            bitPositions[i] = absBitPosition;
        }

        return bitPositions;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random random = new Random();

        final int byteRange = 'z' - 'a';

        final int stringSize = 1000;
        final int totalStrings = 1000000;
        final RandomStringGenerator randomStringGenerator = new RandomStringGenerator(random, byteRange, stringSize);
        int collisions = 0;

        final Runtime runtime = Runtime.getRuntime();

        runtime.gc();

        long startTime;
        startTime = System.currentTimeMillis();

        long startMemory;
        startMemory = runtime.totalMemory();

        BloomFilter<String> bloomFilter = BloomFilter.buildDefault();
        for (int i = 0; i < totalStrings; i++) {
            final String asString = randomStringGenerator.build();
            final boolean shouldNotContain = bloomFilter.definitelyDoesNotContain(asString);

            if (!shouldNotContain) {
                collisions++;
            }

            bloomFilter.insert(asString);
            final boolean insertFailed = bloomFilter.definitelyDoesNotContain(asString);
            if (insertFailed) {
                System.out.println("insertion failed:" + asString);
            }
        }

        final long bloomMemory = runtime.totalMemory() - startMemory;
        bloomFilter.definitelyDoesNotContain("hi");

        runtime.gc();

        final long bloomFilterTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        startMemory = runtime.totalMemory();

        final HashSet<String> hashSet = new HashSet<String>();
        int hashSetCollisions = 0;
        for (int i = 0; i < totalStrings; i++) {
            final String asString = randomStringGenerator.build();
            final boolean shouldNotContain = hashSet.contains(asString);

            if (shouldNotContain) {
                hashSetCollisions++;
            }

            hashSet.add(asString);
            final boolean insertFailed = hashSet.contains(asString);
            if (!insertFailed) {
                System.out.println("insertion failed:" + asString);
            }
        }

        final long hashSetMemory = runtime.totalMemory() - startMemory;

        hashSet.contains("hi");

        final long hashSetTime = System.currentTimeMillis() - startTime;

        System.out.println("BloomFilter collisions:" + collisions + " total:" + totalStrings +
                " time:" + bloomFilterTime + " memory:" + bloomMemory);
        System.out.println("HashSet collisions:" + hashSetCollisions + " total:" + totalStrings +
                " time:" + hashSetTime + " memory:" + hashSetMemory);
    }

    private static class MessageDigestHasher implements Hasher {
        private final MessageDigest messageDigest;

        public MessageDigestHasher(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override
        public int buildHash(String key) {
            final byte[] hashSourceBytes = key.getBytes();
            final byte[] digest = messageDigest.digest(hashSourceBytes);
            // truncate digest
            // TODO check endianness
            int bitPosition = 0;
            if (digest.length >= 4) {
                bitPosition += digest[0];
                bitPosition += digest[1] << 8;
                bitPosition += digest[2] << 16;
                bitPosition += digest[3] << 24;
            }

            return bitPosition;
        }
    }

    private static class ChecksumHasher implements Hasher {
        private final Checksum checksum;

        public ChecksumHasher(Checksum checksum) {
            this.checksum = checksum;
        }

        @Override
        public int buildHash(String key) {
            checksum.reset();
            final byte[] bytes = key.getBytes();
            checksum.update(bytes, 0, bytes.length);
            return (int) checksum.getValue();
        }
    }

    private static class RandomStringGenerator {
        private Random random;
        private int byteRange;
        private int stringSize;

        public RandomStringGenerator(Random random, int byteRange, int stringSize) {
            this.random = random;
            this.byteRange = byteRange;
            this.stringSize = stringSize;
        }

        public String build() {
            final byte[] randomBytes = new byte[stringSize];
            random.nextBytes(randomBytes);

            final byte[] asciBytes = new byte[stringSize];
            for (int j = 0; j < randomBytes.length; j++) {
                byte randomByte = randomBytes[j];
                final int offset = Math.abs(randomByte % byteRange);
                asciBytes[j] = (byte) ('a' + offset);
            }

            return new String(asciBytes);
        }
    }
}
