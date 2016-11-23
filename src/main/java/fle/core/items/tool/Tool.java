package fle.core.items.tool;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.EnumToolType;
import farcore.data.MP;
import farcore.lib.item.ItemTool;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceEntityAttack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Tool implements IToolStat
{
	protected boolean hasHandleColor = false;
	private EnumToolType type;
	
	protected Tool(EnumToolType type)
	{
		this.type = type;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return null;
	}
	
	@Override
	public EnumToolType getToolType()
	{
		return type;
	}
	
	@Override
	public void onToolCrafted(ItemStack stack, EntityPlayer player)
	{
	}
	
	@Override
	public float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos,
			IBlockState block)
	{
		return 1.0F;
	}
	
	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return 1.0F;
	}
	
	@Override
	public float getDamageVsEntity(ItemStack stack)
	{
		return 1.0F;
	}
	
	@Override
	public float getAttackSpeed(ItemStack stack)
	{
		return 0.0F;
	}
	
	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return 1.0F;
	}
	
	@Override
	public float getMaxDurabilityMultiplier()
	{
		return 1.0F;
	}
	
	@Override
	public int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial)
	{
		return type.isToolClass(toolClass) ? baseMaterial.getProperty(MP.property_tool).harvestLevel : -1;
	}
	
	@Override
	public boolean canHarvestDrop(ItemStack stack, IBlockState state)
	{
		return true;
	}
	
	@Override
	public float getMiningSpeed(ItemStack stack, EntityLivingBase user, World world, BlockPos pos, IBlockState block, float speed)
	{
		return speed;
	}

	@Override
	public float getKnockback(ItemStack stack, Mat material, Entity entity)
	{
		return 0F;
	}

	@Override
	public float[] getAttackExpandBoxing(ItemStack stack, Mat material)
	{
		return null;
	}
	
	@Override
	public DamageSource getDamageSource(EntityLivingBase user, Entity target)
	{
		String string = getDeathMessage(target, user);
		return new DamageSourceEntityAttack(type.name(),
				new TextComponentString(string.replace("(S)", "" + ChatFormatting.GREEN + user.getName() + ChatFormatting.RESET).replace("(M)", "" + ChatFormatting.RED + target.getName() + ChatFormatting.RESET)),
				user);
	}
	
	protected abstract String getDeathMessage(Entity target, EntityLivingBase user);
	
	@Override
	public boolean canBlock()
	{
		return false;
	}
	
	@Override
	public boolean isShootable()
	{
		return false;
	}
	
	@Override
	public boolean isWeapon()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int pass)
	{
		switch (pass)
		{
		//0 for base.
		case 1 : return ItemTool.getMaterial(stack, "head").RGB;
		//3 for base override.
		case 4 : return ItemTool.getMaterial(stack, "tie").RGB;
		case 5 : return ItemTool.getMaterial(stack, "rust").RGB;
		case 6 : return ItemTool.getMaterial(stack, "inlay").RGB;
		case 2 : if(hasHandleColor) return ItemTool.getMaterial(stack, "handle").RGB;
		//7 for extended override.
		default: return 0xFFFFFFFF;
		}
	}
}