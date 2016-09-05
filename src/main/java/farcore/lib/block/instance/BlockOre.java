package farcore.lib.block.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.lib.block.BlockTE;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.block.material.MaterialOre;
import farcore.lib.collection.IRegister;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.util.BlockStateWrapper;
import farcore.util.U.Strings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockTE
{
	public static class OreStateWrapper extends BlockStateWrapper
	{
		public final Mat ore;
		public final EnumOreAmount amount;
		public final Mat rock;
		public final RockType type;
		
		OreStateWrapper(IBlockState state, TEOre ore)
		{
			super(state);
			this.ore = ore.getOre();
			amount = ore.amount;
			rock = ore.rock;
			type = ore.rockType;
		}
		public OreStateWrapper(IBlockState state, Mat ore, EnumOreAmount amount, Mat rock, RockType rockType)
		{
			super(state);
			this.ore = ore;
			this.amount = amount;
			this.rock = rock;
			type = rockType;
		}
		
		@Override
		protected BlockStateWrapper wrapState(IBlockState state)
		{
			return new OreStateWrapper(state, ore, amount, rock, type);
		}
	}

	private static final MaterialOre ORE = new MaterialOre();

	public BlockOre()
	{
		super(FarCore.ID, "ore", ORE);
		setTickRandomly(true);
		registerLocalized();
		EnumBlock.ore.set(this);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return property_TE.withProperty(state, 1);
	}

	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(1, "ore", TEOre.class);
		return true;
	}
	
	private void registerLocalized()
	{
		LanguageManager.registerLocal(getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE), "Ore");
		for(Mat ore : Mat.filt(SubTag.ORE))
		{
			for(EnumOreAmount amount : EnumOreAmount.values())
			{
				NBTTagCompound nbt = ItemOre.setRock(ItemOre.setAmount(new NBTTagCompound(), amount), M.stone, RockType.resource);
				ItemStack stack = new ItemStack(this, 1, ore.id);
				stack.setTagCompound(nbt);
				LanguageManager.registerLocal(getTranslateNameForItemStack(stack),
						String.format("%s %s Ore", Strings.upcaseFirst(amount.name()), ore.localName));
			}
		}
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, 1, placer);
	}
	
	@Override
	protected Item createItemBlock()
	{
		return new ItemOre(this);
	}

	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			return String.format("%s@%s.%s",
					getUnlocalizedName(), Mat.material(stack.getItemDamage()), ItemOre.getAmount(nbt).name());
		}
		else
			return getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE);
	}

	@Override
	public String getLocalizedName()
	{
		return LanguageManager.translateToLocal(getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE));
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return new OreStateWrapper(state, (TEOre) tile);
		return state;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(EnumOreAmount amount : EnumOreAmount.values())
		{
			for(Mat ore : Mat.filt(SubTag.ORE))
			{
				//				for(Mat rock : Mat.filt(SubTag.ROCK))
				{
					list.add(((ItemOre) itemIn).createItemStack(1, ore, amount, M.stone, RockType.resource));
				}
			}
		}
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.pickaxe.name();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}