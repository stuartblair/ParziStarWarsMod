package com.parzivail.pswm.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.parzivail.pswm.Resources;

public class ItemHolotableBlock extends ItemBlock
{
	public ItemHolotableBlock(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + Resources.MODID + ".holoTable." + stack.getItemDamage();
	}
}
/*
 * Location: C:\Users\Colby\Downloads\Parzi's Star Wars Mod
 * v1.2.0-dev7.jar!\com\parzi\starwarsmod\items\ItemEndorBaseWall.class Java
 * compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */