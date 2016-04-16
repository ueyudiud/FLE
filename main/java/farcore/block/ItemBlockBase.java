package farcore.block;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.FarCoreSetup;
import fle.load.Langs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock
{
	protected BlockBase block;

	public ItemBlockBase(Block block)
	{
		super(block);
		this.block = (BlockBase) block;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String name1 = getUnlocalizedName();
		String name2 = block.getMetadataName(stack.getItemDamage());
		return name2 == null ? name1 : name1 + "@" + name2;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(stack.getUnlocalizedName() + ".name", block.getTranslateObject(stack));
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return hasSubtypes ? meta : 0;
	}
}