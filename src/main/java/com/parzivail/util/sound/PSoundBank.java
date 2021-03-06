package com.parzivail.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;

public class PSoundBank
{
	private SoundHandler handler;

	public PSoundBank()
	{
		this.handler = Minecraft.getMinecraft().getSoundHandler();
	}

	public boolean isPlaying(ISound sound)
	{
		return this.handler.isSoundPlaying(sound);
	}

	public void play(ISound sound)
	{
		if (this.isPlaying(sound))
			this.handler.stopSound(sound);
		this.handler.playSound(sound);
	}

	public void playIfNotAlready(ISound sound)
	{
		if (!this.isPlaying(sound))
			this.handler.playSound(sound);
	}

	public void stop(ISound sound)
	{
		if (this.isPlaying(sound))
			this.handler.stopSound(sound);
	}
}
