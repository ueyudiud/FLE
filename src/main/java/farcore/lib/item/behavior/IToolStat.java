package farcore.lib.item.behavior;

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

public interface IToolStat extends IItemCapabilityProvider
{
	EnumToolType getToolType();

	void onToolCrafted(ItemStack stack, EntityPlayer player);

	float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block);
	
	float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target);
	
	float getDamageVsEntity(ItemStack stack);
	
	float getSpeedMultiplier(ItemStack stack);

	float getMaxDurabilityMultiplier();

	int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial);
	
	boolean canHarvestDrop(ItemStack stack, IBlockState state);
	
	float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block);

	DamageSource getDamageSource(EntityLivingBase user, Entity target);

	boolean canBlock();
}
