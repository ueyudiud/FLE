package farcore.block;

import farcore.FarCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemStandardFluid extends ItemBlock
{
	protected BlockStandardFluid block;

	public ItemStandardFluid(Block block)
	{
		super(block);
		this.block = (BlockStandardFluid) block;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return block.getFluid().getUnlocalizedName();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(block.getFluid().getUnlocalizedName() + ".name");
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata)
	{
		Block old = world.getBlock(x, y, z);
		if(old == block)
		{
			int total = block.getQuantaValue(world, x, y, z) + metadata + 1;
			block.setFluidLevel(world, x, y, z, total, false);
			return true;
		}
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
	}
}