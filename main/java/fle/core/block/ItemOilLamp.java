package fle.core.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;
import fle.api.FleValue;
import fle.api.block.ItemFleBlock;
import fle.core.init.IB;

public class ItemOilLamp extends ItemFleBlock
{
	public ItemOilLamp(Block aBlock)
	{
		super(aBlock);
		hasSubtypes = true;
		setMaxStackSize(1);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, field_150939_a, metadata, 3))
		{
			return false;
		}
		BlockOilLamp.setOilLamp(world, x, y, z, getToolMaterial(stack), getToolAmount(stack), false, false);
		if (world.getBlock(x, y, z) == field_150939_a)
	    {
			field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
	        field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
	    }
		
		return true;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1D - (double) getToolAmount(stack) / (double) FleValue.CAPACITY[4];
	}
	
	public static ItemStack getToolMaterial(ItemStack aStack)
	{
		return new ItemStack(GameData.getBlockRegistry().getObject(setupNBT(aStack).getString("Material")), 1, aStack.stackTagCompound.getShort("MaterialMeta"));
	}
	
	public static int getToolAmount(ItemStack aStack)
	{
		return setupNBT(aStack).getShort("Amount");
	}
	
	public static ItemStack a(Block aMaterial, int aMeta)
	{
		ItemStack ret = new ItemStack(IB.oilLamp);
		setToolMaterial(ret, aMaterial, aMeta);
		return ret;
	}
	
	public static void setToolMaterial(ItemStack aStack, Block aMaterial, int aMeta)
	{
		setupNBT(aStack).setString("Material", GameData.getBlockRegistry().getNameForObject(aMaterial));
		aStack.stackTagCompound.setShort("MaterialMeta", (short) aMeta);
	}
	
	public static void setAmount(ItemStack aStack, int amount)
	{
		setupNBT(aStack).setShort("Amount", (short) amount);
	}
	
	private Map<String, IIcon> iconMap = new HashMap();
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		itemIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		iconMap.put("lamp", register.registerIcon(FleValue.TEXTURE_FILE + ":tools/oillamp/lamp"));
		iconMap.put("oil", register.registerIcon(FleValue.TEXTURE_FILE + ":tools/oillamp/oil"));
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) 
	{
		return pass == 0 ? iconMap.get("lamp") : getToolAmount(stack) != 0 ? iconMap.get("oil") : itemIcon;
	}
}