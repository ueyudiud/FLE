package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.cg.ICG;
import flapi.cg.RecipesTab;
import flapi.item.ItemFleMetaBase;
import fle.FLE;

public class BehaviorGuideBook extends BehaviorBase implements ICG
{
	RecipesTab tab;
	
	public BehaviorGuideBook(RecipesTab aTab)
	{
		tab = aTab;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemFleMetaBase item,
			ItemStack itemstack, World aWorld, EntityPlayer player)
	{
		if(aWorld.isRemote)
		{
			player.openGui(FLE.MODID, -4, aWorld, player.serverPosX, player.serverPosY, player.serverPosZ);
		}
		return itemstack;
	}

	@Override
	public RecipesTab getBookTab(ItemStack itemstack)
	{
		return tab;
	}
}