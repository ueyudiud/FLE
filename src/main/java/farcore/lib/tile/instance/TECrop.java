/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ObjectArrays;

import farcore.data.EnumToolTypes;
import farcore.data.MP;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.bio.BioData;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import nebula.V;
import nebula.client.util.Client;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_Update;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_CustomModelData;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TE05Aged;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import nebula.common.util.Players;
import nebula.common.util.W;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECrop extends TE05Aged
implements ICropAccess, IDebugableTile, IUpdatableTile, ITP_BoundingBox, ITB_Update,
ITP_HarvestCheck, ITP_Drops, ITB_AddDestroyEffects, ITB_AddHitEffects, ITP_CustomModelData
{
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0F, .0F, 0.0F, 1.0F, .125F, 1.0F);
	
	private static final CropInfo NO_DATA_INFO = new CropInfo();
	
	static
	{
		NO_DATA_INFO.data = new BioData(ICropSpecie.VOID, "wild", -1, new byte[0][], V.INTS_EMPTY);
	}
	
	private int			waterLevel	= 6400;
	private boolean		isWild		= false;
	private float		growBuffer;
	private int			stage		= 1;
	private ICropSpecie	card		= ICropSpecie.VOID;
	private CropInfo	info		= NO_DATA_INFO;
	
	public TECrop()
	{
		
	}
	
	public TECrop(BioData data, boolean unused)
	{
		this(data);
		this.isWild = true;
		this.stage = this.random.nextInt(3) == 0 ? this.card.getMaxStage() : L.nextInt(this.card.getMaxStage() - 2, this.random) + 1;
		this.growBuffer = L.nextInt(this.card.getGrowReq(this), this.random);
	}
	
	public TECrop(BioData data)
	{
		this.card = (ICropSpecie) data.specie;
		this.info = new CropInfo();
		this.info.data = data;
		this.card.expressTraits(this.info, data);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("water", this.waterLevel);
		nbt.setBoolean("isWild", this.isWild);
		nbt.setFloat("growBuf", this.growBuffer);
		NBTs.setNumber(nbt, "stage", this.stage);
		nbt.setString("crop", this.card.getRegisteredName());
		if (this.info != null)
		{
			this.info.writeToNBT(nbt);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.waterLevel = nbt.getInteger("water");
		this.isWild = nbt.getBoolean("isWild");
		this.growBuffer = nbt.getFloat("growBuf");
		this.stage = NBTs.getIntOrDefault(nbt, "stage", 1);
		this.card = Mat.material(nbt.getString("crop")).getProperty(MP.property_crop, ICropSpecie.VOID);
		this.info = new CropInfo();
		this.info.readFromNBT(nbt);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setInteger("s", this.stage);
		nbt.setString("c", this.card.material().getRegisteredName());
		NBTTagCompound nbt1;
		this.info.writeToNBT(nbt1 = new NBTTagCompound());
		nbt.setTag("i", nbt1);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.stage = NBTs.getIntOrDefault(nbt, "s", this.stage);
		Mat material = Mat.getMaterialByNameOrDefault(nbt, "c", null);
		if (material != null)
		{
			this.card = material.getProperty(MP.property_crop, ICropSpecie.VOID);
		}
		if (nbt.hasKey("i"))
		{
			this.info = new CropInfo();
			this.info.readFromNBT(nbt.getCompoundTag("i"));
		}
		markBlockRenderUpdate();
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return CROP_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		return null;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return CROP_AABB;
	}
	
	@Deprecated
	public String getStateName()
	{
		return getCustomModelData("state") + "_" + getCustomModelData("stage");
	}
	
	@Override
	protected long getNextUpdateTick(long thisTick)
	{
		return thisTick + this.card.tickUpdate(this);
	}
	
	@Override
	protected void updateServer1()
	{
		super.updateServer1();
		this.card.onUpdate(this);
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player)
	{
		return this.isWild ? true : Players.matchCurrentToolType(player, EnumToolTypes.SICKLE);
	}
	
	@Override
	public ICropSpecie getSpecie()
	{
		return this.card;
	}
	
	@Override
	public CropInfo info()
	{
		return this.info;
	}
	
	@Override
	public BioData getData()
	{
		return this.info.data;
	}
	
	@Override
	public Biome biome()
	{
		return this.world.getBiome(this.pos);
	}
	
	@Override
	public boolean isWild()
	{
		return this.isWild;
	}
	
	@Override
	public Random rng()
	{
		return this.random;
	}
	
	@Override
	public int stage()
	{
		return this.stage;
	}
	
	@Override
	public float stageBuffer()
	{
		return this.growBuffer;
	}
	
	@Override
	public void setStage(int stage)
	{
		this.stage = stage;
		markDirty();
	}
	
	@Override
	public void grow(int amt)
	{
		if (this.stage < this.card.getMaxStage())
		{
			int i = this.card.getGrowReq(this);
			this.growBuffer += amt;
			if (this.growBuffer > i)
			{
				this.growBuffer -= i;
				this.stage++;
				markBlockUpdate();// BUD?
				markBlockRenderUpdate();
				syncToNearby();
			}
			markDirty();
		}
	}
	
	@Override
	protected int getRenderUpdateRange()
	{
		return 5;
	}
	
	@Override
	public int getWaterLevel()
	{
		return this.waterLevel;
	}
	
	@Override
	public float temp()
	{
		return ThermalNet.getTemperature(this.world, this.pos, true);
	}
	
	@Override
	public int useWater(int amount)
	{
		int c = Math.min(amount, this.waterLevel);
		this.waterLevel -= c;
		return c;
	}
	
	@Override
	public void killCrop()
	{
		removeBlock();
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("Tag : " + this.card.getRegisteredName());
		
		this.card.addInformation(this, list);
	}
	
	public boolean canPlantAt()
	{
		return this.card == null ? true : this.card.canPlantAt(this);
	}
	
	@Override
	public boolean canBlockStay()
	{
		return this.world == null || this.card == ICropSpecie.VOID ? true : this.card.canPlantAt(this);
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if (!canBlockStay())
		{
			W.spawnDropsInWorld(this, getDrops(state, 0, false));
			killCrop();
		}
	}
	
	public EnumPlantType getPlantType()
	{
		return this.card == null ? EnumPlantType.Crop : this.card.getPlantType(this);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if (this.card != null)
		{
			this.card.getDrops(this, list);
		}
		return list;
	}
	
	@Override
	public void onUpdateTick(IBlockState state, Random random, boolean isTickRandomly)
	{
		if (!canPlantAt())
		{
			removeBlock();
		}
	}
	
	@Override
	public void pollinate(boolean self, BioData gm)
	{
		if (gm == null)
			return;
		if (this.info.seed == null)
		{
			BioData gm2 = this.card.getGamete(gm, this.random);
			byte[][] gene = ObjectArrays.concat(gm.chromosome, gm2.chromosome, byte[].class);
			int[] capabilities = new int[gm.capabilities.length];
			for (int i = 0; i < capabilities.length; ++i)
			{
				capabilities[i] = gm.capabilities[i] + gm2.capabilities[i] >> 1;
			}
			this.info.seed = new BioData(this.card.getFamily().getSpecie(gene),
					self ? this.card.getRegisteredName() + "⊗" : "♀" + this.card.getRegisteredName() + " x ♂" + gm.specie.getRegisteredName(),
							this.info.data.generation + 1,
							gene,
							capabilities);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockHitEffect(this.world, this.random, state.getActualState(this.world, this.pos), target.sideHit, this.pos, manager);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockDestroyEffects(this.world, this.pos, state.getActualState(this.world, this.pos), manager);
		return true;
	}
	
	@Override
	public String getCustomModelData(String key)
	{
		switch (key)
		{
		case "state" :
			return this.card.getRegisteredName();
		case "stage" :
			return Integer.toString(this.stage);
		default:
			return null;
		}
	}
}
