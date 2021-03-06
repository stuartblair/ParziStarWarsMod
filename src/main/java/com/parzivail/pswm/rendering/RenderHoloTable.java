package com.parzivail.pswm.rendering;

import java.awt.Color;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import com.parzivail.pswm.StarWarsMod;
import com.parzivail.pswm.tileentities.TileEntityHoloTableBase;
import com.parzivail.util.ui.RenderHelper;

public class RenderHoloTable extends TileEntitySpecialRenderer
{
	public RenderHoloTable()
	{
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p)
	{
		GL11.glPushMatrix();

		TileEntityHoloTableBase table = (TileEntityHoloTableBase)te;

		for (Object e : te.getWorldObj().getEntitiesWithinAABB(Entity.class, te.getRenderBoundingBox().expand(table.getSideLength() / 2 - 6, table.getSideLength() / 2 - 2, table.getSideLength() / 2 - 6)))
			if (e instanceof Entity)
			{
				if (e == StarWarsMod.mc.thePlayer)
					continue;
				Entity entity = (Entity)e;
				GL11.glPushMatrix();
				Vec3 pos = StarWarsMod.mc.thePlayer.getPosition(p);
				GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
				float dx = (float)(te.xCoord - entity.posX) / -16f;
				float dy = (float)(te.yCoord - entity.posY) / -16f;
				float dz = (float)(te.zCoord - entity.posZ) / -16f;
				GL11.glTranslatef(te.xCoord + 0.5f + dx, te.yCoord + 1.04f + dy + table.getOffset() / 16f, te.zCoord + 0.5f + dz);
				GL11.glScalef(1 / 16f, 1 / 16f, 1 / 16f);
				GL11.glTranslatef(-1, 0, -1);
				GL11.glColor4f(1, 1, 1, 1);
				RenderHelper.renderEntity(entity);
				GL11.glPopMatrix();
			}

		if (table.isMapSetup())
		{
			GL11.glPushMatrix();
			Vec3 pos = StarWarsMod.mc.thePlayer.getPosition(p);
			GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
			GL11.glTranslatef(te.xCoord + 0.5f, te.yCoord + 1 + 0.001f + table.getOffset() / 16f, te.zCoord + 0.5f);
			GL11.glScalef(1 / 16f, 1 / 16f, 1 / 16f);

			GL11.glDisable(GL11.GL_LIGHTING); // fix for dimming bug!
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);

			GL11.glLineWidth(4);

			Color c = table.getRGB();
			GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);

			boolean solid = false;

			for (int i = 0; i < table.getMap().length; i++)
			{
				int nx = i % table.getSideLength();
				int nz = (int)Math.floor(i / table.getSideLength());

				float o = table.getOffset() / 16f;

				int s = table.getSideLength() / 2;

				if (solid)
				{
					GL11.glBegin(GL11.GL_TRIANGLE_FAN);
					GL11.glVertex3f(nx - s, table.getMap()[i] - te.yCoord + o, nz - s);
					GL11.glVertex3f(nx - s + 1, table.getMap()[i] - te.yCoord + o, nz - s);
					GL11.glVertex3f(nx - s, table.getMap()[i] - te.yCoord + o, nz - s + 1);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_TRIANGLE_FAN);
					GL11.glVertex3f(nx - s + 1, table.getMap()[i] - te.yCoord + o, nz - s + 1);
					GL11.glVertex3f(nx - s + 1, table.getMap()[i] - te.yCoord + o, nz - s);
					GL11.glVertex3f(nx - s, table.getMap()[i] - te.yCoord + o, nz - s + 1);
					GL11.glEnd();
				}
				else
				{
					GL11.glBegin(GL11.GL_LINE_LOOP);
					GL11.glVertex3d(nx - s - 1, table.getMap()[i] - te.yCoord + o, nz - s - 1);
					GL11.glVertex3d(nx - s, table.getMap()[i] - te.yCoord + o, nz - s);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINE_LOOP);
					GL11.glVertex3d(nx - s - 1, table.getMap()[i] - te.yCoord + o, nz - s);
					GL11.glVertex3d(nx - s, table.getMap()[i] - te.yCoord + o, nz - s - 1);
					GL11.glEnd();
				}
			}

			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING); // end of fix
			GL11.glPopMatrix();
		}
		else
			table.setupMap();

		GL11.glPopMatrix();
	}
}
/*
 * Location: C:\Users\Colby\Downloads\Parzi's Star Wars Mod
 * v1.2.0-dev7.jar!\com\parzi\starwarsmod\rendering\RenderMV.class Java compiler
 * version: 6 (50.0) JD-Core Version: 0.7.1
 */