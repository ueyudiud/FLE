/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.instance;

import static nebula.common.util.L.index8i;

import java.util.List;

import farcore.data.EnumRockType;
import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.data.MP;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.block.terria.BlockRock;
import farcore.lib.material.Mat;
import nebula.client.util.Client;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddLandingEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Light;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_SideSolid;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.TEStatic;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import nebula.common.util.Server;
import nebula.common.world.IBlockCoordQuarterProperties;
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
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECustomCarvedStone extends TEStatic implements IBlockCoordQuarterProperties, ITP_BlockHardness, ITP_ExplosionResistance, ITP_Light, ITP_SideSolid, ITP_BoundingBox, ITB_AddHitEffects, ITB_AddLandingEffects, ITB_AddDestroyEffects, ITP_HarvestCheck, IToolableTile
{
	private static final long				EMPTY			= ~0L;
	private static final float				BLOCK_SCALE		= 0.25F;
	private static final long[]				X_LAYER			= { 0x1111111111111111L, 0x2222222222222222L, 0x4444444444444444L, 0x8888888888888888L };
	private static final long[]				Y_LAYER			= { 0x000F000F000F000FL, 0x00F000F000F000F0L, 0x0F000F000F000F00L, 0xF000F000F000F000L };
	private static final long[]				Z_LAYER			= { 0x000000000000FFFFL, 0x00000000FFFF0000L, 0x00FFFF0000000000L, 0xFFFF000000000000L };
	private static final AxisAlignedBB[]	AXISALIGNEDBBS	= new AxisAlignedBB[64];
	
	static
	{
		for (int i = 0; i < 4; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					AXISALIGNEDBBS[index8i(i, j, k)] = new AxisAlignedBB(.25F * i, .25F * j, .25F * k, .25F * (i + 1), .25F * (j + 1), .25F * (k + 1));
				}
			}
		}
	}
	
	private Mat				rock				= M.stone;
	private RockBehavior	property			= null;
	public EnumRockType		type				= EnumRockType.resource;
	private boolean			neighbourChanged	= false;
	private boolean			modified			= true;
	private AxisAlignedBB	box					= null;
	
	long carvedState = 0x0;
	
	BrightnessProviderCarvedStone brightnessProvider;
	
	private RockBehavior property()
	{
		if (this.property == null)
		{
			this.property = this.rock.getProperty(MP.property_rock);
		}
		return this.property;
	}
	
	public void setRock(Mat rock, EnumRockType type)
	{
		this.rock = rock;
		this.type = type;
		syncToNearby();
	}
	
	public Mat rock()
	{
		return this.rock;
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
		return this.carvedState != 0L ? 3 : 255;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("rock", this.rock.name);
		NBTs.setEnum(compound, "type", this.type);
		compound.setLong("carve", this.carvedState);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.rock = Mat.getMaterialByNameOrDefault(compound, "rock", M.stone);
		this.type = NBTs.getEnumOrDefault(compound, "type", EnumRockType.resource);
		this.carvedState = compound.getLong("carve");
		this.property = null;
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("r", this.rock.name);
		NBTs.setEnum(nbt, "t", this.type);
		nbt.setLong("c", this.carvedState);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.rock = Mat.getMaterialByIDOrDefault(nbt, "r", this.rock);
		this.type = NBTs.getEnumOrDefault(nbt, "t", this.type);
		this.property = null;
		long state = NBTs.getLongOrDefault(nbt, "c", this.carvedState);
		if (state != this.carvedState)
		{
			this.carvedState = state;
			this.modified = true;
			markBlockRenderUpdate();
		}
	}
	
	public boolean isCarved(int x, int y, int z)
	{
		return (this.carvedState & (1L << index8i(x, y, z))) != 0;
	}
	
	public ActionResult<Float> carveRock(EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		double vx = hitX - (player.posX - this.pos.getX());
		double vy = hitY - (player.posY + player.eyeHeight - this.pos.getY());
		double vz = hitZ - (player.posZ - this.pos.getZ());
		double l = Math.sqrt(vx * vx + vy * vy + vz * vz) * 64;
		vx /= l;
		vy /= l;
		vz /= l;
		double vx1 = hitX == 1.0 ? 0.9999 : hitX;
		double vy1 = hitY == 1.0 ? 0.9999 : hitY;
		double vz1 = hitZ == 1.0 ? 0.9999 : hitZ;
		while (nebula.common.util.L.inRange(1.0, 0.0, vx1) && nebula.common.util.L.inRange(1.0, 0.0, vy1) && nebula.common.util.L.inRange(1.0, 0.0, vz1))
		{
			int x = (int) (vx1 * 4);
			int y = (int) (vy1 * 4);
			int z = (int) (vz1 * 4);
			if (!isCarved(x, y, z))
			{
				carveRockUnmark(x, y, z);
				if (isEmpty())
				{
					removeBlock();
				}
				else
				{
					markDirty();
					syncToNearby();
				}
				return new ActionResult<>(EnumActionResult.SUCCESS, property().hardness / 64F);
			}
			vx1 += vx;
			vy1 += vy;
			vz1 += vz;
		}
		return new ActionResult<>(EnumActionResult.FAIL, null);
	}
	
	protected void carveRockUnmark(int x, int y, int z)
	{
		this.carvedState |= (1L << index8i(x, y, z));
		this.box = null;
		this.modified = true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideRender(int x, int y, int z, Direction facing)
	{
		if (isCarved(x, y, z)) return false;
		if (x == 3 && facing == Direction.E) return !this.world.isSideSolid(this.pos.east(), EnumFacing.WEST);
		if (y == 3 && facing == Direction.U) return !this.world.isSideSolid(this.pos.up(), EnumFacing.DOWN);
		if (z == 3 && facing == Direction.S) return !this.world.isSideSolid(this.pos.south(), EnumFacing.NORTH);
		if (x == 0 && facing == Direction.W) return !this.world.isSideSolid(this.pos.west(), EnumFacing.EAST);
		if (y == 0 && facing == Direction.D) return !this.world.isSideSolid(this.pos.down(), EnumFacing.UP);
		if (z == 0 && facing == Direction.N) return !this.world.isSideSolid(this.pos.north(), EnumFacing.SOUTH);
		return isCarved(x + facing.x, y + facing.y, z + facing.z);
	}
	
	public boolean isFullCube()
	{
		return this.carvedState == 0;
	}
	
	public boolean isEmpty()
	{
		return this.carvedState == EMPTY;
	}
	
	@Override
	public boolean isSideSolid(Direction side)
	{
		switch (side)
		{
		case U:
			return (~this.carvedState & Y_LAYER[3]) == 0;
		case D:
			return (~this.carvedState & Y_LAYER[0]) == 0;
		case S:
			return (~this.carvedState & Z_LAYER[3]) == 0;
		case N:
			return (~this.carvedState & Z_LAYER[0]) == 0;
		case E:
			return (~this.carvedState & X_LAYER[3]) == 0;
		case W:
			return (~this.carvedState & X_LAYER[0]) == 0;
		default:
			return true;
		}
	}
	
	@Override
	public boolean canPlaceTorchOnTop()
	{
		return !(isCarved(1, 3, 1) || isCarved(2, 3, 1) || isCarved(1, 3, 2) || isCarved(2, 3, 2));
	}
	
	private static int indexOf(long state, long[] array, boolean invert)
	{
		int i;
		if (invert)
		{
			for (i = 0; i < 4; ++i)
			{
				if ((state & X_LAYER[i]) != 0L)
				{
					break;
				}
			}
		}
		else
		{
			for (i = 3; i >= 0; --i)
			{
				if ((state & X_LAYER[i]) != 0L)
				{
					break;
				}
			}
			i ++;
		}
		return i;
	}
	
	private void checkModified()
	{
		if (this.modified)
		{
			long negS = ~this.carvedState;
			this.box = new AxisAlignedBB(
					indexOf(negS, X_LAYER, false) * BLOCK_SCALE,
					indexOf(negS, Y_LAYER, false) * BLOCK_SCALE,
					indexOf(negS, Z_LAYER, false) * BLOCK_SCALE,
					indexOf(negS, X_LAYER, true ) * BLOCK_SCALE,
					indexOf(negS, Y_LAYER, true ) * BLOCK_SCALE,
					indexOf(negS, Z_LAYER, true ) * BLOCK_SCALE);
			generateLightmap();
			this.modified = false;
			this.neighbourChanged = false;
		}
		if (this.neighbourChanged)
		{
			generateLightmap();
			markBlockRenderUpdate();
			this.neighbourChanged = false;
		}
	}
	
	private void generateLightmap()
	{
		if (!Config.splitBrightnessOfSmallBlock) return;
		Arrays.fill(this.lightmapBlock, (byte) 0);
		Arrays.fill(this.lightmapSky, (byte) 0);
		generateLightmap(EnumSkyBlock.SKY, this.lightmapSky);
		generateLightmap(EnumSkyBlock.BLOCK, this.lightmapBlock);
	}
	
	private void generateLightmap(EnumSkyBlock type, byte[] lightmap)
	{
		byte l1 = (byte) (getLight(0, 1, 0, type) << 4 & 0xFF);
		int idx;
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (type == EnumSkyBlock.SKY)
					{
						int k = 4;
						while (k > 0 && isCarved(i, --k, j))
						{
							lightmap[index8i(i, k, j)] = l1;
						}
					}
					else if (lightmap[idx = index8i(i, 3, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, -1, 0, type) << 4 & 0xFF);
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (lightmap[idx = index8i(i, 0, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(1, 0, 0, type) << 4 & 0xFF);
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (lightmap[idx = index8i(3, i, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(-1, 0, 0, type) << 4 & 0xFF);
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (lightmap[idx = index8i(0, i, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, 0, -1, type) << 4 & 0xFF);
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (lightmap[idx = index8i(i, j, 0)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		l1 = (byte) (getLight(0, 0, 1, type) << 4 & 0xFF);
		if (l1 != 0)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (lightmap[idx = index8i(i, j, 3)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		for (int i = 0; i < 4; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					scanLight(i, j, k, (byte) 0x2F, -1, lightmap);
				}
			}
		}
	}
	
	private void scanLight(int x, int y, int z, byte side, int light, byte[] lightmap)
	{
		if (!isCarved(x, y, z)) return;
		int idx = index8i(x, y, z);
		if (light < 0)
		{
			light = L.uint(lightmap[idx]);
		}
		else
		{
			if (lightmap[idx] >= light) return;
			lightmap[idx] = (byte) light;
		}
		if (light >= 4)
		{
			light -= 4;
			if ((side & 0x1) != 0 && x > 0 && L.uint(lightmap[idx - 0x1]) < light)
			{
				scanLight(x - 1, y, z, (byte) (side & ~0x2), light, lightmap);
			}
			if ((side & 0x2) != 0 && x < 3 && L.uint(lightmap[idx + 0x1]) < light)
			{
				scanLight(x + 1, y, z, (byte) (side & ~0x1), light, lightmap);
			}
			if ((side & 0x4) != 0 && y > 0 && L.uint(lightmap[idx - 0x4]) < light)
			{
				scanLight(x, y - 1, z, (byte) (side & ~0x8), light, lightmap);
			}
			if ((side & 0x8) != 0 && y < 3 && L.uint(lightmap[idx + 0x4]) < light)
			{
				scanLight(x, y + 1, z, (byte) (side & ~0x4), light, lightmap);
			}
			if ((side & 0x10) != 0 && z > 0 && L.uint(lightmap[idx - 0x10]) < light)
			{
				scanLight(x, y, z - 1, (byte) (side & ~0x20), light, lightmap);
			}
			if ((side & 0x20) != 0 && z < 3 && L.uint(lightmap[idx + 0x10]) < light)
			{
				scanLight(x, y, z + 1, (byte) (side & ~0x10), light, lightmap);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		checkModified();
		return this.box;
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		checkModified();
		return this.box;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return getBoundBox(state);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity)
	{
		for (int i = 0; i < 64; ++i)
		{
			if ((this.carvedState & (1 << i)) == 0L)
			{
				collidingBoxes.add(AXISALIGNEDBBS[i]);
			}
		}
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.CHISEL_CARVE)
		{
			if (player.canPlayerEdit(this.pos, side.of(), stack))
			{
				return carveRock(player, hitX, hitY, hitZ);
			}
		}
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon()
	{
		return MaterialTextureLoader.getIcon(this.rock, this.type.condition, this.type.variant);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Client.addBlockHitEffect(this.world, this.random, getBlockState(), target.sideHit, target.getBlockPos(), manager, getIcon());
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, getBlockState(), manager, getIcon());
		return true;
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		IBlockState state2 = property().block.getDefaultState().withProperty(BlockRock.TYPE, this.type);
		Server.addBlockLandingEffects(this.world, this.pos, state2, entity, numberOfParticles);
		return true;
	}
	
	@Override
	public int getLightValue(IBlockState state)
	{
		return property().getLightValue(property().block, state, this.world, this.pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValueLocal(int x, int y, int z)
	{
		checkModified();
		return this.brightnessProvider.getAmbientOcclusionLightValueLocal(x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessLocal(int x, int y, int z)
	{
		checkModified();
		return this.brightnessProvider.getBrightnessLocal(x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getOpaquenessLocal(int x, int y, int z)
	{
		checkModified();
		return this.brightnessProvider.getOpaquenessLocal(x, y, z);
	}
}
