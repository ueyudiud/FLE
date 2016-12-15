package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.bio.DNAHandler;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class CropTemplate extends CropBase implements IPlantable
{
	public EnumPlantType type = EnumPlantType.Crop;
	
	public CropTemplate(Mat material)
	{
		super(material);
	}
	public CropTemplate(Mat material, int maxStage, int growReq)
	{
		super(material);
		this.maxStage = maxStage;
		this.growReq = growReq;
	}
	
	public CropTemplate setDNAHelper(DNAHandler...handlers)
	{
		this.helper = handlers;
		return this;
	}
	
	public CropTemplate setMultiplicationProp(int stage, int range, int type)
	{
		this.floweringStage = stage;
		this.floweringRange = range;
		this.spreadType = (byte) type;
		return this;
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
		return this.type;
	}
	
	@Override
	public EnumPlantType getPlantType(ICropAccess access)
	{
		return this.type;
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		if(access.stage() == this.maxStage - 1)
		{
			ItemStack stack = applyChildSeed(1 + L.nextInt(5) / 3, access.info());
			if(stack != null)
			{
				list.add(stack);
			}
		}
	}
	
	public ItemStack applyChildSeed(int size, CropInfo info)
	{
		return ItemSeed.applySeed(size, this.material, info.gamete == null ? info.geneticMaterial : info.gamete);
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