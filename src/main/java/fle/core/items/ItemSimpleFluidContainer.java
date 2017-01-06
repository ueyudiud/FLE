/*
 * copyright© 2016 ueyudiud
 */

package fle.core.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import farcore.data.EnumFluid;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
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
		boolean enableToFill;
		boolean enableToDrain;
		
		public FluidContainerProperty(int capacity, int durbility)
		{
			this(capacity, durbility, false, false);
		}
		public FluidContainerProperty(int capacity, int durbility, boolean enableToFill, boolean enableToDrain)
		{
			this.capacity = capacity;
			this.durbility = durbility;
			this.enableToDrain = enableToDrain;
			this.enableToFill = enableToFill;
		}
	}
	
	@SideOnly(Side.CLIENT)
	private IProgressBarStyle style;
	private Map<Integer, FluidContainerProperty> propertyMap = new HashMap();
	
	public ItemSimpleFluidContainer()
	{
		super(FLE.MODID, "simple.fluid.container");
		setMaxStackSize(1);
		init();
	}
	
	protected void init()
	{
		addSubItem(1, "barrel", "Barrel", new FluidContainerProperty(1000, 256, true, true));
		addSubItem(2, "bowl_wooden", "Wooden Bowl", new FluidContainerProperty(250, 128, true, false));
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
		FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation(FLE.MODID, "fluidcontainer/bottom"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "items/tool/tank/" + key)));
		FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation(FLE.MODID, "fluidcontainer/convert"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "items/tool/tank/" + key + "_overlay")));
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
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		if(result.getType() != EnumActionResult.PASS) return result;
		itemStackIn = result.getResult();
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(itemStackIn));
		if(property != null)
		{
			FluidStack fluid = getFluid(itemStackIn);
			if(property.enableToDrain && !playerIn.isSneaking() && fluid != null)
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, false);
				if(raytraceresult == null || !playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, itemStackIn)) return result;
				int amount = FluidStacks.drainFluidToWorld(worldIn, raytraceresult, fluid, !worldIn.isRemote);
				if(amount > 0)
				{
					itemStackIn = itemStackIn.copy();
					fluid.amount -= amount;
					setFluid(itemStackIn, fluid);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
			if(property.enableToFill)
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, true);
				if(raytraceresult == null ||
						!playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, itemStackIn) ||
						!worldIn.canMineBlockBody(playerIn, raytraceresult.getBlockPos())) return result;
				FluidStack stack = FluidStacks.fillFluidFromWorld(worldIn, raytraceresult, property.capacity - FluidStacks.getAmount(fluid), FluidStacks.getFluid(fluid), !worldIn.isRemote);
				
				if(stack != null)
				{
					itemStackIn = itemStackIn.copy();
					if(fluid == null)
					{
						setFluid(itemStackIn, stack);
					}
					else
					{
						fluid.amount += stack.amount;
						setFluid(itemStackIn, fluid);
					}
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
		}
		return result;
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
		if(contain == null || contain.amount == 0)
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
	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		ItemStack stack = new ItemStack(this, 1, meta);
		if(this.propertyMap.get(meta).capacity >= 1000)
		{
			setFluid(stack, new FluidStack(FluidRegistry.WATER, 1000));
			subItems.add(stack.copy());
		}
		setFluid(stack, new FluidStack(EnumFluid.water.fluid, 100));
		subItems.add(stack);
	}
	
	@Override
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		Localization.addFluidInformation(getFluid(stack), unlocalizedList);
		int max = getMaxCustomDamage(stack);
		Localization.addDamageInformation(max - getCustomDamage(stack), max, unlocalizedList);
	}
}