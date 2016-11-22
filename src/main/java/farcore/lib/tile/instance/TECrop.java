package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Update;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_CollisionBoundingBox;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_SelectedBoundingBox;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.abstracts.TEAged;
import farcore.lib.util.Direction;
import farcore.util.U.Client;
import farcore.util.U.NBTs;
import farcore.util.U.Players;
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
implements ICropAccess, IDebugableTile, IUpdatableTile, ITP_CollisionBoundingBox,
ITP_SelectedBoundingBox, ITB_Update, ITP_HarvestCheck, ITP_Drops, ITB_AddDestroyEffects,
ITB_AddHitEffects
{
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(.0625F, .0F, .0625F, .9375F, .9375F, .9375F);
	
	private static final CropInfo NO_DATA_INFO = new CropInfo();

	static
	{
		NO_DATA_INFO.DNA = "";
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
		this(crop, crop.makeNativeDNA(), 0);
		isWild = true;
	}
	public TECrop(ICrop crop, String dna, int generation)
	{
		card = crop;
		info = new CropInfo();
		info.DNA = dna;
		info.generations = generation;
		crop.decodeDNA(this, dna);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("water", waterLevel);
		nbt.setBoolean("isWild", isWild);
		nbt.setFloat("growBuf", growBuffer);
		nbt.setInteger("stage", stage);
		nbt.setString("crop", card.getRegisteredName());
		if(info != null)
		{
			info.writeToNBT(nbt);
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		waterLevel = nbt.getInteger("water");
		isWild = nbt.getBoolean("isWild");
		growBuffer = nbt.getFloat("growBuf");
		stage = nbt.getInteger("stage");
		card = Mat.material(nbt.getString("crop")).getProperty(M.property_crop, ICrop.VOID);
		info = new CropInfo();
		info.readFromNBT(nbt);
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setInteger("s", stage);
		NBTs.setString(nbt, "c", card);
		NBTTagCompound nbt1;
		info.writeToNBT(nbt1 = new NBTTagCompound());
		nbt.setTag("i", nbt1);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		stage = NBTs.getIntOrDefault(nbt, "s", stage);
		Mat material = NBTs.getMaterialByNameOrDefault(nbt, "c", null);
		if(material != null)
		{
			card = material.getProperty(M.property_crop, ICrop.VOID);
		}
		if(nbt.hasKey("i"))
		{
			info = new CropInfo();
			info.readFromNBT(nbt.getCompoundTag("i"));
		}
		markBlockRenderUpdate();
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
		return thisTick + card.tickUpdate(this);
	}

	@Override
	protected void updateServer1()
	{
		super.updateServer1();
		card.onUpdate(this);
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player)
	{
		return Players.matchCurrentToolType(player, EnumToolType.sickle);
	}

	@Override
	public String getDNA()
	{
		return info.DNA;
	}

	@Override
	public ICrop crop()
	{
		return card;
	}

	@Override
	public CropInfo info()
	{
		return info;
	}

	@Override
	public Biome biome()
	{
		return worldObj.getBiomeGenForCoords(pos);
	}

	@Override
	public boolean isWild()
	{
		return isWild;
	}

	@Override
	public Random rng()
	{
		return random;
	}

	@Override
	public int stage()
	{
		return stage;
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
		if(stage + 1 < card.getMaxStage())
		{
			int i = card.getGrowReq(this);
			growBuffer += amt;
			if(growBuffer > i)
			{
				growBuffer -= i;
				stage++;
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
		return waterLevel;
	}
	
	@Override
	public float temp()
	{
		return ThermalNet.getTemperature(worldObj, pos, true);
	}

	@Override
	public int useWater(int amount)
	{
		int c = Math.min(amount, waterLevel);
		waterLevel -= c;
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
		list.add("Name : " + card.getTranslatedName(getDNA()));
		list.add("DNA : " + info.DNA);
		int max = card.getMaxStage();
		int req = card.getGrowReq(this);

		list.add("Grow Progress : " + ChatFormatting.GREEN +
				(stage + 1 < card.getMaxStage() ?
						(int) (growBuffer + stage * req) + "/" + (card.getMaxStage() - 1) * req :
						"Mature"));
		card.addInformation(this, list);
	}

	public boolean canPlantAt()
	{
		return card == null ? true : card.canPlantAt(this);
	}
	
	@Override
	public boolean canBlockStay()
	{
		return worldObj == null || card == ICrop.VOID ? true : card.canPlantAt(this);
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
		return card == null ? EnumPlantType.Crop : card.getPlantType(this);
	}

	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(card != null)
		{
			card.getDrops(this, list);
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
		return card != null ? card.getState(this) : "void";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockHitEffect(worldObj, random, state.getActualState(worldObj, pos), target.sideHit, pos, manager);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		IBlockState state = getBlockState();
		Client.addBlockDestroyEffects(worldObj, pos, state.getActualState(worldObj, pos), manager);
		return true;
	}
}