package flapi.item.interfaces;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IItemBehaviour<E extends Item>
{
	public abstract boolean onLeftClickEntity(E item, ItemStack itemstack, EntityPlayer player, Entity entity);
  
	public abstract boolean onItemUse(E item, ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, float xPos, float yPos, float zPos);
  
	public abstract boolean onItemUseFirst(E item, ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, float xPos, float yPos, float zPos);
  
	public abstract ItemStack onItemRightClick(E item, ItemStack itemstack, World paramWorld, EntityPlayer player);
  
	public abstract void getAdditionalToolTips(E item, List<String> list, ItemStack itemstack, boolean aFlag);
  
	public abstract void onUpdate(E item, ItemStack itemstack, World world, Entity entity, int tick, boolean flag);
  
	public abstract boolean onItemDamageBlock(E item, ItemStack aStack, Block aBlock, EntityLivingBase aEntity, World aWorld, int aX, int aY, int aZ);

	public abstract float getDigSpeed(E item, ItemStack aStack, Block aBlock, int aMetadata);

	public abstract boolean canHarvestBlock(E item,	Block aBlock, int aMetadata, ItemStack aStack);

	public abstract boolean onEntityItemUpdate(E item, EntityItem aItem);
	
	public abstract boolean isValidArmor(E item, ItemStack aStack, int ammorType, Entity aEntity);

	public abstract void onPlayerStoppedUsing(E item, World aWorld, EntityPlayer aPlayer, int aUseTick, ItemStack aStack);
}