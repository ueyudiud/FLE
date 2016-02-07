package farcore.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import farcore.util.Direction;
import flapi.FleAPI;
import net.minecraftforge.common.IPlantable;

/**
 * The generalize method to get block data.
 * 
 * @author ueyudiud
 * 		
 */
public class BlockData implements IObjectInWorld
{
	private int dimID = Integer.MIN_VALUE;
	/**
	 * The world the block in.
	 */
	private IBlockAccess world;
	/**
	 * The position the block in.
	 */
	private BlockPos pos;
	
	public BlockData(int dim, int x, int y, int z)
	{
		this.dimID = dim;
		this.pos = new BlockPos(x, y, z);
	}
	
	public BlockData(int dim, BlockPos pos)
	{
		this.dimID = dim;
		this.pos = pos;
	}
	
	public BlockData(IBlockAccess world, BlockPos pos)
	{
		this.world = world;
		if (world instanceof World)
			dimID = ((World) world).provider.getDimensionId();
		this.pos = pos;
	}
	
	public BlockData(IObjectInWorld object)
	{
		this(object.getWorld(), object.getBlockPos());
	}
	
	public World world()
	{
		if (world instanceof World)
			return (World) world;
		else if (dimID != Integer.MIN_VALUE)
			return (World) (world = FleAPI.getWorld(dimID));
		else
			return null;
	}
	
	private IBlockAccess access()
	{
		if (world != null)
			return world;
		else
			return world = FleAPI.getWorld(dimID);
	}
	
	public IBlockState state()
	{
		return access().getBlockState(pos);
	}
	
	public Block block()
	{
		return state().getBlock();
	}
	
	public Material material()
	{
		return block().getMaterial();
	}
	
	public TileEntity tile()
	{
		return access().getTileEntity(pos);
	}
	
	public BiomeGenBase biome()
	{
		return access().getBiomeGenForCoords(pos);
	}
	
	public int tempreture()
	{
		return FleAPI.getThermalNet().getEnviormentTemperature(world(), pos);
	}
	
	/**
	 * Get average rainfall in this biome.
	 * 
	 * @return Range from 0 to 65536.
	 */
	public int rainfall()
	{
		return biome().getIntRainfall();
	}
	
	public int rainStrength()
	{
		return (int) (world().getRainStrength(1.0F) * 128);
	}
	
	public Comparable property(IProperty property)
	{
		return state().getValue(property);
	}
	
	public float hardness()
	{
		return block().getBlockHardness(world(), pos);
	}
	
	/**
	 * 
	 * @param direction
	 *            Only access basic direction.
	 * @return
	 */
	public boolean isSideSolid(Direction direction)
	{
		return block().isSideSolid(world, pos, direction.toFacing());
	}
	
	public boolean isAir()
	{
		return access().isAirBlock(pos);
	}
	
	public boolean isLoaded(int range)
	{
		return world().isAreaLoaded(pos.add(-range, -range, -range),
				pos.add(range, range, range));
	}
	
	public boolean isRaining()
	{
		return world().isRaining();
	}
	
	public boolean isDaytime()
	{
		return world().isDaytime();
	}
	
	public boolean isSidePowered(Direction direction)
	{
		return world().isSidePowered(pos, direction.toFacing());
	}
	
	public int getRedstonePower(Direction direction)
	{
		return world().getRedstonePower(pos, direction.toFacing());
	}
	
	public int getSkyLight()
	{
		if (!(world instanceof World))
			return access().getCombinedLight(pos, 0) >> 20;
		else
			return ((World) world).getLightFromNeighborsFor(EnumSkyBlock.SKY,
					pos);
	}
	
	public int getBlockLight()
	{
		if (!(world instanceof World))
			return (access().getCombinedLight(pos, 0) >> 4) & 0xFFFF;
		else
			return ((World) world).getLightFromNeighborsFor(EnumSkyBlock.BLOCK,
					pos);
	}
	
	public int getLight()
	{
		return world().getLightFromNeighbors(pos);
	}
	
	public boolean canSustainPlant(Direction direction, IPlantable plantable)
	{
		return block().canSustainPlant(world, pos, direction.toFacing(),
				plantable);
	}
	
	public void tickUpdate()
	{
		tickUpdate(block().tickRate(world()));
	}
	
	public void tickUpdate(int delay)
	{
		world().scheduleUpdate(pos, block(), delay);
	}
	
	public void set(IBlockState state)
	{
		world().setBlockState(pos, state);
	}
	
	public void setToAir()
	{
		world().setBlockToAir(pos);
	}
	
	public void markForUpdate()
	{
		world().markBlockForUpdate(pos);
	}
	
	public void markRenderForUpdate(int range)
	{
		world().markBlockRangeForRenderUpdate(pos.add(-range, -range, -range),
				pos.add(range, range, range));
	}
	
	public void createExplosion(float strength, boolean breakBlock)
	{
		world().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(),
				strength, breakBlock);
	}
	
	public void createExplosion(float strength, boolean burning,
			boolean breakBlock)
	{
		world().newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength,
				burning, breakBlock);
	}
	
	// ==============Position offset=================
	public BlockData to(Direction direction)
	{
		if (dimID != Integer.MIN_VALUE)
			return new BlockData(dimID + direction.d,
					pos.offset(direction.toFacing()));
		else
			return new BlockData(world, pos);
	}
	
	public BlockData to(int x, int y, int z)
	{
		if (world != null)
			return new BlockData(world, pos.add(x, y, z));
		else
			return new BlockData(dimID, pos.add(x, y, z));
	}
	
	public BlockData to(int d, float scale)
	{
		return new BlockData(dimID + d, (int) (pos.getX() * scale),
				(int) (pos.getY() * scale), (int) (pos.getZ() * scale));
	}
	
	// ============Object in world start============
	@Override
	public World getWorld()
	{
		return world();
	}
	
	@Override
	public BlockPos getBlockPos()
	{
		return pos;
	}
	// ============Object in world end==============
}