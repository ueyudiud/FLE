package fle.resource.lib.infomation;

import farcore.block.BlockHook;
import farcore.fluid.BlockFluidBase;
import farcore.substance.Atom;
import farcore.util.Util;
import farcore.world.BlockPos;
import farcore.world.Direction;
import fle.core.init.Config;
import fle.init.Fluids;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class IngotAlkaliInfo extends IngotInfo
{
	private float ee;
	
	public IngotAlkaliInfo(Atom atom, int waterExplosionEffect)
	{
		super(atom);
		ee = (float) waterExplosionEffect / 100F;
	}
	
	private static final int[] range = {0, -1, 1, -2, 2};
	
	@Override
	public void onEntityUpdate(EntityItem item)
	{
		if(!Config.alkaliExplosion) return;
		if(item.worldObj.isRemote) return;
		BlockPos pos = new BlockPos(item);
		if(BlockHook.isBlockWater(pos) || 
				BlockHook.isBlockWater(pos.offset(Direction.NORTH)) || 
				BlockHook.isBlockWater(pos.offset(Direction.SOUTH)) || 
				BlockHook.isBlockWater(pos.offset(Direction.EAST)) || 
				BlockHook.isBlockWater(pos.offset(Direction.WEST)))// || (item.worldObj.isRaining() && item.worldObj.getTopSolidOrLiquidBlock((int) item.posX, (int) item.posZ) < item.posY))
		{
			explode(pos, item.getEntityItem().stackSize);
			item.setDead();
		}
	}
	
	@Override
	public void onItemUpdate(ItemStack stack, World world, int x, int y, int z)
	{
		if(!Config.alkaliExplosion) return;
		if(world.isRemote) return;
		if(stack.stackSize == 0) return;
		if(BlockHook.isBlockWater(new BlockPos(world, x, y, z)))// || (world.isRaining() && world.getTopSolidOrLiquidBlock(x, z) < y))
		{
			explode(new BlockPos(world, x, y, z), stack.stackSize);
			stack.stackSize = 0;
		}
	}
	
	protected void explode(BlockPos pos, int size)
	{
		pos.setToAir();
		pos.createExplosion(ee * size, true);
		int i = size;
		for(int a : range)
			for(int b : range)
			{
				BlockPos offset = pos.offset(a, 0, b);
				int amount = Math.min(Util.nextInt(i) + 1, 16);
				i -= amount;
				if(offset.isAir())
				{
					((BlockFluidBase) Fluids.hydrogen.getBlock())
					.setQuantaValue(offset.world(), offset.x, offset.y, offset.z, amount);
					offset.tickUpdate();
				}
				if(i == 0) return;
				amount = Math.min(Util.nextInt(i) + 1, 16);
				i -= amount;
				offset = offset.offset(0, 1, 0);
				if(offset.isAir())
				{
					((BlockFluidBase) Fluids.hydrogen.getBlock())
					.setQuantaValue(offset.world(), offset.x, offset.y, offset.z, amount);
					offset.tickUpdate();
				}
				if(i == 0) return;
			}
	}
}