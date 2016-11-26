package farcore.lib.tile.instance;

import java.util.Arrays;
import java.util.List;

import farcore.data.Config;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.data.MP;
import farcore.data.RockType;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddLandingEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Light;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_SideSolid;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.abstracts.TEStatic;
import farcore.lib.util.Direction;
import farcore.lib.world.IBlockCoordQuarterProperties;
import farcore.util.U;
import farcore.util.U.Lights;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECustomCarvedStone extends TEStatic
implements IBlockCoordQuarterProperties, ITP_BlockHardness, ITP_ExplosionResistance, ITP_Light, ITP_SideSolid, ITP_BoundingBox,
ITB_AddHitEffects, ITB_AddLandingEffects, ITB_AddDestroyEffects, ITP_HarvestCheck, IToolableTile
{
	private static final long EMPTY = ~0L;
	private static final float BLOCK_SCALE = 0.25F;
	private static final long[] X_LAYER = {0x1111111111111111L, 0x2222222222222222L, 0x4444444444444444L, 0x8888888888888888L};
	private static final long[] Y_LAYER = {0x000F000F000F000FL, 0x00F000F000F000F0L, 0x0F000F000F000F00L, 0xF000F000F000F000L};
	private static final long[] Z_LAYER = {0x000000000000FFFFL, 0x00000000FFFF0000L, 0x00FFFF0000000000L, 0xFFFF000000000000L};
	private static final AxisAlignedBB[] AXISALIGNEDBBS = new AxisAlignedBB[64];

	static
	{
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				for(int k = 0; k < 4; ++k)
				{
					AXISALIGNEDBBS[index(i, j, k)] = new AxisAlignedBB(.25F * i, .25F * j, .25F * k, .25F * (i + 1), .25F * (j + 1), .25F * (k + 1));
				}
			}
		}
	}

	private Mat rock = M.stone;
	private PropertyRock property = null;
	public RockType type = RockType.resource;
	private boolean neighbourChanged = false;
	private boolean modified = true;
	private AxisAlignedBB box = null;
	private byte[] lightmapSky = new byte[64];
	private byte[] lightmapBlock = new byte[64];
	
	private long carvedState= 0x0;
	
	private PropertyRock property()
	{
		if(property == null)
		{
			property = rock.getProperty(MP.property_rock);
		}
		return property;
	}

	public void setRock(Mat rock, RockType type)
	{
		this.rock = rock;
		this.type = type;
		syncToNearby();
	}

	public Mat rock()
	{
		return rock;
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return property().hardness;
	}

	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return property().explosionResistance;
	}

	@Override
	public int getLightOpacity(IBlockState state)
	{
		return carvedState != 0L ? 3 : 255;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("rock", rock.name);
		compound.setString("type", type.name());
		compound.setLong("carve", carvedState);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		rock = Mat.material(compound.getString("rock"), M.stone);
		type = RockType.valueOf(compound.getString("type"));
		carvedState = compound.getLong("carve");
		property = null;
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("r", rock.name);
		nbt.setString("t", type.name());
		nbt.setLong("c", carvedState);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		rock = Mat.material(nbt.getString("r"), M.stone);
		type = RockType.valueOf(nbt.getString("t"));
		property = null;
		long state = nbt.getLong("c");
		if(state != carvedState)
		{
			carvedState = state;
			modified = true;
			markBlockRenderUpdate();
		}
	}
	
	private static int index(int x, int y, int z)
	{
		return z << 4 | y << 2 | x;
	}

	public boolean isCarved(int x, int y, int z)
	{
		return (carvedState & (1L << index(x, y, z))) != 0;
	}

	public ActionResult<Float> carveRock(EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		double vx = hitX - (player.posX - pos.getX());
		double vy = hitY - (player.posY + player.eyeHeight - pos.getY());
		double vz = hitZ - (player.posZ - pos.getZ());
		double l = Math.sqrt(vx * vx + vy * vy + vz * vz) * 64;
		vx /= l;
		vy /= l;
		vz /= l;
		double vx1 = hitX == 1.0 ? 0.9999 : hitX;
		double vy1 = hitY == 1.0 ? 0.9999 : hitY;
		double vz1 = hitZ == 1.0 ? 0.9999 : hitZ;
		while(farcore.util.L.inRange(1.0, 0.0, vx1) && farcore.util.L.inRange(1.0, 0.0, vy1) && farcore.util.L.inRange(1.0, 0.0, vz1))
		{
			int x = (int) (vx1 * 4);
			int y = (int) (vy1 * 4);
			int z = (int) (vz1 * 4);
			if(!isCarved(x, y, z))
			{
				carveRockUnmark(x, y, z);
				if(isEmpty())
				{
					removeBlock();
				}
				else
				{
					markDirty();
					syncToNearby();
				}
				return new ActionResult<Float>(EnumActionResult.SUCCESS, property().hardness / 64F);
			}
			vx1 += vx;
			vy1 += vy;
			vz1 += vz;
		}
		return new ActionResult<Float>(EnumActionResult.FAIL, null);
	}

	protected void carveRockUnmark(int x, int y, int z)
	{
		carvedState |= (1L << index(x, y, z));
		box = null;
		modified = true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideRender(int x, int y, int z, Direction facing)
	{
		if(isCarved(x, y, z))
			return false;
		if(x == 3 && facing == Direction.E)
			return !worldObj.isSideSolid(pos.east(), EnumFacing.WEST);
		if(y == 3 && facing == Direction.U)
			return !worldObj.isSideSolid(pos.up(), EnumFacing.DOWN);
		if(z == 3 && facing == Direction.S)
			return !worldObj.isSideSolid(pos.south(), EnumFacing.NORTH);
		if(x == 0 && facing == Direction.W)
			return !worldObj.isSideSolid(pos.west(), EnumFacing.EAST);
		if(y == 0 && facing == Direction.D)
			return !worldObj.isSideSolid(pos.down(), EnumFacing.UP);
		if(z == 0 && facing == Direction.N)
			return !worldObj.isSideSolid(pos.north(), EnumFacing.SOUTH);
		return isCarved(x + facing.x, y + facing.y, z + facing.z);
	}

	public boolean isFullCube()
	{
		return carvedState == 0;
	}

	public boolean isEmpty()
	{
		return carvedState == EMPTY;
	}
	
	@Override
	public boolean isSideSolid(Direction side)
	{
		int i, j, k;
		switch (side)
		{
		case U : return (~carvedState & Y_LAYER[3]) == 0;
		case D : return (~carvedState & Y_LAYER[0]) == 0;
		case S : return (~carvedState & Z_LAYER[3]) == 0;
		case N : return (~carvedState & Z_LAYER[0]) == 0;
		case E : return (~carvedState & X_LAYER[3]) == 0;
		case W : return (~carvedState & X_LAYER[0]) == 0;
		default: return true;
		}
	}

	@Override
	public boolean canPlaceTorchOnTop()
	{
		return !(isCarved(1, 3, 1) || isCarved(2, 3, 1) || isCarved(1, 3, 2) || isCarved(2, 3, 2));
	}

	private void checkModified()
	{
		if(modified)
		{
			long negS = ~carvedState;
			int minX = 0;
			int minY = 0;
			int minZ = 0;
			int maxX = 4;
			int maxY = 4;
			int maxZ = 4;
			for(int i = 0; i < 4; ++i)
			{
				if((negS & X_LAYER[i]) != 0L)
				{
					minX = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & X_LAYER[i - 1]) != 0L)
				{
					maxX = i;
					break;
				}
			}
			for(int i = 0; i < 4; ++i)
			{
				if((negS & Y_LAYER[i]) != 0L)
				{
					minY = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & Y_LAYER[i - 1]) != 0L)
				{
					maxY = i;
					break;
				}
			}
			for(int i = 0; i < 4; ++i)
			{
				if((negS & Z_LAYER[i]) != 0L)
				{
					minZ = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & Z_LAYER[i - 1]) != 0L)
				{
					maxZ = i;
					break;
				}
			}
			box = new AxisAlignedBB(minX * BLOCK_SCALE, minY * BLOCK_SCALE, minZ * BLOCK_SCALE, maxX * BLOCK_SCALE, maxY * BLOCK_SCALE, maxZ * BLOCK_SCALE);
			generateLightmap();
			modified = false;
			neighbourChanged = false;
		}
		if(neighbourChanged)
		{
			generateLightmap();
			neighbourChanged = false;
		}
	}
	
	private void generateLightmap()
	{
		if(!Config.splitBrightnessOfSmallBlock) return;
		Arrays.fill(lightmapBlock, (byte) 0);
		Arrays.fill(lightmapSky, (byte) 0);
		generateLightmap(EnumSkyBlock.SKY, lightmapSky);
		generateLightmap(EnumSkyBlock.BLOCK, lightmapBlock);
	}
	
	private void generateLightmap(EnumSkyBlock type, byte[] lightmap)
	{
		byte l1 = (byte) (getLight(0, 1, 0, type) << 4 & 0xFF);
		int idx;
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(type == EnumSkyBlock.SKY)
					{
						int k = 4;
						while(k > 0 && isCarved(i, --k, j))
						{
							lightmap[index(i, k, j)] = l1;
						}
					}
					else if(lightmap[idx = index(i, 3, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, -1, 0, type) << 4 & 0xFF);
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(lightmap[idx = index(i, 0, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(1, 0, 0, type) << 4 & 0xFF);
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(lightmap[idx = index(3, i, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(-1, 0, 0, type) << 4 & 0xFF);
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(lightmap[idx = index(0, i, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, 0, -1, type) << 4 & 0xFF);
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(lightmap[idx = index(i, j, 0)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, 0, 1, type) << 4 & 0xFF);
		if(l1 != 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				for(int j = 0; j < 4; ++j)
				{
					if(lightmap[idx = index(i, j, 3)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				for(int k = 0; k < 4; ++k)
				{
					scanLight(i, j, k, (byte) 0x2F, -1, lightmap);
				}
			}
		}
	}
	
	private void scanLight(int x, int y, int z, byte side, int light, byte[] lightmap)
	{
		if(!isCarved(x, y, z)) return;
		int idx = index(x, y, z);
		if(light < 0)
		{
			light = farcore.util.L.castPositive(lightmap[idx]);
		}
		else
		{
			if(lightmap[idx] >= light) return;
			lightmap[idx] = (byte) light;
		}
		if(light >= 4)
		{
			light -= 4;
			if((side & 0x1) != 0 && x > 0 && farcore.util.L.castPositive(lightmap[idx - 0x1]) < light)
			{
				scanLight(x - 1, y, z, (byte) (side & ~0x2), light, lightmap);
			}
			if((side & 0x2) != 0 && x < 3 && farcore.util.L.castPositive(lightmap[idx + 0x1]) < light)
			{
				scanLight(x + 1, y, z, (byte) (side & ~0x1), light, lightmap);
			}
			if((side & 0x4) != 0 && y > 0 && farcore.util.L.castPositive(lightmap[idx - 0x4]) < light)
			{
				scanLight(x, y - 1, z, (byte) (side & ~0x8), light, lightmap);
			}
			if((side & 0x8) != 0 && y < 3 && farcore.util.L.castPositive(lightmap[idx + 0x4]) < light)
			{
				scanLight(x, y + 1, z, (byte) (side & ~0x4), light, lightmap);
			}
			if((side & 0x10) != 0 && z > 0 && farcore.util.L.castPositive(lightmap[idx - 0x10]) < light)
			{
				scanLight(x, y, z - 1, (byte) (side & ~0x20), light, lightmap);
			}
			if((side & 0x20) != 0 && z < 3 && farcore.util.L.castPositive(lightmap[idx + 0x10]) < light)
			{
				scanLight(x, y, z + 1, (byte) (side & ~0x10), light, lightmap);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		checkModified();
		return box;
	}

	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		checkModified();
		return box;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return getBoundBox(state);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
			Entity entity)
	{
		for(int i = 0; i < 64; ++i)
		{
			if((carvedState & (1 << i)) == 0L)
			{
				collidingBoxes.add(AXISALIGNEDBBS[i]);
			}
		}
	}

	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX,
			float hitY, float hitZ)
	{
		if(tool == EnumToolType.chisel)
		{
			if(player.canPlayerEdit(pos, side.of(), stack))
				return carveRock(player, hitX, hitY, hitZ);
		}
		return IToolableTile.DEFAULT_RESULT;
	}

	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon()
	{
		return MaterialTextureLoader.getIcon(rock, type.condition, type.variant);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		U.Client.addBlockHitEffect(worldObj, random, getBlockState(), target.sideHit, target.getBlockPos(), manager, getIcon());
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		U.Client.addBlockDestroyEffects(worldObj, pos, getBlockState(), manager, getIcon());
		return true;
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, IBlockState iblockstate, EntityLivingBase entity,
			int numberOfParticles)
	{
		IBlockState state2 = property().block.getDefaultState().withProperty(BlockRock.ROCK_TYPE, type);
		U.Server.addBlockLandingEffects(worldObj, pos, state2, entity, numberOfParticles);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessLocal(int x, int y, int z)
	{
		int idx = index(x, y, z);
		return Config.splitBrightnessOfSmallBlock ?
				Lights.mixSkyBlockLight(lightmapSky[idx], lightmapBlock[idx]) :
					worldObj.getCombinedLight(pos, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValueLocal(int x, int y, int z)
	{
		return isCarved(x, y, z) ? 1.0F : 0.3F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getOpaquenessLocal(int x, int y, int z)
	{
		return isCarved(x, y, z) ? 0.0F : 1.0F;
	}
}