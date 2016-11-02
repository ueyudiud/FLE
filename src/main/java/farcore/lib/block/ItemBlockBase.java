package farcore.lib.block;

import farcore.lib.util.LanguageManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBase extends ItemBlock
{
	public static boolean placeflag = false;

	public BlockBase block;
	
	public ItemBlockBase(BlockBase block)
	{
		super(block);
		this.block = block;
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return block.getUnlocalizedName() + "@" + getDamage(stack);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return LanguageManager.translateToLocal(block.getTranslateNameForItemStack(stack));
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return block.getCreativeTabs();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState)
	{
		placeflag = true;
		boolean flag = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		placeflag = false;
		return flag;
	}
}