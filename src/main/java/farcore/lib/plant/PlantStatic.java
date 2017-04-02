/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.plant;

import java.util.Random;

import com.google.common.collect.ImmutableMap;

import farcore.lib.material.Mat;
import nebula.client.util.Client;
import nebula.client.util.IRenderRegister;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class PlantStatic extends PlantNormal implements IRenderRegister
{
	public PlantStatic(Mat material)
	{
		super(material);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		ModelLoader.setCustomStateMapper(this.block, block-> ImmutableMap.of(block.getDefaultState(), new ModelResourceLocation(this.material.modid + ":plant/" + this.material.name, "normal")));
		Client.registerModel(this.block, 0, this.material.modid + ":plant/" + this.material.name, "inventory");
	}
	
	@Override
	public void registerStateToRegister(Block block, ExtendedBlockStateRegister register)
	{
		register.registerState(block.getDefaultState());
	}
	
	@Override
	public BlockStateContainer createBlockState(Block block)
	{
		return new BlockStateContainer(block);
	}
	
	@Override
	public int getMeta(Block block, IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getState(Block block, int meta)
	{
		return block.getDefaultState();
	}
	
	@Override
	public IBlockState initDefaultState(IBlockState state)
	{
		return state;
	}
	
	@Override
	protected IBlockState randomGrowState(IBlockState parent, Random random)
	{
		return parent;
	}
}