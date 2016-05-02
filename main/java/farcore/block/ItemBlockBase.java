package farcore.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.interfaces.item.ILocalizedRegisterListener;
import farcore.util.LanguageManager;
import fle.load.Langs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlockBase extends ItemBlock implements ILocalizedRegisterListener
{
	protected BlockBase block;

	public ItemBlockBase(Block block)
	{
		super(block);
		this.block = (BlockBase) block;
	}
	public ItemBlockBase(Block block, Boolean hasSub)
	{
		super(block);
		this.block = (BlockBase) block;
		this.hasSubtypes = hasSub;
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		block.registerLocalizedName(manager);		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String name1 = getUnlocalizedName();
		String name2;
		try
		{
			name2 = block.getMetadataName(stack.getItemDamage());
		}
		catch(Throwable throwable)
		{
			name2 = null;
		}
		return name2 == null ? name1 : name1 + "@" + name2;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(getUnlocalizedName(stack) + ".name", block.getTranslateObject(stack));
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return hasSubtypes ? meta : 0;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return block.getIcon(0, meta);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, block, metadata, 3))
		{
			return false;
		}
		if (world.getBlock(x, y, z) == block)
		{
			block.onBlockPlacedBy(world, x, y, z, player, stack);
			block.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}
}