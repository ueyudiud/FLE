package farcore.lib.block.instance;

import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.lib.block.BlockTileUpdatable;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.block.material.MaterialOre;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.util.BlockStateWrapper;
import farcore.util.U;
import farcore.util.U.Strings;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockTileUpdatable
implements ITileEntityProvider, IThermalCustomBehaviorBlock, IToolableBlock
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

	private void registerLocalized()
	{
		LanguageManager.registerLocal(getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE), "Ore");
		for(Mat ore : Mat.filt(SubTag.ORE))
		{
			for(Mat rock : Mat.filt(SubTag.ROCK))
			{
				for(EnumOreAmount amount : EnumOreAmount.values())
				{
					for(RockType type : RockType.values())
					{
						NBTTagCompound nbt = ItemOre.setRock(ItemOre.setAmount(new NBTTagCompound(), amount), rock, type);
						ItemStack stack = new ItemStack(this, 1, ore.id);
						stack.setTagCompound(nbt);
						LanguageManager.registerLocal(getTranslateNameForItemStack(stack),
								String.format("%s %s Ore", Strings.upcaseFirst(amount.name()), ore.localName));
					}
				}
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
			return String.format("%s@%s.%s.%s.%s",
					getUnlocalizedName(), Mat.register.get(stack.getItemDamage(), M.VOID), ItemOre.getAmount(nbt).name(), ItemOre.getRockMaterial(nbt).name, ItemOre.getRockType(nbt).name());
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
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 1.0F;
		return ((TEOre) tile).getHardness();
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 1.0F;
		return ((TEOre) tile).getExplosionResistance();
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return;
		((TEOre) tile).getOre().oreProperty.onEntityWalk((TEOre) tile, entityIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(EnumOreAmount amount : EnumOreAmount.values())
		{
			for(Mat ore : Mat.filt(SubTag.ORE))
			{
				for(Mat rock : Mat.filt(SubTag.ROCK))
				{
					list.add(((ItemOre) itemIn).createItemStack(1, ore, amount, rock, RockType.resource));
				}
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		TileEntity tile;
		if((tile = worldIn.getTileEntity(pos)) instanceof TEOre)
		{
			if(stack.hasTagCompound())
			{
				NBTTagCompound nbt = stack.getTagCompound();
				((TEOre) tile).amount = ItemOre.getAmount(nbt);
				((TEOre) tile).rock = ItemOre.getRockMaterial(nbt);
				((TEOre) tile).rockType = ItemOre.getRockType(nbt);
			}
			((TEOre) tile).setOre(Mat.register.get(stack.getItemDamage()));
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return false;
		ItemStack stack = player.inventory.getCurrentItem();
		String tool = EnumToolType.pickaxe.name();
		if (stack == null)
			return player.canHarvestBlock(getDefaultState());
		int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
		if (toolLevel < 0)
			return player.canHarvestBlock(getDefaultState());
		return toolLevel >= ((TEOre) tile).getHarvestLevel();
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.pickaxe.name();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEOre();
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			((TEOre) tile).getOre().oreProperty.updateTick((TEOre) tile, rand);
		}
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return ((TEOre) tile).rockType.burnable ? 80 : 0;
		return 0;
	}

	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			if(((TEOre) tile).getOre().oreProperty.onBurn((TEOre) tile, burnHardness, direction)) return true;
			if(((TEOre) tile).rockType.burnable)
			{
				((TEOre) tile).rockType = ((TEOre) tile).rockType.burned();
				tile.markDirty();
			}
		}
		return false;
	}

	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			if(((TEOre) tile).getOre().oreProperty.onBurningTick((TEOre) tile, rand, fireSourceDir, fireState)) return true;
		}
		return false;
	}

	@Override
	public float getThermalConduct(World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return ((TEOre) tile).getThermalConduct();
		return -1F;
	}

	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 0F;
		return ((TEOre) tile).getOre().oreProperty.onToolClick(player, tool, stack, (TEOre) tile, side, hitX, hitY, hitZ);
	}
	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 0F;
		return ((TEOre) tile).getOre().oreProperty.onToolUse(player, tool, stack, (TEOre) tile, side, hitX, hitY, hitZ, useTick);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(target.getBlockPos());
		if(tile instanceof TEOre)
		{
			IBlockState state2 = ((TEOre) tile).rock.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, ((TEOre) tile).rockType);
			U.Client.addBlockHitEffect(world, RANDOM, state2, target.sideHit, target.getBlockPos(), manager);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			IBlockState state2 = ((TEOre) tile).rock.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, ((TEOre) tile).rockType);
			U.Client.addBlockDestroyEffects(world, pos, state2, manager);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}