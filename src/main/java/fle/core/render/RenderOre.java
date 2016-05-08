package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderBase;
import farcore.lib.substance.SubstanceMineral;
import farcore.lib.substance.SubstanceRock;
import fle.core.tile.statics.TileEntityOre;

@SideOnly(Side.CLIENT)
public class RenderOre extends RenderBase
{
	private static final float OFFSET = 0.002F;
	
	@Override
	public void renderBlock()
	{
		if(render.hasOverrideBlockTexture())
		{
			icon = render.overrideBlockTexture;
			renderBlock(0F - OFFSET, 0F - OFFSET, 0F - OFFSET, 1F + OFFSET, 1F + OFFSET, 1F + OFFSET);
			return;
		}
		SubstanceMineral mineral;
		SubstanceRock rock;
		if(isItem())
		{
			mineral = SubstanceMineral.getSubstance(meta & 0x3FF);
			rock = SubstanceRock.getSubstance((meta >> 10));
		}
		else
		{
			if(world.getTileEntity(x, y, z) instanceof TileEntityOre)
			{
				TileEntityOre ore = (TileEntityOre) world.getTileEntity(x, y, z);
				mineral = ore.mineral;
				rock = ore.rock;
			}
			else
			{
				mineral = null;
				rock = null;
			}
		}
		if(mineral == null)
		{
			return;
		}
		setTexture(rock.icon);
		renderBlock(0F, 0F, 0F, 1F, 1F, 1F);
		setTexture(mineral.icon);
		renderBlock(0F - OFFSET, 0F - OFFSET, 0F - OFFSET, 1F + OFFSET, 1F + OFFSET, 1F + OFFSET);
	}
}