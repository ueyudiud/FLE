package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public abstract class CropAbstract extends CropBase implements IPlantable
{
	public EnumPlantType type = EnumPlantType.Crop;

	public CropAbstract(Mat material)
	{
		super(material);
	}
	
	@Override
	public String getTranslatedName(String dna)
	{
		return material.name;
	}

	@Override
	public boolean canPlantAt(ICropAccess access)
	{
		IBlockState state;
		return (state = access.getBlockState(Direction.D)).getBlock().canSustainPlant(state, access.world(), access.pos().down(), EnumFacing.UP, this);
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return EnumBlock.crop.block.getDefaultState();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return type;
	}

	@Override
	public EnumPlantType getPlantType(ICropAccess access)
	{
		return type;
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		if(access.stage() == maxStage - 1)
		{
			list.add(applyChildSeed(1 + U.L.nextInt(20) / 17, access.info()));
		}
	}
	
	public ItemStack applyChildSeed(int size, CropInfo info)
	{
		return ItemSeed.applySeed(size, material, info.generations + 1, makeChildDNA(info.generations, info.DNA));
	}

	@Override
	public String getState(ICropAccess access)
	{
		return getRegisteredName() + "_stage_" + access.stage();
	}
	
	@Override
	public List<String> getAllowedState()
	{
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for(int i = 0; i < getMaxStage(); ++i)
		{
			builder.add(getRegisteredName() + "_stage_" + i);
		}
		return builder.build();
	}
}