/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumItem;
import farcore.data.KS;
import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.skill.SkillAbstract;
import fle.api.item.behavior.IPolishableBehavior;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.api.util.ToolPropertiesModificater;
import fle.api.util.ToolPropertiesModificater.Property;
import fle.core.FLE;
import nebula.Log;
import nebula.base.Judgable;
import nebula.client.util.Client;
import nebula.client.util.UnlocalizedList;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import nebula.common.item.IProjectileItem;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.OreDict;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolFar extends ItemTool implements IIP_CustomOverlayInGui, IProjectileItem, IPolishableItem, IIP_Containerable
{
	public ItemToolFar()
	{
		super(FLE.MODID, "tool");
		EnumItem.tool.set(this);
	}
	
	@Override
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation, MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterHead, Judgable<? super Mat> filterTie, Judgable<? super Mat> filterHandle, IBehavior...behaviors)
	{
		ToolProp prop = super.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterHead, filterTie, filterHandle, behaviors);
		prop.skillEfficiency = new SkillAbstract(name + ".efficiency", localName + " Efficiency")
		{
		}.setExpInfo(30, 10F, 1.5F);
		prop.skillAttack = new SkillAbstract(name + ".attack", localName + " Attack")
		{
		}.setExpInfo(30, 12F, 1.4F);
		ItemStack stack = new ItemStack(this, 1, id);
		for (EnumToolType toolType : stat.getAllowedToolTypes())
		{
			if (toolType == stat.getToolType()) continue;// Exclude main tool
															// type.
			OreDictExt.registerOreFunction(toolType.ore(), this, s -> stat.getToolTypes(s).contains(toolType), ImmutableList.of(stack));
		}
		OreDict.registerValid(stat.getToolType().ore(), stack);
		return prop;
	}
	
	@Override
	public int getDefaultMaxDurability(ToolProp prop, ItemStack stack)
	{
		return (int) new ToolPropertiesModificater(stack).applyModification(getMaterial(stack, "head").toolMaxUse, Property.DURABILITY);
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		int lv = super.getHarvestLevel(stack, toolClass);
		return lv < 0 ? lv : (int) new ToolPropertiesModificater(stack).applyModification(lv, Property.HARVEST_LEVEL);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return new ToolPropertiesModificater(stack).applyModification(super.getStrVsBlock(stack, state), Property.MINING_SPEED);
	}
	
	@Override
	public int getToolLevel(ItemStack stack, EnumToolType type)
	{
		int lv = super.getToolLevel(stack, type);
		return lv < 0 ? lv : (int) new ToolPropertiesModificater(stack).applyModification(lv, Property.HARVEST_LEVEL);
	}
	
	@Override
	public void onBlockHarvested(ItemStack stack, HarvestDropsEvent event)
	{
		if (event.getHarvester() != null)
		{
			getToolProp(stack).skillEfficiency.using(event.getHarvester(), 1.0F);
		}
		super.onBlockHarvested(stack, event);
	}
	
	@Override
	public float replaceDigSpeed(ItemStack stack, BreakSpeed event)
	{
		float speed = super.replaceDigSpeed(stack, event);
		if (event.getEntityPlayer() != null)
		{
			int level1 = KS.DIGGING.level(event.getEntityPlayer());
			int level2 = getToolProp(stack).skillEfficiency.level(event.getEntityPlayer());
			speed *= 1 + level1 * 1E-3F + level2 * 5E-3F;
		}
		return speed;
	}
	
	@Override
	protected float getPlayerRelatedAttackDamage(ToolProp prop, ItemStack stack, EntityPlayer player, float baseAttack, float attackSpeed, int cooldown, boolean isAttackerFalling)
	{
		baseAttack = new ToolPropertiesModificater(stack).applyModification(baseAttack, Property.ATTACK_DAMAGE);
		int lv = prop.skillAttack.level(player);
		cooldown += lv * 5;
		if (lv > 1)
		{
			baseAttack += Math.sqrt(player.getRNG().nextInt(lv)) * 0.2F;
		}
		return super.getPlayerRelatedAttackDamage(prop, stack, player, baseAttack, attackSpeed, cooldown, isAttackerFalling);
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, String text)
	{
		Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z);
		Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity)
	{
		try
		{
			for (IBehavior behavior : getBehavior(entity.currentItem))
				if (behavior instanceof IProjectileItem)
				{
					((IProjectileItem) behavior).initEntity(entity);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
	}
	
	@Override
	public void onEntityTick(EntityProjectileItem entity)
	{
		try
		{
			for (IBehavior behavior : getBehavior(entity.currentItem))
				if (behavior instanceof IProjectileItem)
				{
					((IProjectileItem) behavior).onEntityTick(entity);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
	}
	
	@Override
	public boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction)
	{
		try
		{
			for (IBehavior behavior : getBehavior(entity.currentItem))
				if (behavior instanceof IProjectileItem)
				{
					if (((IProjectileItem) behavior).onHitGround(world, pos, entity, direction)) return true;
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return false;
	}
	
	@Override
	public boolean onHitEntity(World world, Entity target, EntityProjectileItem entity)
	{
		try
		{
			for (IBehavior behavior : getBehavior(entity.currentItem))
				if (behavior instanceof IProjectileItem)
				{
					if (((IProjectileItem) behavior).onHitEntity(world, target, entity)) return true;
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return false;
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		try
		{
			for (IBehavior behavior : getBehavior(stack))
				if (behavior instanceof IPolishableBehavior)
				{
					return ((IPolishableBehavior) behavior).getPolishLevel(stack);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return -1;
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		try
		{
			for (IBehavior behavior : getBehavior(stack))
				if (behavior instanceof IPolishableBehavior)
				{
					return ((IPolishableBehavior) behavior).getPolishResult(stack, base);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return base;
	}
	
	@Override
	public void onPolished(EntityPlayer player, ItemStack stack)
	{
		try
		{
			for (IBehavior behavior : getBehavior(stack))
				if (behavior instanceof IPolishableBehavior)
				{
					((IPolishableBehavior) behavior).onPolished(player, stack);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		try
		{
			for (IBehavior behavior : getBehavior(stack))
				if (behavior instanceof IIP_Containerable)
				{
					return ((IIP_Containerable) behavior).openContainer(world, pos, player, stack);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		try
		{
			for (IBehavior behavior : getBehavior(stack))
				if (behavior instanceof IIP_Containerable)
				{
					return ((IIP_Containerable) behavior).openGui(world, pos, player, stack);
				}
		}
		catch (Throwable exception)
		{
			Log.catching(exception);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		ToolPropertiesModificater modificater = new ToolPropertiesModificater(stack);
		modificater.getDisplayInformation(unlocalizedList);
	}
}
