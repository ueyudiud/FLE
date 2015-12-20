package fle.core.block.machine;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.material.MaterialAbstract;
import fle.core.block.ItemSubTile;
import fle.core.init.IB;
import fle.energy.block.TileEntityThermalWire;

public class ItemThermalWire extends ItemSubTile
{
	public ItemThermalWire(Block aBlock)
	{
		super(aBlock);
	}

	public static ItemStack a(MaterialAbstract material)
	{
		return a(material, 1);
	}
	public static ItemStack a(MaterialAbstract material, int size)
	{
		ItemStack stack = new ItemStack(IB.thermalWire, size);
		setupNBT(stack).setString("Material", MaterialAbstract.getMaterialRegistry().name(material));
		return stack;
	}
	
	public static MaterialAbstract b(ItemStack aStack)
	{
		return MaterialAbstract.getMaterialRegistry().get(setupNBT(aStack).getString("Material"));
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
    	short tDamage = (short) getDamage(stack);
    	MaterialAbstract material = b(stack);
    	if (!world.setBlock(x, y, z, block, 0, 3))
    	{
    		return false;
    	}
    	if (world.getTileEntity(x, y, z) instanceof TileEntityThermalWire)
    	{
    		world.getTileEntity(x, y, z).blockMetadata = tDamage;
    		((TileEntityThermalWire) world.getTileEntity(x, y, z)).init(material);
    	}

    	if (world.getBlock(x, y, z) == block)
    	{
    		block.onBlockPlacedBy(world, x, y, z, player, stack);
    		block.onPostBlockPlaced(world, x, y, z, tDamage);
    	}

    	return true;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		try
		{
			return b(stack).getPropertyInfo().getColors()[0];
		}
		catch(Throwable e)
		{
			return 0xFFFFFF;
		}
	}
}