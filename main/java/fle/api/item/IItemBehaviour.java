package fle.api.item;

import java.util.List;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
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
  
  public abstract List<String> getAdditionalToolTips(E item, List<String> list, ItemStack itemstack);
  
  public abstract void onUpdate(E item, ItemStack itemstack, World world, Entity entity, int tick, boolean flag);
  
  public abstract boolean canDispense(E item, IBlockSource source, ItemStack itemstack);
  
  public abstract ItemStack onDispense(E item, IBlockSource source, ItemStack itemstack);
}