/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import farcore.data.Capabilities;
import farcore.util.Localization;
import fle.core.FLE;
import fle.core.items.behavior.BehaviorBlockableTool;
import fle.loader.IBFS;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.render.IProgressBarStyle;
import nebula.client.util.Client;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.capability.CapabilityProviderItem;
import nebula.common.fluid.container.IItemFluidContainerV1;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import nebula.common.item.ItemSubBehavior;
import nebula.common.util.EnumChatFormatting;
import nebula.common.util.FluidStacks;
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
		int		capacity;
		int		durbility;
		boolean	enableToFill;
		boolean	enableToDrain;
		
		public FluidContainerProperty(int capacity, int durbility)
		{
			this(capacity, durbility, false, false);
		}
		
		public FluidContainerProperty(int capacity, int durbility, boolean enableToFill, boolean enableToDrain)
		{
			this.capacity = capacity;
			this.durbility = durbility * capacity;
			this.enableToDrain = enableToDrain;
			this.enableToFill = enableToFill;
		}
	}
	
	public static ItemStack createItemStack(String name, @Nullable FluidStack stack)
	{
		ItemStack stack2 = IBFS.iFluidContainer.getSubItem(name);
		setFluid(stack2, stack);
		return stack2;
	}
	
	@SideOnly(Side.CLIENT)
	private IProgressBarStyle						style;
	private Map<Integer, FluidContainerProperty>	propertyMap	= new HashMap<>();
	
	public ItemSimpleFluidContainer()
	{
		super(FLE.MODID, "simple.fluid.container");
		setMaxStackSize(1);
		init();
	}
	
	protected void init()
	{
		addSubItem(1, "barrel", "Barrel", new FluidContainerProperty(1000, 256, true, true));
		addSubItem(2, "bowl_wooden", "Wooden Bowl", new FluidContainerProperty(250, 128, true, false), new BehaviorBlockableTool(1));
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		LanguageManager.registerLocal("info.fluidcontainer.completely.damaged", EnumChatFormatting.RED + "This fluid container has already damaged, you can only drain fluid from this container.");
	}
	
	public void addSubItem(int id, String name, String localName, FluidContainerProperty property, IBehavior...behaviors)
	{
		super.addSubItem(id, name, localName, behaviors);
		this.propertyMap.put(id, property);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "fluidcontainer"));
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "fluidcontainer/bottom"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "items/tool/tank/" + key)));
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "fluidcontainer/convert"), () -> Maps.toMap(this.nameMap.values(), key -> new ResourceLocation(FLE.MODID, "items/tool/tank/" + key + "_overlay")));
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FLE.MODID, "fluidcontainer"), stack -> this.nameMap.getOrDefault(getBaseDamage(stack), "error"));
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FLE.MODID, "fluidcontainer_getfluid"), stack -> {
			FluidStack f;
			return (f = getFluid(stack)) != null ? f.getFluid().getName() : "empty";
		});
		NebulaModelLoader.registerItemColorMultiplier(new ResourceLocation(FLE.MODID, "fluidcontainer/fluidcolor"), stack -> FluidStacks.getColor(getFluid(stack)));
		this.style = new IProgressBarStyle()
		{
			@Override
			public double getProgressScale(ItemStack stack)
			{
				return ItemSimpleFluidContainer.this.propertyMap.containsKey(getBaseDamage(stack)) ? (float) getFluidAmount(stack) / (float) ItemSimpleFluidContainer.this.propertyMap.get(getBaseDamage(stack)).capacity : 0;
			}
			
			@Override
			public int[] getProgressColor(ItemStack stack, double progress)
			{
				int color = FluidStacks.getColor(getFluid(stack));
				return new int[] { color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF };
			}
		};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, String text)
	{
		Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z, 1, this.style);
		Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(stackIn, worldIn, playerIn, hand);
		if (result.getType() != EnumActionResult.PASS) return result;
		stackIn = result.getResult();
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stackIn));
		if (property != null)
		{
			CapabilityProviderSimpleFluidContainer capability = (CapabilityProviderSimpleFluidContainer) stackIn.getCapability(Capabilities.CAPABILITY_FLUID, null);
			FluidStack fluid = capability.getFluid();
			if (property.enableToDrain && !playerIn.isSneaking() && fluid != null)
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, false);
				if (raytraceresult == null || !playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, stackIn)) return result;
				int amount = FluidStacks.drainFluidToWorld(worldIn, raytraceresult, fluid, !worldIn.isRemote);
				if (amount > 0)
				{
					capability.drain(amount, true);
					return new ActionResult<>(EnumActionResult.SUCCESS, stackIn);
				}
			}
			if (property.enableToFill && capability.canUse())
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, true);
				if (raytraceresult == null || !playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, stackIn) || !worldIn.canMineBlockBody(playerIn, raytraceresult.getBlockPos())) return result;
				FluidStack stack = FluidStacks.fillFluidFromWorld(worldIn, raytraceresult, property.capacity - FluidStacks.getAmount(fluid), FluidStacks.getFluid(fluid), !worldIn.isRemote);
				
				if (stack != null)
				{
					capability.fill(stack, true);
					return new ActionResult<>(EnumActionResult.SUCCESS, stackIn);
				}
			}
		}
		return result;
	}
	
	public static FluidStack getFluid(ItemStack stack)
	{
		return ((CapabilityProviderSimpleFluidContainer) stack.getCapability(Capabilities.CAPABILITY_FLUID, null)).getFluid();
	}
	
	public static int getCustomDamage(ItemStack stack)
	{
		return ((CapabilityProviderSimpleFluidContainer) stack.getCapability(Capabilities.CAPABILITY_FLUID, null)).getDamage();
	}
	
	public int getMaxCustomDamage(ItemStack stack)
	{
		return this.propertyMap.get(getBaseDamage(stack)).durbility;
	}
	
	public static void setCustomDamage(ItemStack stack, int damage)
	{
		((CapabilityProviderSimpleFluidContainer) stack.getCapability(Capabilities.CAPABILITY_FLUID, null)).setDamage(damage);
	}
	
	public static int getFluidAmount(ItemStack stack)
	{
		FluidStack fluid = getFluid(stack);
		return fluid == null ? 0 : fluid.amount;
	}
	
	public static void setFluid(ItemStack stack, @Nullable FluidStack contain)
	{
		((CapabilityProviderSimpleFluidContainer) stack.getCapability(Capabilities.CAPABILITY_FLUID, null)).setFluid(contain);
	}
	
	@Override
	public void setFluidInContainer(ItemStack stack, FluidStack fluid)
	{
		setFluid(stack, fluid);
	}
	
	@Override
	public boolean hasFluid(ItemStack stack)
	{
		return getFluid(stack) != null;
	}
	
	@Override
	public boolean isFull(ItemStack stack)
	{
		return getFluidAmount(stack) >= this.propertyMap.get(getBaseDamage(stack)).capacity;
	}
	
	@Override
	public boolean canDrain(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean canFill(ItemStack stack, @Nullable FluidStack resource)
	{
		if (getCustomDamage(stack) >= getMaxCustomDamage(stack)) return false;
		FluidStack stack1;
		return resource == null || ((stack1 = getFluid(stack)) == null || stack1.isFluidEqual(resource));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this, 1, meta));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		Localization.addFluidInformation(getFluid(stack), unlocalizedList);
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stack));
		if (stack != null)
		{
			int damage = getCustomDamage(stack);
			if (damage < property.durbility)
			{
				Localization.addDamageInformation((property.durbility - damage) / property.capacity, property.durbility / property.capacity, unlocalizedList);
			}
			else
			{
				unlocalizedList.add("info.fluidcontainer.completely.damaged");
			}
		}
	}
	
	@Override
	protected boolean hasCapability()
	{
		return true;
	}
	
	@Override
	protected CapabilityProviderItem createProvider(ItemStack stack)
	{
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stack));
		return property != null ? new CapabilityProviderSimpleFluidContainer(property) : null;
	}
}
