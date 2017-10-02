/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.lib.block.instance.BlockPlant;
import farcore.lib.material.Mat;
import farcore.lib.world.CalendarHandler;
import farcore.lib.world.ICalendar;
import farcore.lib.world.ICalendarWithMonth;
import nebula.client.model.StateMapperExt;
import nebula.client.util.IRenderRegister;
import nebula.client.util.Renders;
import nebula.common.item.ItemSubBehavior;
import nebula.common.util.Properties;
import nebula.common.util.Worlds;
import nebula.common.world.chunk.IBlockStateRegister;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class PlantDandelion implements IPlant<BlockPlant>, IRenderRegister
{
	public static final PropertyInteger AGE = Properties.create("age", 0, 3);
	public static final PropertyInteger PROGRESS = Properties.create("progress", 0, 7);
	
	private Block block;
	
	public PlantDandelion()
	{
		this.block = BlockPlant.create(FarCore.ID, "dandelion", this);
	}
	
	@Override
	public Block block()
	{
		return this.block;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Renders.registerCompactModel(new StateMapperExt(FarCore.ID, "plant/dandelion", null, PROGRESS), this.block, AGE);
	}
	
	@Override
	public void registerStateToRegister(Block block, IBlockStateRegister register)
	{
		register.registerStates(block, AGE);
	}
	
	@Override
	public BlockStateContainer createBlockState(Block block)
	{
		return new BlockStateContainer(block, AGE, PROGRESS);
	}
	
	@Override
	public int getMeta(Block block, IBlockState state)
	{
		return state.getValue(PROGRESS) << 2 | state.getValue(AGE);
	}
	
	@Override
	public IBlockState getState(Block block, int meta)
	{
		return block.getDefaultState()
				.withProperty(AGE, meta & 0x3)
				.withProperty(PROGRESS, (meta >> 2) & 7);
	}
	
	@Override
	public IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(AGE, 3).withProperty(PROGRESS, 0);
	}
	
	@Override
	public void randomTick(BlockPlant block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if (!canBlockStay(block, state, world, pos))
		{
			world.setBlockToAir(pos);
			return;
		}
		if (world.isRemote) return;
		IBlockState newState = state;
		float temp = world.getBiome(pos).getFloatTemperature(pos);
		float humidity = world.getBiome(pos).getRainfall();
		if (temp <= 0.8F && temp >= -0.3F && humidity >= 0.2F && humidity <= 0.8F &&
				world.provider.isSurfaceWorld())
		{
			int age = state.getValue(AGE);
			int progress = state.getValue(PROGRESS);
			ICalendar calendarRaw = CalendarHandler.getCalendar(world);
			if (!(calendarRaw instanceof ICalendarWithMonth)) return;
			ICalendarWithMonth calendar = (ICalendarWithMonth) calendarRaw;
			long day = calendar.dayInYear(world);
			if (age == 3)
			{
				if (day >= 40 && day <= 88)
				{
					progress += 1 + random.nextInt(3);
					if (progress > 7)
					{
						spreadSeed(block, world, pos, random);
						world.setBlockToAir(pos);
						return;
					}
					else
					{
						newState = newState.withProperty(PROGRESS, progress);
					}
				}
			}
			else if (day >= 16 && day <= 80)
			{
				int light = world.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
				if (light < 4)
				{
					world.setBlockToAir(pos);
					return;
				}
				else if (light > 10)
				{
					progress += 1 + random.nextInt(3);
					if (progress > 7)
					{
						newState = newState.withProperty(AGE, age + 1).withProperty(PROGRESS, 0);
					}
					else
					{
						newState = newState.withProperty(PROGRESS, progress);
					}
				}
			}
		}
		if (newState != state)
		{
			world.setBlockState(pos, newState);
		}
	}
	
	private void spreadSeed(BlockPlant block, World world, BlockPos pos, Random random)
	{
		int maxCount = 3 + random.nextInt(4);
		MutableBlockPos pos2 = new MutableBlockPos();
		for (int i = 0; i < maxCount; ++i)
		{
			pos2.setPos(pos.getX() + random.nextInt(20) - 10, pos.getY() + random.nextInt(20) - 10, pos.getZ() + random.nextInt(20) - 10);
			while (world.isAirBlock(pos2.down()) && pos2.getY() > 0)
			{
				pos2.move(EnumFacing.DOWN);
			}
			if (canBlockStay(block, block.getDefaultState(), world, pos2) &&
					Worlds.isAirOrReplacable(world, pos2))
			{
				world.setBlockState(pos2, block.getDefaultState().withProperty(AGE, 0));
			}
		}
	}
	
	@Override
	public Mat material()
	{
		return M.dandelion;
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockPlant block, IBlockState state, BlockPos pos, IBlockAccess world,
			TileEntity tile, int fortune, boolean silkTouch)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		if (state.getValue(AGE) >= 2)
		{
			list.add(((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("dandelion"));
		}
		return list;
	}
}