package com.phantazap.gensettings.client;

public class SkippableRandom {
	private long seed;
	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;
	private static final long modulus = mask + 1;

	public SkippableRandom(long seed) {
		this.seed = (seed ^ multiplier) & mask;
	}

	public int nextInt() {
		return next(32);
	}

	public int nextInt(int bound) {
		if ((bound & -bound) == bound) {
			return (int)((bound * (long)next(31)) >> 31);
		}

		int bits, val;
		do {
			bits = next(31);
			val = bits % bound;
		} while (bits - val + (bound - 1) < 0);
		return val;
	}

	public float nextFloat() {
		return next(24) / ((float)(1 << 24));
	}

	private int next(int bits) {
		seed = (seed * multiplier + addend) & mask;
		return (int)(seed >>> (48 - bits));
	}

	public void skip(long steps) {
		seed = skipLCG(seed, steps);
	}

	private static long skipLCG(long oldSeed, long steps) {
		long multiplierPow = 1;
		long addendSum = 0;

		long currentMultiplier = SkippableRandom.multiplier;
		long currentAddend = SkippableRandom.addend;

		while (steps > 0) {
			if ((steps & 1) != 0) {
				multiplierPow = (multiplierPow * currentMultiplier) % SkippableRandom.modulus;
				addendSum = (addendSum * currentMultiplier + currentAddend) % SkippableRandom.modulus;
			}

			currentAddend = (currentMultiplier + 1) * currentAddend % SkippableRandom.modulus;
			currentMultiplier = (currentMultiplier * currentMultiplier) % SkippableRandom.modulus;
			steps >>= 1;
		}

		return (oldSeed * multiplierPow + addendSum) % SkippableRandom.modulus;
	}
}