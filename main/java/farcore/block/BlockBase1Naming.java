package farcore.block;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.FarCore;
import farcore.block.item.ItemBlockBase;
import farcore.util.IUnlocalized;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockBase1Naming extends Block implements IUnlocalized
{
	/**
	 * The unlocalized name of block, override in block.
	 * @see net.minecraft.block.Block
	 */
	protected final String unlocalizedName;
	
	BlockBase1Naming(String unlocalized, Material Material)
	{
		super(Material);
		setBlockName(unlocalizedName = unlocalized);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName();
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName();
	}
	
	protected String getTranslateName()
	{
		return getUnlocalizedName();
	}
	
	protected String getTranslateName(int meta)
	{
		return getUnlocalizedName();
	}
	
	protected Object[] getTransObject()
	{
		return new Object[0];
	}
	
	@Override
	public final String getLocalizedName()
	{
		return FarCore.translateToLocal(getTranslateName(), getTransObject());
	}

	public String getLocalizedName(int meta)
	{
		return FarCore.translateToLocal(getTranslateName(meta), getTransObject());
	}
	
	public String getLocalizedName(ItemStack stack)
	{
		return getLocalizedName(stack.getItemDamage());
	}
	
	@Override
	public final String getUnlocalized()
	{
		return getUnlocalizedName();
	}
}