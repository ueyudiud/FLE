package fle.core.item.tool;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.material.MaterialAbstract;
import fle.api.world.BlockPos;
import fle.core.recipe.RustRecipe;

public class ToolMaterialInfo 
{
	private MaterialAbstract materialBase;
	private MaterialAbstract materialSurface;
	private float coverLevel;
	private MaterialAbstract materialMosaic;
	
	public ToolMaterialInfo(NBTTagCompound nbt) 
	{
		if(nbt == null) return;
		materialBase = MaterialAbstract.getMaterialRegistry().get(nbt.getString("BaseMaterial"));
		materialSurface = MaterialAbstract.getMaterialRegistry().get(nbt.getString("SurfaceMaterial"));
		coverLevel = nbt.getFloat("SurfaceCoverLevel");
		materialMosaic = MaterialAbstract.getMaterialRegistry().get(nbt.getString("MosaicMaterial"));
	}
	public ToolMaterialInfo(MaterialAbstract aMaterial1, MaterialAbstract aMaterial2, float aCover, MaterialAbstract aMaterial3) 
	{
		materialBase = aMaterial1;
		materialSurface = aMaterial2;
		materialMosaic = aMaterial3;
	}
	
	public MaterialAbstract getMaterialBase() 
	{
		return materialBase;
	}
	
	public MaterialAbstract getMaterialSurface()
	{
		return materialSurface;
	}
	
	public MaterialAbstract getMaterialMosaic()
	{
		return materialMosaic;
	}
	
	public void setMaterialMosaic(MaterialAbstract materialMosaic)
	{
		this.materialMosaic = materialMosaic;
	}
	
	public void setMaterialSurface(MaterialAbstract materialSurface, float aLevel)
	{
		this.materialSurface = materialSurface;
		this.coverLevel = aLevel;
	}
	
	public int getToolLevel()
	{
		float a = materialMosaic == null ? 0F : materialMosaic.getPropertyInfo().getHardness();
		float b = (materialBase.getPropertyInfo().getHardness() + (materialSurface == null ? 0F : materialSurface.getPropertyInfo().getHardness()) * coverLevel) / (1 + coverLevel);
		return materialBase == null ? 0 : (int) Math.floor(Math.max(a, b));
	}
	
	public float getHardness()
	{
		if(materialBase == null) return 0F;
		float f1 = 0F;
		float f2 = 0F;
		if(materialMosaic != null) f1 = materialMosaic.getPropertyInfo().getHardness();
		if(materialSurface == null) f2 = materialBase.getPropertyInfo().getHardness();
		else f2 = (materialBase.getPropertyInfo().getHardness() + materialSurface.getPropertyInfo().getHardness() * coverLevel) / (1 + coverLevel * 1.2F);
		return Math.max(f1, f2);
	}

	public static void onRusting(World world, int x, int y, int z, NBTTagCompound tag)
	{
		new ToolMaterialInfo(tag).r(world, x, y, z).writeToNBT(tag);
	}
	
	public ToolMaterialInfo r(World world, int x, int y, int z)
	{
		RustRecipe tRecipe = RustRecipe.getRustRecipe(materialBase);
		if(tRecipe == null) return this;
		float speed = tRecipe.getBaseSpeed(FLE.fle.getThermalNet().getEnvironmentTemperature(new BlockPos(world, x, y, z)), new BlockPos(world, x, y, z).getBiome().getFloatRainfall());
		coverLevel += speed * (Math.min(0, 2F - materialSurface.getPropertyInfo().getDenseness()) * coverLevel) / 200D;
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(materialBase != null)
			nbt.setString("BaseMaterial", MaterialAbstract.getMaterialRegistry().name(materialBase));
		if(materialSurface != null)
			nbt.setString("SurfaceMaterial", MaterialAbstract.getMaterialRegistry().name(materialSurface));
		if(materialMosaic != null)
			nbt.setString("MosaicMaterial", MaterialAbstract.getMaterialRegistry().name(materialMosaic));
		nbt.setFloat("SurfaceCoverLevel", coverLevel);
		return nbt;
	}
	
	public float getCoverLevel() 
	{
		return coverLevel;
	}
	
	public int getMaxUse() 
	{
		return materialBase.getPropertyInfo().getMaxUses();
	}
}