package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import farcore.lib.block.instance.BlockRock;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.util.Direction;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRCarvedRock extends TESRBase<TECustomCarvedStone>
{
	private static final float off = 0.25F;
	
	@Override
	public void renderTileEntityAt(TECustomCarvedStone tile, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		if(!tile.initialized)
		{
			tile.sendToServer(new PacketTEAsk(tile));
			return;
		}
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float)x, (float)y, (float)z);
		TextureAtlasSprite icon =
				getTexture(tile.rock().rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, tile.type));
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				for(int k = 0; k < 4; ++k)
				{
					float x1 = 0.25F * i;
					float y1 = 0.25F * j;
					float z1 = 0.25F * k;
					float x2 = 0.25F * (i + 1);
					float y2 = 0.25F * (j + 1);
					float z2 = 0.25F * (k + 1);
					if(tile.shouldSideRender(i, j, k, Direction.N))
					{
						face(tessellator, x2, y2, z1,
								-off, 0, 0, 0, -off, 0,
								x2, y1, x1, y2, icon);
					}
					if(tile.shouldSideRender(i, j, k, Direction.S))
					{
						face(tessellator, x1, y2, z2,
								off, 0, 0, 0, -off, 0,
								x1, y1, x2, y2, icon);
					}
					if(tile.shouldSideRender(i, j, k, Direction.W))
					{
						face(tessellator, x1, y2, z1,
								0, 0, off, 0, -off, 0,
								z1, y1, z2, y2, icon);
					}
					if(tile.shouldSideRender(i, j, k, Direction.E))
					{
						face(tessellator, x2, y2, z2,
								0, 0, -off, 0, -off, 0,
								z2, y1, z1, y2, icon);
					}
					if(tile.shouldSideRender(i, j, k, Direction.D))
					{
						face(tessellator, x1, y1, z1,
								0, 0, off, off, 0, 0,
								x2, z2, x1, y1, icon);
					}
					if(tile.shouldSideRender(i, j, k, Direction.U))
					{
						face(tessellator, x1, y2, z2,
								0, 0, -off, off, 0, 0,
								x1, z2, x2, z1, icon);
					}
				}
			}
		}
		GL11.glPopMatrix();
	}
}