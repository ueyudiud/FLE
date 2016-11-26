package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.CropAccessSimulated;
import farcore.lib.crop.ICrop;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.behavior.IFoodStat;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyEdible;
import farcore.lib.util.SubTag;
import farcore.lib.util.UnlocalizedList;
import farcore.util.Localization;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeed extends ItemMulti implements IFoodStat
{
	public ItemSeed()
	{
		super(FarCore.ID, MC.seed);
		enableChemicalFormula = false;
		EnumItem.seed.set(this);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Mat material = getMaterialFromItem(stack);
		if(material.contain(SubTag.CROP))
		{
			IBlockState state = worldIn.getBlockState(pos);
			if(!state.getBlock().isReplaceable(worldIn, pos))
			{
				pos = pos.offset(facing);
			}
			if(!playerIn.canPlayerEdit(pos, facing, stack))
				return EnumActionResult.FAIL;
			ICrop crop = material.getProperty(MP.property_crop);
			CropAccessSimulated access = new CropAccessSimulated(worldIn, pos, crop, getDNAFromStack(stack));
			if(!crop.canPlantAt(access)) return EnumActionResult.SUCCESS;
			BlockCrop.ITEM_THREAD.set(stack);
			worldIn.setBlockState(pos, EnumBlock.crop.block.getDefaultState(), 3);
			BlockCrop.ITEM_THREAD.set(null);
			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.filt(condition))
		{
			ItemStack stack = applySeed(1, material, 0, material.getProperty(MP.property_crop).makeNativeDNA());
			subItems.add(stack);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		unlocalizedList.add("info.crop.type", getMaterialFromItem(stack).getProperty(MP.property_crop).getLocalName(getDNAFromStack(stack)));
		unlocalizedList.add("info.crop.generation", getGenerationFromStack(stack) + 1);
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if(playerIn.capabilities.isCreativeMode)
		{
			Localization.addFoodStatInformation(getMaterialFromItem(stack).getProperty(MP.property_edible), stack, unlocalizedList);
		}
	}

	public static ItemStack applySeed(int size, Mat material, int generation, String dna)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("generation", (short) generation);
		nbt.setString("dna", dna);
		ItemStack stack = new ItemStack(EnumItem.seed.item, size, material.id);
		stack.setTagCompound(nbt);
		return stack;
	}

	public static String getDNAFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? "" : stack.getTagCompound().getString("dna");
	}

	public static int getGenerationFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getShort("generation");
	}
	
	@Override
	public float[] getNutritionAmount(ItemStack stack)
	{
		PropertyEdible property = getMaterialFromItem(stack).getProperty(MP.property_edible);
		return property == null ? null : property.nutrAmount;
	}
	
	@Override
	public float getFoodAmount(ItemStack stack)
	{
		PropertyEdible property = getMaterialFromItem(stack).getProperty(MP.property_edible);
		return property == null ? null : property.foodLevel;
	}
	
	@Override
	public float getSaturation(ItemStack stack)
	{
		PropertyEdible property = getMaterialFromItem(stack).getProperty(MP.property_edible);
		return property == null ? null : property.saturationLevel;
	}
	
	@Override
	public float getDrinkAmount(ItemStack stack)
	{
		PropertyEdible property = getMaterialFromItem(stack).getProperty(MP.property_edible);
		return property == null ? null : property.waterLevel;
	}
	
	@Override
	public boolean isEdible(ItemStack stack, EntityPlayer player)
	{
		return player.canEat(false);
	}
	
	@Override
	public boolean isWolfEdible(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isDrink(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public int getEatDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if(isEdible(itemStackIn, playerIn))
		{
			playerIn.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult(EnumActionResult.FAIL, itemStackIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		try
		{
			if(entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entityLiving;
				player.getFoodStats().addStats(null, stack);
				worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
				stack = onEat(stack, player);
				return stack;
			}
			return super.onItemUseFinish(stack, worldIn, entityLiving);
		}
		catch (Exception exception)
		{
			return stack;
		}
	}
	
	@Override
	public ItemStack onEat(ItemStack stack, EntityPlayer player)
	{
		PropertyEdible property = getMaterialFromItem(stack).getProperty(MP.property_edible);
		if(property != null)
		{
			stack = property.onEat(stack, player);
		}
		return stack;
	}
}