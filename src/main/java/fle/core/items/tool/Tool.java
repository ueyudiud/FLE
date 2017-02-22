package fle.core.items.tool;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceEntityAttack;
import nebula.common.tool.EnumToolType;
import nebula.common.tool.ToolHooks;
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
	protected float damagePerBreak = 1.0F;
	protected float damagePerAttack = 1.0F;
	protected float damageVsEntity = 1.0F;
	protected float speedMultiplier = 1.0F;
	protected float durabilityMultiplier = 1.0F;
	protected float knockback = 0.0F;
	protected float[] attackExpandBoxing = null;
	private final EnumToolType type;
	
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
		return this.type;
	}
	
	@Override
	public void onToolCrafted(ItemStack stack, EntityPlayer player)
	{
	}
	
	@Override
	public float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos,
			IBlockState block)
	{
		return this.damagePerBreak;
	}
	
	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return this.damagePerAttack;
	}
	
	@Override
	public float getDamageVsEntity(ItemStack stack, Entity entity)
	{
		return this.damageVsEntity;
	}
	
	@Override
	public float getAttackSpeed(ItemStack stack, float mutiplier)
	{
		return mutiplier;
	}
	
	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return this.speedMultiplier;
	}
	
	@Override
	public float getMaxDurabilityMultiplier()
	{
		return this.durabilityMultiplier;
	}
	
	@Override
	public int getToolHarvestLevel(ItemStack stack, String toolClass, Mat baseMaterial)
	{
		return this.type.isToolClass(toolClass) ? baseMaterial.toolHarvestLevel : -1;
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
		return this.knockback;
	}
	
	@Override
	public float[] getAttackExpandBoxing(ItemStack stack, Mat material)
	{
		return this.attackExpandBoxing != null ? this.attackExpandBoxing.clone() : null;
	}
	
	@Override
	public DamageSource getDamageSource(EntityLivingBase user, Entity target)
	{
		String string = getDeathMessage(target, user);
		return new DamageSourceEntityAttack(this.type.name,
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
	public boolean canBreakEffective(ItemStack stack, IBlockState state)
	{
		return ToolHooks.isToolEffciency(state, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int pass)
	{
		switch (pass)
		{
		//0 for base.
		case 1 : return ItemTool.getMaterial(stack, "head").RGB;
		case 3 : return ItemTool.getMaterial(stack, "tie").RGB;
		//4 for base override.
		case 5 : return ItemTool.getMaterial(stack, "rust").RGB;
		case 6 : return ItemTool.getMaterial(stack, "inlay").RGB;
		//7 for extended override.
		case 2 : if(this.hasHandleColor) return ItemTool.getMaterial(stack, "handle").RGB;
		default: return 0xFFFFFFFF;
		}
	}
}