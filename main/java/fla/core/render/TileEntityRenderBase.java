package fla.core.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.primitives.SignedBytes;

public class TileEntityRenderBase extends TileEntitySpecialRenderer
{
	protected static RenderBlocks renderBlocks = new RenderBlocks();
	protected static RenderItem itemRenderer;
	
	protected World getWorldObj()
	{
		return field_147501_a.field_147550_f;
	}
	
	static
	{
		itemRenderer = new RenderItem()
		{
			@Override
			public byte getMiniBlockCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
			}

			@Override
			public byte getMiniItemCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 7) + 1);
			}

			@Override
			public boolean shouldBob()
			{
				return true;
			}

			@Override
			public boolean shouldSpreadItems()
			{
				return false;
			}
		};
		itemRenderer.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		
	}

}
