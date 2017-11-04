/*
 * copyright© 2016-2017 ueyudiud
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
import fle.loader.IBF;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.render.IProgressBarStyle;
import nebula.client.util.Client;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.fluid.container.IItemFluidContainerV1;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import nebula.common.item.ItemSubBehavior;
import nebula.common.util.EnumChatFormatting;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
			this.durbility = durbility * capacity;
			this.enableToDrain = enableToDrain;
			this.enableToFill = enableToFill;
		}
	}
	
	public static ItemStack createItemStack(String name, FluidStack stack)
	{
		ItemStack stack2 = IBF.iFluidContainer.getSubItem(name);
		setFluid(stack2, stack);
		return stack2;
	}
	
	@SideOnly(Side.CLIENT)
	private IProgressBarStyle style;
	private Map<Integer, FluidContainerProperty> propertyMap = new HashMap<>();
	
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
		LanguageManager.registerLocal("info.fluidcontainer.completely.damaged", EnumChatFormatting.RED +
				"This fluid container has already damaged, you can only drain fluid from this container.");
	}
	
	public void addSubItem(int id, String name, String localName, FluidContainerProperty property,
			IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, (stack, nbt) -> new ICapabilityProvider()
		{
			private IFluidHandler handler = createFluidHandlerWrapper(stack);
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability == Capabilities.CAPABILITY_FLUID && facing == null;
			}
			
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return hasCapability(capability, facing) ? Capabilities.CAPABILITY_FLUID.cast(this.handler) : null;
			}
		}, behaviors);
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
			NBTTagCompound nbt = ItemStacks.getSubOrSetupNBT(stack, "tank", false);
			return nbt.hasKey("FluidName") ? nbt.getString("FluidName") : "empty";
		});
		NebulaModelLoader.registerItemColorMultiplier(new ResourceLocation(FLE.MODID, "fluidcontainer/fluidcolor"), stack -> FluidStacks.getColor(getFluid(stack)));
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
		Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z, 1, this.style);
		Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		if (result.getType() != EnumActionResult.PASS) return result;
		itemStackIn = result.getResult();
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(itemStackIn));
		if (property != null)
		{
			FluidStack fluid = getFluid(itemStackIn);
			if (property.enableToDrain && !playerIn.isSneaking() && fluid != null)
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, false);
				if (raytraceresult == null || !playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, itemStackIn)) return result;
				int amount = FluidStacks.drainFluidToWorld(worldIn, raytraceresult, fluid, !worldIn.isRemote);
				if (amount > 0)
				{
					itemStackIn = itemStackIn.copy();
					drain(itemStackIn, amount, true);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
			if (property.enableToFill && isItemUsable(itemStackIn))
			{
				RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, true);
				if (raytraceresult == null ||
						!playerIn.canPlayerEdit(raytraceresult.getBlockPos(), raytraceresult.sideHit, itemStackIn) ||
						!worldIn.canMineBlockBody(playerIn, raytraceresult.getBlockPos())) return result;
				FluidStack stack = FluidStacks.fillFluidFromWorld(worldIn, raytraceresult, property.capacity - FluidStacks.getAmount(fluid), FluidStacks.getFluid(fluid), !worldIn.isRemote);
				
				if (stack != null)
				{
					itemStackIn = itemStackIn.copy();
					if (fluid == null)
					{
						stack.amount = Math.min(property.capacity, stack.amount);
						setFluid(itemStackIn, stack);
					}
					else
					{
						stack.amount = Math.min(property.capacity - fluid.amount, stack.amount);
						fluid.amount += stack.amount;
						setFluid(itemStackIn, fluid);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
		}
		return result;
	}
	
	public static FluidStack getFluid(ItemStack stack)
	{
		return stack.hasTagCompound() ? FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("tank")) : null;
	}
	
	public static int getCustomDamage(ItemStack stack)
	{
		return ItemStacks.getOrSetupNBT(stack, false).getInteger("damage");
	}
	
	public int getMaxCustomDamage(ItemStack stack)
	{
		return this.propertyMap.get(getBaseDamage(stack)).durbility;
	}
	
	public static void setCustomDamage(ItemStack stack, int damage)
	{
		NBTs.setRemovableNumber(ItemStacks.getOrSetupNBT(stack, true), "damage", damage);
	}
	
	public static int getFluidAmount(ItemStack stack)
	{
		FluidStack fluid = getFluid(stack);
		return fluid == null ? 0 : fluid.amount;
	}
	
	public static void setFluid(ItemStack stack, @Nullable FluidStack contain)
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
	protected boolean isItemUsable(ItemStack stack)
	{
		return getCustomDamage(stack) < getMaxCustomDamage(stack);
	}
	
	@Override
	public int fill(ItemStack stack, FluidStack resource, boolean doFill)
	{
		if (resource == null || !isItemUsable(stack)) return 0;
		FluidStack contain = getFluid(stack);
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stack));
		if (contain == null)
		{
			int amount = Math.min(resource.amount, property.capacity);
			if (doFill)
			{
				setFluid(stack, FluidStacks.sizeOf(resource, amount));
			}
			return amount;
		}
		else if(!contain.isFluidEqual(resource)) return 0;
		else if(contain.amount == property.capacity) return 0;
		int result = Math.min(property.capacity - contain.amount, resource.amount);
		if(doFill)
		{
			contain.amount += result;
			setFluid(stack, contain);
		}
		return result;
	}
	
	@Override
	public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain)
	{
		if (maxDrain == 0) return null;
		FluidStack contain = getFluid(stack);
		if (contain == null) return null;
		int amount = Math.min(maxDrain, contain.amount);
		if (doDrain)
		{
			contain.amount -= amount;
			setFluid(stack, contain.amount == 0 ? null : contain);
			int max = getMaxCustomDamage(stack);
			if (NBTs.plusRemovableNumber(stack.getTagCompound(), "damage", amount, max) == max && contain.amount == 0)
			{
				stack.stackSize--;
			}
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
		subItems.add(new ItemStack(this, 1, meta));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		Localization.addFluidInformation(getFluid(stack), unlocalizedList);
		FluidContainerProperty property = this.propertyMap.get(getBaseDamage(stack));
		if (stack != null)
		{
			int damage = getCustomDamage(stack);
			if (damage < property.durbility)
				Localization.addDamageInformation((property.durbility - damage) / property.capacity, property.durbility / property.capacity, unlocalizedList);
			else
				unlocalizedList.add("info.fluidcontainer.completely.damaged");
		}
	}
}