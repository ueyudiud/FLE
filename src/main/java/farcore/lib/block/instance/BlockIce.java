package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IHitByFallenBehaviorBlock;
import farcore.lib.block.material.MaterialIce;
import farcore.lib.util.LanguageManager;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import farcore.util.L;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIce extends BlockBase implements IHitByFallenBehaviorBlock
{
	private static final Material ICE = new MaterialIce();
	
	public BlockIce()
	{
		super(FarCore.ID, "ice", ICE);
		this.slipperiness = 0.98F;
		this.unharvestableSpeedMultiplier = 800F;//Do you think you can easily break a 1m^3 icy cube?
		setHardness(0.7F);
		setTickRandomly(true);
		EnumBlock.ice.set(this);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Ice");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		U.Mod.registerItemBlockModel(this, 0, FarCore.ID, "ice");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName();
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return 1;
	}
	
	@Override
	protected boolean onBlockHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player,
			boolean silkHarvest)
	{
		if(!silkHarvest)
		{
			Material material = worldIn.getBlockState(pos.down()).getMaterial();
			if ((material.blocksMovement() || material.isLiquid()))
			{
				turnIntoWater(worldIn, pos, state, false);
			}
			return true;
		}
		return super.onBlockHarvest(worldIn, pos, state, player, silkHarvest);
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if(Config.enableWaterFreezeAndIceMeltTempCheck)
		{
			IWorldPropProvider properties = WorldPropHandler.getWorldProperty(worldIn);
			if(!worldIn.isRemote && ThermalNet.getTemperature(worldIn, pos, true) > V.WATER_FREEZE_POINT_F && random.nextInt(3) == 0)
			{
				turnIntoWater(worldIn, pos, state, true);
			}
		}
		else
		{
			int light = worldIn.getLightFor(EnumSkyBlock.BLOCK, pos);
			if(light > 10 && random.nextInt(8) == 0)
			{
				turnIntoWater(worldIn, pos, state, true);
			}
		}
		super.randomTick(worldIn, pos, state, random);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		return silkTouch ? ImmutableList.of(new ItemStack(this, 1)) : new ArrayList();
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.NORMAL;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	protected void turnIntoWater(World worldIn, BlockPos pos, IBlockState state, boolean setToAir)
	{
		if (worldIn.provider.doesWaterVaporize())
		{
			if(setToAir)
			{
				worldIn.setBlockToAir(pos);
			}
		}
		else
		{
			worldIn.setBlockState(pos, EnumBlock.water.block.getDefaultState(), 2);
			worldIn.notifyBlockOfStateChange(pos, EnumBlock.water.block);
		}
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean isPermeatableBy(World world, BlockPos pos, IBlockState state, IBlockState fallen)
	{
		return false;
	}
	
	@Override
	public boolean onFallingOn(World world, BlockPos pos, IBlockState state, IBlockState fallen, int height)
	{
		if(height > 10 || height > 4 && L.nextInt(11 - height) == 0)
		{
			world.setBlockToAir(pos);
			//Drop ice cube? TODO
			return true;
		}
		return false;
	}
}