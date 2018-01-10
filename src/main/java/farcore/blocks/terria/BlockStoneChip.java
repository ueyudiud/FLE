/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks.terria;

import java.util.List;
import java.util.Random;

import farcore.data.MC;
import farcore.data.Materials;
import farcore.lib.block.BlockMaterial;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.base.ObjArrayParseHelper;
import nebula.client.model.StateMapperExt;
import nebula.common.util.Maths;
import nebula.common.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockStoneChip extends BlockMaterial<PropertyBlockable>
{
	private static final AxisAlignedBB[] AABBS;
	
	public static final IProperty<Integer> SHAPE = Properties.create("shape", 0, 15);
	
	public BlockStoneChip(PropertyBlockable property)
	{
		super(property.material.modid, "chip." + property.material.name, Materials.ROCK, property.material, property);
		setToolNotRequired();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "stone_chip", null);
		mapper.setVariants("material", this.material.name);
		ModelLoader.setCustomStateMapper(this, mapper);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, SHAPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return getDefaultState().withProperty(SHAPE, (int) Maths.mod(Maths.getCoordinateRandom(pos), 16));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABBS[getActualState(state, source, pos).getValue(SHAPE)];
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn)
	{
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return 0;
	}
	
	@Override
	public boolean canBreakEffective(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return 0.0F;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		super.neighborChanged(state, worldIn, pos, blockIn);
		if (!canPlaceBlockAt(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos.down(), EnumFacing.UP);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return ObjArrayParseHelper.newArrayList(ItemMulti.createStack(this.material, MC.chip_rock));
	}
	
	static
	{
		AABBS = new AxisAlignedBB[16];
		Random random = new Random(4214141650735463852L);
		for (int i = 0; i < 16; ++i)
		{
			int x = random.nextInt(4) + 1, y = random.nextInt(5) + 1, z = random.nextInt(4) + 1;
			AABBS[i] = new AxisAlignedBB((8 - x) * 0.0625F, 0, (8 - z) * 0.0625F, (8 + x) * 0.0625F, y * 0.0625, (8 + z) * 0.0625F);
		}
	}
}
