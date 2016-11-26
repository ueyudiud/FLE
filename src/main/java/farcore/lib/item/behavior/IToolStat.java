package farcore.lib.item.behavior;

import javax.annotation.Nullable;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolType;
import farcore.lib.item.IItemCapabilityProvider;
import farcore.lib.material.Mat;
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

public interface IToolStat extends IItemCapabilityProvider
{
	EnumToolType getToolType();
	
	default EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.SMASH;
	}
	
	void onToolCrafted(ItemStack stack, EntityPlayer player);
	
	float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block);
	
	float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target);
	
	/**
	 * For some special entity attack affect.
	 * @param stack
	 * @param entity
	 * @return
	 */
	default float getDamageVsEntity(ItemStack stack, Entity entity)
	{
		return getDamageVsEntity(stack);
	}
	
	float getDamageVsEntity(ItemStack stack);
	
	float getAttackSpeed(ItemStack stack, float mutiplier);
	
	float getSpeedMultiplier(ItemStack stack);
	
	float getMaxDurabilityMultiplier();
	
	float getKnockback(ItemStack stack, Mat material, Entity entity);
	
	int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial);
	
	boolean canHarvestDrop(ItemStack stack, IBlockState state);
	
	float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block, float speedBase);
	
	DamageSource getDamageSource(EntityLivingBase user, Entity target);
	
	boolean canBlock();
	
	boolean isShootable();
	
	/**
	 * Return a float array with length of two if this weapon has
	 * AOE.
	 * @param stack
	 * @param material
	 * @return
	 */
	@Nullable
	float[] getAttackExpandBoxing(ItemStack stack, Mat material);
	
	@SideOnly(Side.CLIENT)
	int getColor(ItemStack stack, int pass);
	
	boolean isWeapon();
}