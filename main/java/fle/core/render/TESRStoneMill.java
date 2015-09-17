package fle.core.render;

import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.core.te.TileEntityStoneMill;

public class TESRStoneMill extends TESRBase<TileEntityStoneMill>
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/model/stone_mill.png");
	
	@Override
	public void renderTileEntityAt(TileEntityStoneMill tile, double xPos, double yPos,
			double zPos)
	{
		//model.setRotation(tile.getRotation());
		bindTexture(locate);
		//model.renderTileEntityAt(tile, 1.0F);
	}
}