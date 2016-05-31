package fle.api.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public interface BowlBehavior
{
	@SideOnly(Side.CLIENT)
	String getTextureName();
	
	ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player);
}