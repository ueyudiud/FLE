package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumToolType;
import farcore.data.MP;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.bio.DNAPair;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Update;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.abstracts.TEAged;
import farcore.lib.util.Direction;
import farcore.util.NBTs;
import farcore.util.Players;
import farcore.util.U.Client;
import farcore.util.U.Worlds;
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
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(.0625F, .0F, .0625F, .9375F, .9375F, .9375F);
	
	private static final CropInfo NO_DATA_INFO = new CropInfo();
	
	static
	{
		NO_DATA_INFO.geneticMaterial = GeneticMaterial.newGeneticMaterial("", -1, new DNAPair[0]);
	}
	
	private int waterLevel = 6400;
	private boolean isWild = false;
	private float growBuffer;
	private int stage;
	private ICrop card = ICrop.VOID;
	private CropInfo info = NO_DATA_INFO;
	
	public TECrop()
	{
		
	}
	public TECrop(ICrop crop)
	{
		this(crop, crop.applyNativeDNA(), 0);
		this.isWild = true;
	}
	public TECrop(ICrop crop, GeneticMaterial geneticMaterial, int generation)
	{
		this.card = crop;
		this.info = new CropInfo();
		this.info.geneticMaterial = geneticMaterial;
		this.info.generations = generation;
		this.info.geneticMaterial.expressTrait(this);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("water", this.waterLevel);
		nbt.setBoolean("isWild", this.isWild);
		nbt.setFloat("growBuf", this.growBuffer);
		nbt.setInteger("stage", this.stage);
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
		this.stage = nbt.getInteger("stage");
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
		Mat material = NBTs.getMaterialByNameOrDefault(nbt, "c", null);
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
		return BlockCrop.CropSelectBoxHeightHandler.INSTANCE.getSelectBoundBox(getStateName(), CROP_AABB);
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
		return Players.matchCurrentToolType(player, EnumToolType.sickle);
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
		return this.worldObj.getBiomeGenForCoords(this.pos);
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
		if(this.stage + 1 < this.card.getMaxStage())
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
		return ThermalNet.getTemperature(this.worldObj, this.pos, true);
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
		list.add("Tag : " + getStateName());
		list.add("Name : " + this.card.getTranslatedName(this.info.geneticMaterial));
		list.add("DNA : " + this.info.geneticMaterial.getDNAString());
		int max = this.card.getMaxStage();
		int req = this.card.getGrowReq(this);
		
		this.card.addInformation(this, list);
	}
	
	public boolean canPlantAt()
	{
		return this.card == null ? true : this.card.canPlantAt(this);
	}
	
	@Override
	public boolean canBlockStay()
	{
		return this.worldObj == null || this.card == ICrop.VOID ? true : this.card.canPlantAt(this);
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
	
	public String getStateName()
	{
		return this.card != null ? this.card.getState(this) : "void";
	}
	
	@Override
	public void pollinate(GeneticMaterial gm)
	{
		if(this.info.gamete == null && this.card.getRegisteredName().equals(gm.specie))
		{
			this.info.gamete = GeneticMaterial.newGeneticMaterial(this, this.random, this.info.geneticMaterial.generateGameteDNA(this, this.random, true), gm);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockHitEffect(this.worldObj, this.random, state.getActualState(this.worldObj, this.pos), target.sideHit, this.pos, manager);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockDestroyEffects(this.worldObj, this.pos, state.getActualState(this.worldObj, this.pos), manager);
		return true;
	}
}