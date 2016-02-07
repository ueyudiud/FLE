package fle.resource.block.auto;

import farcore.render.block.BlockRenderBase;
import farcore.render.block.ItemBlockRender;
import farcore.substance.Substance;
import flapi.FleResource;
import fle.core.enums.EnumRockSize;
import fle.resource.tile.TileEntityStoneChip;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class RenderStoneChip extends BlockRenderBase
{
	@Override
	public void renderBlock()
	{
		EnumRockSize size;
		if(isItem())
		{
			setTexture(getIcon(
					((BlockUniversalStoneChip) block).getRock(ItemBlockRender.getStack())));
			size = ((BlockUniversalStoneChip) block).getRockSize(ItemBlockRender.getStack());
		}
		else
		{
			TileEntityStoneChip tile = (TileEntityStoneChip) world.getTileEntity(x, y, z);
			if(tile != null)
			{
				setTexture(getIcon(tile.rock));
				size = tile.size;
			}
			else
			{
				setTexture(Blocks.stone);
				size = EnumRockSize.small;
			}	
		}
		double minX = (double) .5F - size.getWidth() * .5F,
				minY = (double) 0,
				minZ = (double) .5F - size.getWidth() * .5F,
				maxX = (double) .5F + size.getWidth() * .5F,
				maxY = (double) size.getHeiht(),
				maxZ = (double) .5F + size.getWidth() * .5F;
		renderBlock(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	private IIcon getIcon(Substance rock)
	{
		return ResourceIcons.rock[FleResource.rock.serial(rock)];
	}
}