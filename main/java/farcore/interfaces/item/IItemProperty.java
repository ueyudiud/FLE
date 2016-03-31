package farcore.interfaces.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemProperty
{
	void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag);
	
	boolean canHarvestBlock(Block block, ItemStack stack);

	int getItemStackLimit(ItemStack stack);

	int getMaxItemUseDuration(ItemStack stack);

	boolean hasContainerItem(ItemStack stack);
	
	ItemStack getContainerItem(ItemStack stack);
	
	float getDigSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int metadata);

	Set<String> getToolClasses(ItemStack stack);

	int getHarvestLevel(ItemStack stack, String toolClass);
	
	EnumAction getItemUseAction(ItemStack stack);

	EnumRarity getRarity(ItemStack stack);
	
	void addAttributeModifiers(Multimap map, ItemStack stack);
}