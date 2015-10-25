package fle.core.block.tank;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.api.block.ItemFleBlock;
import fle.api.material.MaterialRock;
import fle.api.util.Register;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.te.tank.TileEntityMultiTank;
import fle.core.util.TankBlockInfo;

public class ItemTankBlock extends ItemFleBlock
{	
	static final Register<TankBlockInfo> register = new Register();
	
	public ItemTankBlock(Block aBlock)
	{
		super(aBlock);
		register.register(TankBlockInfo.DEFAULT, "DEFAULT");
		register.register(new TankBlockInfo(Blocks.stonebrick, 0), "stoneBrickI");
		register.register(new TankBlockInfo(Blocks.stonebrick, 1), "stoneBrickII");
		register.register(new TankBlockInfo(Blocks.stonebrick, 2), "stoneBrickIII");
		register.register(new TankBlockInfo(Blocks.stonebrick, 3), "stoneBrickIV");
		register.register(new TankBlockInfo(Blocks.sandstone, 0), "sandBrickI");
		register.register(new TankBlockInfo(Blocks.sandstone, 1), "sandBrickII");
		register.register(new TankBlockInfo(Blocks.sandstone, 2), "sandBrickIII");
		register.register(new TankBlockInfo(Blocks.quartz_block, 0), "quartzBlockI");
		register.register(new TankBlockInfo(Blocks.quartz_block, 1), "quartzBlockII");
		register.register(new TankBlockInfo(Blocks.quartz_block, 2), "quartzBlockIII");
		register.register(new TankBlockInfo(Blocks.nether_brick), "netherBrick");
		register.register(new TankBlockInfo(IB.rock, MaterialRock.getOreID(Materials.CompactStone)), "compactStone");
		register.register(new TankBlockInfo(Blocks.bedrock), "bedrock");
		register.register(new TankBlockInfo(Blocks.end_stone), "end");
		hasSubtypes = true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		int meta = stack.getItemDamage() & 15;
		if (!world.setBlock(x, y, z, block, meta, 3))
		{
			return false;
		}
		if (world.getTileEntity(x, y, z) instanceof TileEntityMultiTank)
		{
			TileEntityMultiTank tank = (TileEntityMultiTank) world.getTileEntity(x, y, z);
			tank.setBlock(c(stack));
		}
		if (world.getBlock(x, y, z) == block)
	    {
			block.onBlockPlacedBy(world, x, y, z, player, stack);
			block.onPostBlockPlaced(world, x, y, z, meta);
		}
		return true;
	}
	
	public static ItemStack a(String tag, int meta)
	{
		return new ItemStack(IB.tank, 1, (register.serial(tag) << 4) + meta);
	}
	
	public static TankBlockInfo b(String tag)
	{
		return register.contain(tag) ? register.get(tag) : TankBlockInfo.DEFAULT;
	}
	
	public static TankBlockInfo c(ItemStack tag)
	{
		return register.get(tag.getItemDamage() >> 4);
	}

	public static int d(TankBlockInfo info)
	{
		return register.serial(info);
	}

	public static String e(TankBlockInfo info)
	{
		return register.name(info);
	}
}