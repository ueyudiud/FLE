package farcore.lib.item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.IItemBehaviorsAndProperties.IIB_BlockHarvested;
import farcore.lib.item.IItemBehaviorsAndProperties.IIP_DigSpeed;
import farcore.lib.item.behavior.IBehavior;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.skill.ISkill;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.lib.world.IEnvironment;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTool extends ItemSubBehavior
implements ITool, IUpdatableItem, IIB_BlockHarvested, IIP_DigSpeed
{
	public static Mat getMaterial(ItemStack stack, String part)
	{
		if(stack != null && stack.getItem() instanceof ItemTool)
			return ((ItemTool) stack.getItem()).getMaterialFromItem(stack, part);
		else
			return M.VOID;
	}
	
	@SideOnly(Side.CLIENT)
	public static int getColor(ItemStack stack, int pass)
	{
		if(stack != null && stack.getItem() instanceof ItemTool)
			return ((ItemTool) stack.getItem()).getToolProp(stack).stat.getColor(stack, pass);
		else
			return -1;
	}
	
	private static final DecimalFormat HARDNESS_FORMAT = new DecimalFormat("##.0");
	
	public static final ToolProp EMPTY_PROP;
	
	static
	{
		EMPTY_PROP = new ToolProp();
		EMPTY_PROP.id = -1;
		EMPTY_PROP.hasHandle = false;
		EMPTY_PROP.hasTie = false;
		EMPTY_PROP.filterHead = IDataChecker.FALSE;
		EMPTY_PROP.filterHandle = IDataChecker.FALSE;
		EMPTY_PROP.filterTie = IDataChecker.FALSE;
		EMPTY_PROP.customToolInformation = null;
		EMPTY_PROP.condition = MC.LATTICE;
		EMPTY_PROP.toolTypes = ImmutableList.of();
	}
	
	public static class ToolProp
	{
		int id;
		boolean hasTie;
		boolean hasHandle;
		MatCondition condition;
		IToolStat stat;
		IDataChecker<? extends ISubTagContainer> filterHead;
		IDataChecker<? extends ISubTagContainer> filterTie;
		IDataChecker<? extends ISubTagContainer> filterHandle;
		List<EnumToolType> toolTypes;
		String customToolInformation;
		public ISkill skillEfficiency;
	}

	private Map<Integer, ToolProp> toolPropMap = new HashMap();
	protected String textureFileName = "tool/";
	
	protected boolean modelFlag = true;

	protected ItemTool(String name)
	{
		super(name);
	}
	protected ItemTool(String modid, String name)
	{
		super(modid, name);
	}
	
	public ToolProp addSubItem(int id, String name, String localName, String customToolInformation,
			MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle,
			IDataChecker<? extends ISubTagContainer> filterHead,
			IDataChecker<? extends ISubTagContainer> filterTie, IDataChecker<? extends ISubTagContainer> filterHandle,
			List<EnumToolType> toolTypes,
			IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, stat, behaviors);
		if(modelFlag)
		{
			U.Mod.registerItemModel(this, id, modid, textureFileName + name);
		}
		ToolProp prop = new ToolProp();
		prop.id = id;
		prop.condition = condition;
		prop.hasTie = hasTie;
		prop.hasHandle = hasHandle;
		prop.stat = stat;
		prop.filterHead = filterHead;
		prop.filterHandle = filterHandle;
		prop.filterTie = filterTie;
		prop.customToolInformation = customToolInformation;
		prop.toolTypes = toolTypes;
		toolPropMap.put(id, prop);
		if(customToolInformation != null)
		{
			LanguageManager.registerLocal("info.tool." + getUnlocalizedName() + "@" + id, customToolInformation);
		}
		return prop;
	}

	protected Mat getMaterialFromItem(ItemStack stack, String part)
	{
		NBTTagCompound nbt = U.ItemStacks.setupNBT(stack, false).getCompoundTag("tool");
		return Mat.material(nbt.getString(part));
	}

	public ItemStack setMaterialToItem(ItemStack stack, String part, Mat material)
	{
		stack.getSubCompound("tool", true).setString(part, material.name);
		return stack;
	}

	protected ToolProp getToolProp(ItemStack stack)
	{
		return toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
	}
	
	@Override
	public List<EnumToolType> getToolTypes(ItemStack stack)
	{
		return toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).toolTypes;
	}
	
	@Override
	public void onToolUse(EntityLivingBase user, ItemStack stack, EnumToolType toolType, float amount)
	{
		if(!isItemUsable(stack)) return;
		if(user instanceof EntityPlayer && ((EntityPlayer) user).capabilities.isCreativeMode)
			return;
		int max = getMaxDurability(stack);
		float now = getToolDamage(stack);
		if(now + amount >= max)
		{
			stack.stackSize --;
			if(user != null)
			{
				user.renderBrokenItemStack(stack);
			}
			if(stack.stackSize != 0)
			{
				setToolDamage(stack, 0F);
			}
		}
		else
		{
			setToolDamage(stack, now + amount);
		}
	}
	
	@Override
	public float replaceDigSpeed(ItemStack stack, BreakSpeed event)
	{
		return !isItemUsable(stack) ? 0.0F : toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getMiningSpeed(stack, event.getEntityPlayer(), event.getEntityPlayer().worldObj, event.getPos(), event.getState(), event.getOriginalSpeed());
	}

	@Override
	public void onBlockHarvested(ItemStack stack, HarvestDropsEvent event)
	{
		if(!isItemUsable(stack)) return;
		IToolStat stat = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat;
		if(stat.canHarvestDrop(stack, event.getState()))
		{
			onToolUse(event.getHarvester(), stack, stat.getToolType(), stat.getToolDamagePerBreak(stack, event.getHarvester(), event.getWorld(), event.getPos(), event.getState()));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		EnumActionResult result = super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.PASS)
			return U.ItemStacks.onUseOnBlock(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ);
		return result;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(super.onLeftClickEntity(stack, player, entity))
			return true;
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		Mat material = getMaterial(stack, "head");
		if(entity.canBeAttackedWithItem() && !entity.isInvisibleToPlayer(player))
		{
			float damage = prop.stat.getDamageVsEntity(stack, material);
			boolean flag = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && player.getRidingEntity() == null && (entity instanceof EntityLivingBase);
			if(flag)
			{
				damage *= 1.5F;
			}
			if(damage > 0 && entity.attackEntityFrom(prop.stat.getDamageSource(player, entity), damage))
			{
				int knockback = (player.isSprinting() ? 1 : 0) + EnchantmentHelper.getKnockbackModifier(player);
				entity.addVelocity(-Math.sin(player.rotationYaw * Math.PI / 180D) * knockback * .5, 0.1F, Math.cos(player.rotationYaw * Math.PI / 180D) * knockback * .5);
				player.motionX *= 0.6F;
				player.motionZ *= 0.6F;
				player.setSprinting(false);
				if(flag)
				{
					player.onCriticalHit(entity);
				}
				if(damage >= 18.0F)
				{
					player.addStat(AchievementList.OVERKILL);
				}
				entity.hurtResistantTime = Math.max(1, entity.hurtResistantTime + 1);
				player.setLastAttacker(entity);
				if (entity instanceof EntityLivingBase)
				{
					EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, player);
				}
				EnchantmentHelper.applyArthropodEnchantments(player, entity);
				onToolUse(player, stack, prop.stat.getToolType(), prop.stat.getToolDamagePerAttack(stack, player, entity));
			}
		}
		U.Players.destoryPlayerCurrentItem(player);
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		ActionResult<ItemStack> result;
		if((result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)).getType() != EnumActionResult.PASS)
			return result;
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(itemStackIn), EMPTY_PROP);
		if(prop.stat.canBlock())
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return super.canHarvestBlock(state, stack);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getToolType().getToolClasses();
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getToolHarvestLevel(stack, toolClass, getMaterial(stack, "head"));
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP).stat.getSpeedMultiplier(stack);
	}
	
	@Override
	public boolean isItemTool(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		if(slot == EntityEquipmentSlot.MAINHAND)
		{
			Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
			Mat material = getMaterial(stack, "head");
			ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
			float speed = prop.stat.getAttackSpeed(stack, material);
			if(speed != 0)
			{
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", speed, 0));
			}
			return multimap;
		}
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		return prop.stat.canBlock() ? EnumAction.BLOCK :
			prop.stat.isShootable() ? EnumAction.BOW : EnumAction.NONE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		if(displayList == null)
		{
			List<Integer> list = new ArrayList(32768);
			for(int i = 0; i < 32768; ++i)
			{
				if(idMap.containsValue(i))
				{
					list.add(i);
				}
			}
			displayList = U.L.cast(list.toArray(new Integer[list.size()]));
		}
		for(int id : displayList)
		{
			subItems.add(setMaterialToItem(new ItemStack(this, 1, id), "head", M.VOID));
		}
	}
	
	public static float getToolDamage(ItemStack stack)
	{
		return U.ItemStacks.setupNBT(stack, false).getCompoundTag("durability").getFloat("damage");
	}

	public static int getDefaultMaxDurability(ToolProp prop, ItemStack stack)
	{
		if(prop.hasHandle)
		{
			Mat head = getMaterial(stack, "head");
			Mat handle = getMaterial(stack, "handle");
			return (int) (head.toolMaxUse * handle.handleToughness);
		}
		else
			return getMaterial(stack, "head").toolMaxUse;
	}
	
	public static int getMaxDurability(ItemStack stack)
	{
		ToolProp prop = ((ItemTool) stack.getItem()).toolPropMap.get(((ItemTool) stack.getItem()).getBaseDamage(stack));
		NBTTagCompound tag = stack.getSubCompound("durability", true);
		if(!tag.hasKey("maxDurability"))
		{
			if(U.Sides.isServer() || !U.Sides.isSimulating())
			{
				tag.setInteger("maxDurability", (int) (prop.stat.getMaxDurabilityMultiplier() * getDefaultMaxDurability(prop, stack)));
			}
			else
				return getDefaultMaxDurability(prop, stack);
		}
		return tag.getInteger("maxDurability");
	}
	
	public static float getDurability(ItemStack stack)
	{
		return getMaxDurability(stack) - getToolDamage(stack);
	}

	public static void setToolDamage(ItemStack stack, float damage)
	{
		stack.getSubCompound("durability", true).setFloat("damage", damage);
	}
	
	@Override
	public int getMaxDamage()
	{
		return 0;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) getToolDamage(stack) / (double) getMaxDurability(stack);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getToolDamage(stack) != 0;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		ItemStack stack2 = stack;
		if(!entityIn.worldObj.isRemote)
		{
			stack = ((IUpdatableItem) this).updateItem(null, stack);
			if(entityIn instanceof EntityPlayer)
			{
				if(stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
			}
			if(stack.getItem() != this)
				return;
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		boolean flag = super.onEntityItemUpdate(entityItem);
		if(!entityItem.worldObj.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(null, entityItem.getEntityItem());
			if(stack == null)
			{
				entityItem.setDead();
				return false;
			}
			else if(stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if(stack.getItem() != this)
				return true;
		}
		return flag;
	}
	
	@Override
	public ItemStack updateItem(IEnvironment environment, ItemStack stack)
	{
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		Mat material = getMaterialFromItem(stack, "head");
		if(material.itemProp != null)
		{
			stack = material.itemProp.updateItem(stack, material, prop.condition, environment, "");
		}
		if(stack != null && prop.hasTie)
		{
			material = getMaterialFromItem(stack, "tie");
			if(material.itemProp != null)
			{
				stack = material.itemProp.updateItem(stack, material, MC.tie, environment);
			}
		}
		if(stack != null && prop.hasHandle)
		{
			material = getMaterialFromItem(stack, "handle");
			if(material.itemProp != null)
			{
				stack = material.itemProp.updateItem(stack, material, MC.handle, environment);
			}
		}
		return stack;
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public int getStackMetaOffset(ItemStack stack)
	{
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		Mat material = getMaterialFromItem(stack, "head");
		if(material != null && material.itemProp != null)
			return material.itemProp.getMetaOffset(stack, material, prop.condition, "head");
		return 0;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		int base = damage & 0x7FFF;
		super.setDamage(stack, base);
		Mat material = getMaterial(stack, "head");
		if(material.itemProp != null)
		{
			material.itemProp.setInstanceFromMeta(stack, damage >> 15, material, toolPropMap.getOrDefault(base, EMPTY_PROP).condition);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		int max = getMaxDurability(stack);
		float now = getDurability(stack);
		ToolProp prop = toolPropMap.getOrDefault(getBaseDamage(stack), EMPTY_PROP);
		unlocalizedList.addNotNull("info.tool." + getUnlocalizedName() + "@" + getBaseDamage(stack));
		unlocalizedList.add("info.tool.damage", (int) (now * 100), max * 100);
		Mat material = getMaterialFromItem(stack, "head");
		unlocalizedList.add("info.tool.harvest.level", material.toolHarvestLevel);
		unlocalizedList.add("info.tool.hardness", HARDNESS_FORMAT.format(material.toolHardness));
		unlocalizedList.add("info.tool.head.name", material.name);
		unlocalizedList.addNotNull("info.material.custom." + material.name);
		if(material.itemProp != null)
		{
			material.itemProp.addInformation(stack, material, prop.condition, unlocalizedList, "head");
		}
		if(prop.hasHandle)
		{
			material = getMaterialFromItem(stack, "handle");
			unlocalizedList.add("info.tool.handle.name", material.name);
			unlocalizedList.addNotNull("info.material.custom." + material.name);
			if(material.itemProp != null)
			{
				material.itemProp.addInformation(stack, material, MC.handle, unlocalizedList, "handle");
			}
		}
		if(prop.hasTie)
		{
			material = getMaterialFromItem(stack, "tie");
			unlocalizedList.add("info.tool.tie.name", material.name);
			unlocalizedList.addNotNull("info.material.custom." + material.name);
			if(material.itemProp != null)
			{
				material.itemProp.addInformation(stack, material, MC.tie, unlocalizedList, "tie");
			}
		}
	}
}