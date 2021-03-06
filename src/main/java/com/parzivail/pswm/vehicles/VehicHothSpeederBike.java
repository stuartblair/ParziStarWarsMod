package com.parzivail.pswm.vehicles;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

import com.parzivail.pswm.StarWarsMod;
import com.parzivail.util.vehicle.VehicleLandBase;

public class VehicHothSpeederBike extends VehicleLandBase
{
	public VehicHothSpeederBike(World par1World)
	{
		super(par1World);
		this.setSize(1.0F, 2.0F);
		this.vehicYOffset = -0.4F;
		this.moveModifier = 3.0F;
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1.0D);
	}

	@Override
	public void dropFewItems(boolean par1, int par2)
	{
		this.dropItem(StarWarsMod.spawnHothSpeederBike, 1);
	}

	@Override
	public String getCommandSenderName()
	{
		if (this.hasCustomNameTag())
			return this.getCustomNameTag();
		return "74-Z Speeder Bike";
	}

	@Override
	public String getMovingSound()
	{
		return "vehicle.speeder.move";
	}
}
/*
 * Location: C:\Users\Colby\Downloads\Parzi's Star Wars Mod
 * v1.2.0-dev7.jar!\com\parzi\starwarsmod\vehicles\VehicHothSpeederBike.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */