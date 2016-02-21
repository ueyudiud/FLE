package farcore.block;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import flapi.FleAPI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockFleName1 extends Block
{
	/**
	 * The unlocalized name of block, override in block.
	 * @see net.minecraft.block.Block
	 */
	protected final String unlocalizedName;
	
	protected BlockFleName1(String unlocalized, Material material)
	{
		super(material);
		setBlockName(unlocalizedName = unlocalized);
		GameRegistry.registerBlock(this, ItemFleBlock.class, unlocalized);
	}
	protected BlockFleName1(Class<? extends ItemBlock> clazz, String unlocalized, Material material)
	{
		super(material);
		setBlockName(unlocalizedName = unlocalized);
		GameRegistry.registerBlock(this, clazz, unlocalized);
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
	
	protected String getTranslateName(int i)
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
		return FleAPI.langManager.translateToLocal(getTranslateName(), getTransObject());
	}

	public String getLocalizedName(int meta)
	{
		return FleAPI.langManager.translateToLocal(getTranslateName(meta), getTransObject());
	}
	
	public String getLocalizedName(ItemStack stack)
	{
		return getLocalizedName(stack.getItemDamage());
	}
	
	/**
	 * Add tool tip on GUI when display this item on slot.
	 * @param aStack
	 * @param aList
	 * @param aPlayer
	 */
	public void addInformation(ItemStack stack, List<String> list, EntityPlayer player)
	{
		;
	}
}