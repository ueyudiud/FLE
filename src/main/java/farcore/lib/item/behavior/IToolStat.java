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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IToolStat extends IItemCapabilityProvider
{
	EnumToolType getToolType();

	void onToolCrafted(ItemStack stack, EntityPlayer player);

	float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block);
	
	float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target);

	default float getDamageVsEntity(ItemStack stack, Mat material)
	{
		return getDamageVsEntity(stack) * material.toolDamageToEntity;
	}

	default float getAttackSpeed(ItemStack stack, Mat material)
	{
		return (1F + getAttackSpeed(stack)) * material.toolAttackSpeed - 1F;
	}
	
	float getDamageVsEntity(ItemStack stack);

	float getAttackSpeed(ItemStack stack);
	
	float getSpeedMultiplier(ItemStack stack);

	float getMaxDurabilityMultiplier();

	int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial);
	
	boolean canHarvestDrop(ItemStack stack, IBlockState state);
	
	float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block, float speedBase);

	DamageSource getDamageSource(EntityLivingBase user, Entity target);

	boolean canBlock();
	
	boolean isShootable();
	
	@SideOnly(Side.CLIENT)
	int getColor(ItemStack stack, int pass);
}