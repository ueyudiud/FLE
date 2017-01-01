/*
 * copyright© 2016 ueyudiud
 */

package fle.core.items;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import farcore.lib.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import farcore.lib.item.ItemSubBehavior;
import farcore.lib.item.behavior.IBehavior;
import farcore.lib.model.item.FarCoreColorMultiplier;
import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.model.item.FarCoreItemSubmetaGetterLoader;
import farcore.lib.model.item.FarCoreTextureSet;
import farcore.lib.render.IProgressBarStyle;
import farcore.lib.stack.fluid.IItemFluidContainerV1;
import farcore.lib.util.UnlocalizedList;
import farcore.util.FluidStacks;
import farcore.util.ItemStacks;
import farcore.util.Localization;
import farcore.util.NBTs;
import farcore.util.U;
import fle.core.FLE;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemSimpleFluidContainer extends ItemSubBehavior implements IIP_CustomOverlayInGui, IItemFluidContainerV1
{
	public static class FluidContainerProperty
	{
		int capacity;
		int durbility;
		
		public FluidContainerProperty(int capacity, int durbility)
		{
			this.capacity = capacity;
			this.durbility = durbility;
		}
	}
	
	@SideOnly(Side.CLIENT)
	private IProgressBarStyle style;
	private Map<Integer, FluidContainerProperty> propertyMap = new HashMap();
	
	public ItemSimpleFluidContainer()
	{
		super(FLE.MODID, "simple.fluid.container");
		init();
	}
	
	protected void init()
	{
		addSubItem(1, "barrel", "Barrel", new FluidContainerProperty(1000, 256));
		addSubItem(2, "bowl_wooden", "Wooden Bowl", new FluidContainerProperty(250, 128));
	}
	
	public void addSubItem(int id, String name, String localName, FluidContainerProperty property,
			IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, null, behaviors);
		this.propertyMap.put(id, property);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		FarCoreItemModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "fluidcontainer"));
		FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation(FLE.MODID, "fluidcontainer/bottom"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "textures/items/tool/tank/" + key)));
		FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation(FLE.MODID, "fluidcontainer/convert"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "textures/items/tool/tank/" + key + "_overlay")));
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation(FLE.MODID, "fluidcontainer"), stack -> this.nameMap.getOrDefault(getBaseDamage(stack), "error"));
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation(FLE.MODID, "fluidcontainer/fluidcolor"), stack -> FluidStacks.getColor(getFluid(stack)));
		this.style = new IProgressBarStyle()
		{
			@Override
			public double getProgressScale(ItemStack stack)
			{
				return ItemSimpleFluidContainer.this.propertyMap.containsKey(getBaseDamage(stack)) ?
						(float) getFluidAmount(stack) / (float) ItemSimpleFluidContainer.this.propertyMap.get(getBaseDamage(stack)).capacity : 0;
			}
			
			@Override
			public int[] getProgressColor(ItemStack stack, double progress)
			{
				FluidStack fluid = getFluid(stack);
				int color = (fluid == null ? 0xFFFFFFFF : fluid.getFluid().getColor(fluid));
				return new int[]{(color >> 16) & 0xFF, (color >> 8) & 0xFF, (color) & 0xFF};
			}
		};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x,
			int z, String text)
	{
		U.Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		U.Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z, 1, this.style);
		U.Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
	
	protected FluidStack getFluid(ItemStack stack)
	{
		return stack.hasTagCompound() ? FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("tank")) : null;
	}
	
	protected int getCustomDamage(ItemStack stack)
	{
		return ItemStacks.getOrSetupNBT(stack, false).getInteger("damage");
	}
	
	protected int getMaxCustomDamage(ItemStack stack)
	{
		return this.propertyMap.get(getBaseDamage(stack)).durbility;
	}
	
	protected void setCustomDamage(ItemStack stack, int damage)
	{
		NBTs.setRemovableNumber(ItemStacks.getOrSetupNBT(stack, true), "damage", damage);
	}
	
	protected int getFluidAmount(ItemStack stack)
	{
		FluidStack fluid = getFluid(stack);
		return fluid == null ? 0 : fluid.amount;
	}
	
	protected void setFluid(ItemStack stack, FluidStack contain)
	{
		if(contain == null)
		{
			ItemStacks.getOrSetupNBT(stack, false).removeTag("tank");
		}
		else
		{
			contain.writeToNBT(ItemStacks.getSubOrSetupNBT(stack, "tank", true));
		}
	}
	
	@Override
	public boolean hasFluid(ItemStack stack)
	{
		return getFluid(stack) != null;
	}
	
	@Override
	public boolean canDrain(ItemStack stack)
	{
		return hasFluid(stack);
	}
	
	@Override
	public boolean canFill(ItemStack stack, FluidStack resource)
	{
		FluidStack stack1 = getFluid(stack);
		return (stack1 == null || stack1.isFluidEqual(resource)) && getCustomDamage(stack) < getMaxCustomDamage(stack);
	}
	
	@Override
	public int fill(ItemStack stack, FluidStack resource, boolean doFill)
	{
		if(resource == null) return 0;
		FluidStack contain = getFluid(stack);
		if(contain != null && !contain.isFluidEqual(resource)) return 0;
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stack));
		if(contain.amount == property.capacity) return 0;
		int amount = resource.amount + (contain == null ? 0 : contain.amount);
		if(doFill)
		{
			resource = resource.copy();
			resource.amount = amount;
			setFluid(stack, resource);
		}
		return amount;
	}
	
	@Override
	public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain)
	{
		if(maxDrain == 0) return null;
		FluidStack contain = getFluid(stack);
		int amount = Math.min(maxDrain, contain.amount);
		if(doDrain)
		{
			contain.amount -= amount;
			setFluid(stack, contain.amount == 0 ? null : contain);
			NBTs.plusRemovableNumber(stack.getTagCompound(), "damage", 1);
		}
		contain.amount = amount;
		return contain;
	}
	
	@Override
	public FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain)
	{
		if(resource == null) return null;
		FluidStack fluid = getFluid(stack);
		if(fluid != null && fluid.isFluidEqual(resource))
			return drain(stack, resource.amount, doDrain);
		return null;
	}
	
	@Override
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		Localization.addFluidInformation(getFluid(stack), unlocalizedList);
		Localization.addDamageInformation(getCustomDamage(stack), getMaxCustomDamage(stack), unlocalizedList);;
	}
}