package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.CropAccessSimulated;
import farcore.lib.crop.ICrop;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyEdible;
import farcore.util.Localization;
import nebula.client.util.UnlocalizedList;
import nebula.common.item.IFoodStat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
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
	public static boolean tryPlantSeed(Mat material, ItemStack stack, World worldIn, BlockPos pos)
	{
		ICrop crop = material.getProperty(MP.property_crop);
		CropAccessSimulated access = new CropAccessSimulated(worldIn, pos, crop, getDNAFromStack(stack));
		if (!crop.canPlantAt(access)) return false;
		BlockCrop.ITEM_THREAD.set(stack);
		boolean flag = worldIn.setBlockState(pos, EnumBlock.crop.block.getDefaultState(), 3);
		BlockCrop.ITEM_THREAD.set(null);
		if (!flag) return false;
		return true;
	}
	
	public static ItemStack applySeed(int size, Mat material, GeneticMaterial dna)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		GenticMaterialFactory.INSTANCE.writeToNBT(dna, nbt, "genetic");
		ItemStack stack = new ItemStack(EnumItem.seed.item, size, material.id);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static GeneticMaterial getDNAFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? null :
			GenticMaterialFactory.INSTANCE.readFromNBT(stack.getTagCompound(), "genetic");
	}
	
	public ItemSeed()
	{
		super(FarCore.ID, MC.seed);
		this.enableChemicalFormula = false;
		EnumItem.seed.set(this);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Mat material = getMaterialFromItem(stack);
		if(material.contain(SubTags.CROP))
		{
			IBlockState state = worldIn.getBlockState(pos);
			if(!state.getBlock().isReplaceable(worldIn, pos))
			{
				pos = pos.offset(facing);
			}
			if(!playerIn.canPlayerEdit(pos, facing, stack))
				return EnumActionResult.FAIL;
			if(tryPlantSeed(material, stack, worldIn, pos))
			{
				--stack.stackSize;
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.filt(this.condition))
		{
			ICrop crop = material.getProperty(MP.property_crop);
			assert(crop != null);
			ItemStack stack = applySeed(1, material, crop.createNativeGeneticMaterial());
			subItems.add(stack);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		GeneticMaterial geneticMaterial = getDNAFromStack(stack);
		unlocalizedList.add("info.crop.type", getMaterialFromItem(stack).getProperty(MP.property_crop).getLocalName(geneticMaterial));
		unlocalizedList.add("info.crop.generation", geneticMaterial.generation + 1);
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if (playerIn.capabilities.isCreativeMode)
		{
			Localization.addFoodStatInformation(getMaterialFromItem(stack).getProperty(MP.property_edible), stack, unlocalizedList);
		}
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
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return isDrink(stack) ? EnumAction.DRINK : EnumAction.EAT;
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
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return getEatDuration(stack);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if(isEdible(itemStackIn, playerIn))
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
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