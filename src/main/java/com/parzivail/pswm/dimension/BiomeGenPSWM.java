package com.parzivail.pswm.dimension;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;

public class BiomeGenPSWM extends BiomeGenBase
{
	public BiomeGenPSWM(int biomeId)
	{
		super(biomeId);
	}

	public WorldGenAbstractTree getTreeAt(World world, Random rand, int x, int y, int z)
	{
		return new WorldGenForest(true, true);
	}
}
