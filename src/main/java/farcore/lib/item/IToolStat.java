/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumPhysicalDamageType;
import farcore.lib.material.Mat;
import nebula.common.capability.CapabilityProviderItem;
import nebula.common.tool.EnumToolType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IToolStat
{
	EnumToolType getToolType();
	
	default EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.HIT;
	}
	
	void onToolCrafted(ItemStack stack, EntityPlayer player);
	
	/**
	 * Get causes damage per digging block.
	 * 
	 * @param stack
	 * @param user
	 * @param world
	 * @param pos
	 * @param block
	 * @return
	 */
	float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block);
	
	/**
	 * Get causes damage per attack entity.
	 * 
	 * @param stack
	 * @param user
	 * @param target
	 * @return
	 */
	float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target);
	
	/**
	 * For some special entity attack affect.
	 * 
	 * @param stack
	 * @param entity
	 * @return
	 */
	float getDamageVsEntity(ItemStack stack, Entity entity);
	
	float getAttackSpeed(ItemStack stack, float mutiplier);
	
	float getSpeedMultiplier(ItemStack stack);
	
	float getMaxDurabilityMultiplier();
	
	float getKnockback(ItemStack stack, Mat material, Entity entity);
	
	int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial);
	
	boolean canBreakEffective(ItemStack stack, IBlockState state);
	
	boolean canHarvestDrop(ItemStack stack, IBlockState state);
	
	float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block, float speedBase);
	
	/**
	 * Get damage source.
	 * 
	 * @param user
	 * @param target
	 * @return
	 */
	DamageSource getDamageSource(EntityLivingBase user, Entity target);
	
	/**
	 * Get this tool can block as sword.
	 * 
	 * @return
	 */
	boolean canBlock();
	
	/**
	 * Is tool shootable.
	 * 
	 * @return
	 */
	boolean isShootable();
	
	/**
	 * Return a float array with length of two if this weapon has AOE.
	 * 
	 * @param stack
	 * @param material
	 * @return
	 */
	@Nullable
	float[] getAttackExpandBoxing(ItemStack stack, Mat material);
	
	@SideOnly(Side.CLIENT)
	int getColor(ItemStack stack, int pass);
	
	/**
	 * Is tool is a weapon.
	 * 
	 * @return
	 */
	boolean isWeapon();
	
	default List<EnumToolType> getAllowedToolTypes()
	{
		return ImmutableList.of(getToolType());
	}
	
	List<EnumToolType> getToolTypes(ItemStack stack);
	
	CapabilityProviderItem createProvider();
}
