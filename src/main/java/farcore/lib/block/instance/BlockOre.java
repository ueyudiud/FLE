package farcore.lib.block.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.lib.block.BlockSingleTE;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.block.material.MaterialOre;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.BlockStateWrapper;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U.Strings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockSingleTE
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

	public static final MaterialOre ORE = new MaterialOre();

	public BlockOre()
	{
		super(FarCore.ID, "ore", ORE);
		setTickRandomly(true);
		registerLocalized();
		EnumBlock.ore.set(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEOre();
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
		//		for(EnumOreAmount amount : EnumOreAmount.values())
		{
			EnumOreAmount amount = EnumOreAmount.normal;//Only provide normal amount ore in creative tab or it is too many ore to display.
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

	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip,
			boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		tooltip.addNotNull("info.material.chemical.formula." + Mat.material(stack.getItemDamage()).name);
	}
}