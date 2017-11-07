/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.plant;

import java.util.Random;

import com.google.common.collect.ImmutableMap;

import farcore.lib.material.Mat;
import nebula.client.util.Client;
import nebula.client.util.IRenderRegister;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.IBlockStateRegister;
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
	private boolean withColor;
	
	public PlantStatic(Mat material)
	{
		this(material, false);
	}
	
	public PlantStatic(Mat material, boolean withColor)
	{
		super(material);
		this.withColor = withColor;
		LanguageManager.registerLocal(this.block.getTranslateNameForItemStack(0), material.localName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		ModelResourceLocation location = new ModelResourceLocation(this.material.modid + ":plant/" + this.material.name, "normal");
		ModelLoader.setCustomStateMapper(this.block, block -> ImmutableMap.of(block.getDefaultState(), location));
		Client.registerModel(this.block.getItemBlock(), new ModelResourceLocation(location, "inventory"));
		if (this.withColor) Renders.registerBiomeColorMultiplier(this.block);
	}
	
	@Override
	public void registerStateToRegister(Block block, IBlockStateRegister register)
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
