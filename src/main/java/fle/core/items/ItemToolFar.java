package fle.core.items;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumItem;
import farcore.data.KS;
import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.skill.SkillAbstract;
import fle.api.item.behavior.IPolishableBehavior;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.core.FLE;
import nebula.base.Judgable;
import nebula.client.util.Client;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import nebula.common.item.IProjectileItem;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.OreDict;
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
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation,
			MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle,
			Judgable<? super Mat> filterHead, Judgable<? super Mat> filterTie,
			Judgable<? super Mat> filterHandle, List<EnumToolType> toolTypes,
			IBehavior... behaviors)
	{
		ToolProp prop = super.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterHead, filterTie,
				filterHandle, toolTypes, behaviors);
		prop.skillEfficiency = new SkillAbstract(name + ".efficiency", localName + " Efficiency"){}.setExpInfo(30, 10F, 1.5F);
		prop.skillAttack = new SkillAbstract(name + ".attack", localName + " Attack"){}.setExpInfo(30, 12F, 1.4F);
		ItemStack stack = new ItemStack(this, 1, id);
		for (EnumToolType toolType : toolTypes)
		{
			OreDict.registerValid(toolType.ore(), stack);
		}
		return prop;
	}
	
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterTie,
			Judgable<? super Mat> filterHandle, List<EnumToolType> toolTypes, IBehavior... behaviors)
	{
		return addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, condition.filter, filterTie, filterHandle, toolTypes, behaviors);
	}
	
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterTie,
			Judgable<? super Mat> filterHandle, IBehavior... behaviors)
	{
		return addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, condition.filter, filterTie, filterHandle, ImmutableList.of(stat.getToolType()), behaviors);
	}
	
	@Override
	public void onBlockHarvested(ItemStack stack, HarvestDropsEvent event)
	{
		if(event.getHarvester() != null)
		{
			getToolProp(stack).skillEfficiency.using(event.getHarvester(), 1.0F);
		}
		super.onBlockHarvested(stack, event);
	}
	
	@Override
	public float replaceDigSpeed(ItemStack stack, BreakSpeed event)
	{
		float speed = super.replaceDigSpeed(stack, event);
		if(event.getEntityPlayer() != null)
		{
			int level1 = KS.DIGGING.level(event.getEntityPlayer());
			int level2 = getToolProp(stack).skillEfficiency.level(event.getEntityPlayer());
			speed *= 1 + level1 * 1E-3F + level2 * 5E-3F;
		}
		return speed;
	}
	
	@Override
	protected float getPlayerRelatedAttackDamage(ToolProp prop, ItemStack stack, EntityPlayer player, float baseAttack,
			float attackSpeed, int cooldown, boolean isAttackerFalling)
	{
		int lv = prop.skillAttack.level(player);
		cooldown += lv * 5;
		if(lv > 1)
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
	public boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x,
			int z, String text)
	{
		Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z);
		Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity)
	{
		List<IBehavior> list = getBehavior(entity.currentItem);
		for(IBehavior behavior : list)
			if(behavior instanceof IProjectileItem)
			{
				((IProjectileItem) behavior).initEntity(entity);
			}
	}
	
	@Override
	public void onEntityTick(EntityProjectileItem entity)
	{
		List<IBehavior> list = getBehavior(entity.currentItem);
		for(IBehavior behavior : list)
			if(behavior instanceof IProjectileItem)
			{
				((IProjectileItem) behavior).onEntityTick(entity);
			}
	}
	
	@Override
	public boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction)
	{
		List<IBehavior> list = getBehavior(entity.currentItem);
		for(IBehavior behavior : list)
			if(behavior instanceof IProjectileItem)
			{
				if(((IProjectileItem) behavior).onHitGround(world, pos, entity, direction))
					return true;
			}
		return false;
	}
	
	@Override
	public boolean onHitEntity(World world, Entity target, EntityProjectileItem entity)
	{
		List<IBehavior> list = getBehavior(entity.currentItem);
		for(IBehavior behavior : list)
			if(behavior instanceof IProjectileItem)
			{
				if(((IProjectileItem) behavior).onHitEntity(world, target, entity))
					return true;
			}
		return false;
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		List<IBehavior> list = getBehavior(stack);
		for(IBehavior behavior : list)
			if(behavior instanceof IPolishableBehavior)
			{
				return ((IPolishableBehavior) behavior).getPolishLevel(stack);
			}
		return -1;
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		List<IBehavior> list = getBehavior(stack);
		for(IBehavior behavior : list)
			if(behavior instanceof IPolishableBehavior)
			{
				return ((IPolishableBehavior) behavior).getPolishResult(stack, base);
			}
		return base;
	}
	
	@Override
	public void onPolished(EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> list = getBehavior(stack);
		for(IBehavior behavior : list)
			if(behavior instanceof IPolishableBehavior)
			{
				((IPolishableBehavior) behavior).onPolished(player, stack);
			}
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> list = getBehavior(stack);
		for(IBehavior behavior : list)
			if(behavior instanceof IIP_Containerable)
			{
				return ((IIP_Containerable) behavior).openContainer(world, pos, player, stack);
			}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> list = getBehavior(stack);
		for(IBehavior behavior : list)
			if(behavior instanceof IIP_Containerable)
			{
				return ((IIP_Containerable) behavior).openGui(world, pos, player, stack);
			}
		return null;
	}
}