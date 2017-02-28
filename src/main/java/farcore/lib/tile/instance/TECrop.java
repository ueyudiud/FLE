package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumToolTypes;
import farcore.data.MP;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import nebula.client.util.Client;
import nebula.common.data.Misc;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_Update;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TEAged;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import nebula.common.util.Players;
import nebula.common.util.Worlds;
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

public class TECrop extends TEAged
implements ICropAccess, IDebugableTile, IUpdatableTile, ITP_BoundingBox, ITB_Update, ITP_HarvestCheck, ITP_Drops, ITB_AddDestroyEffects,
ITB_AddHitEffects
{
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0F, .0F, 0.0F, 1.0F, .125F, 1.0F);
	
	private static final CropInfo NO_DATA_INFO = new CropInfo();
	
	static
	{
		NO_DATA_INFO.geneticMaterial = new GeneticMaterial("", -1, Misc.LONGS_EMPTY, Misc.INTS_EMPTY);
	}
	
	private int waterLevel = 6400;
	private boolean isWild = false;
	private float growBuffer;
	private int stage = 1;
	private ICrop card = ICrop.VOID;
	private CropInfo info = NO_DATA_INFO;
	
	public TECrop()
	{
		
	}
	public TECrop(ICrop crop)
	{
		this(crop, crop.createNativeGeneticMaterial());
		this.isWild = true;
	}
	public TECrop(ICrop crop, GeneticMaterial geneticMaterial)
	{
		this.card = crop;
		this.info = new CropInfo();
		this.info.geneticMaterial = geneticMaterial;
		this.card.expressTrait(this, geneticMaterial);
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
		if(this.info != null)
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
		this.card = Mat.material(nbt.getString("crop")).getProperty(MP.property_crop, ICrop.VOID);
		this.info = new CropInfo();
		this.info.readFromNBT(nbt);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setInteger("s", this.stage);
		NBTs.setString(nbt, "c", this.card);
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
		if(material != null)
		{
			this.card = material.getProperty(MP.property_crop, ICrop.VOID);
		}
		if(nbt.hasKey("i"))
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
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
			Entity entity)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return CROP_AABB;
	}
	
	public String getStateName()
	{
		return this.card.getRegisteredName() + "_" + this.stage;
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
		return Players.matchCurrentToolType(player, EnumToolTypes.SICKLE);
	}
	
	@Override
	public ICrop crop()
	{
		return this.card;
	}
	
	@Override
	public CropInfo info()
	{
		return this.info;
	}
	
	@Override
	public GeneticMaterial getGeneticMaterial()
	{
		return this.info.geneticMaterial;
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
		if(this.stage < this.card.getMaxStage())
		{
			int i = this.card.getGrowReq(this);
			this.growBuffer += amt;
			if(this.growBuffer > i)
			{
				this.growBuffer -= i;
				this.stage++;
				markBlockUpdate();//BUD?
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
		list.add("Name : " + this.card.getLocalName(this.info.geneticMaterial));
		
		this.card.addInformation(this, list);
	}
	
	public boolean canPlantAt()
	{
		return this.card == null ? true : this.card.canPlantAt(this);
	}
	
	@Override
	public boolean canBlockStay()
	{
		return this.world == null || this.card == ICrop.VOID ? true : this.card.canPlantAt(this);
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if(!canBlockStay())
		{
			Worlds.spawnDropsInWorld(this, getDrops(state, 0, false));
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
		if(this.card != null)
		{
			this.card.getDrops(this, list);
		}
		return list;
	}
	
	@Override
	public void onUpdateTick(IBlockState state, Random random, boolean isTickRandomly)
	{
		if(!canPlantAt())
		{
			removeBlock();
		}
	}
	
	@Override
	public void pollinate(GeneticMaterial gm)
	{
		if(this.info.gamete == null && this.card.getRegisteredName().equals(gm.specie))
		{
			this.info.gamete = this.card.createGameteGeneticMaterial(this, gm);
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
}