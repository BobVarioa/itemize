package com.bobvarioa.mobitems.render;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class DummyRandomSource implements RandomSource {
    @Override
    public RandomSource fork() {
        return new DummyRandomSource();
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return null;
    }

    @Override
    public void setSeed(long pSeed) {

    }

    @Override
    public int nextInt() {
        return 0;
    }

    @Override
    public int nextInt(int pBound) {
        return 0;
    }

    @Override
    public long nextLong() {
        return 0;
    }

    @Override
    public boolean nextBoolean() {
        return false;
    }

    @Override
    public float nextFloat() {
        return 0;
    }

    @Override
    public double nextDouble() {
        return 0;
    }

    @Override
    public double nextGaussian() {
        return 0;
    }
}
