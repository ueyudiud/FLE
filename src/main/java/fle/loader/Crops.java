/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader;

import static farcore.data.M.bristlegrass;
import static farcore.data.M.ramie_dry;
import static farcore.lib.bio.GeneData.m;
import static farcore.lib.bio.GeneData.v;
import static fle.loader.IBFS.iCropRelated;

import java.util.ArrayList;

import farcore.data.MP;
import farcore.lib.crop.Crop;
import farcore.lib.crop.CropFamily;
import farcore.lib.crop.CropOrder;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyEdible;
import fle.core.FLE;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class Crops
{
	public static void init()
	{
		{
			CropFamily.Builder builder = CropFamily.builder("wheat");
			Crop _wheat = new Crop(wheat, "Wheat", 7, 800, 2, 6, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("wheat", (15 + access.rng().nextInt(30 + access.info().grain)) / 20));
				}
			};
			wheat.builder().setCrop(_wheat);
			builder.addDefSpecie(_wheat, v(5, 0, 10, 20, 5), v(5, 40, 10, 10, 10));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("millet");
			Crop _millet = new Crop(millet, "Millet", 7, 900, 2, 6, 3)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("millet", (15 + access.rng().nextInt(30 + access.info().grain)) / 30));
				}
			};
			Crop _bristlegrass = new Crop(bristlegrass, "Bristlegrass", 7, 600, 2, 6, 4)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(2 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("bristlegrass", (15 + access.rng().nextInt(30 + access.info().grain)) / 20));
				}
			};
			millet.builder().setCrop(_millet);
			bristlegrass.builder().setCrop(_bristlegrass);
			builder.addDefSpecie(_bristlegrass, v(0, 0, 10, 5, 5), v(5, 30, 10, 5, 20));
			builder.addSpecie(_millet, 0b1, 0b0, v(10, 0, 10, 5, 5), v(10, 35, 10, 10, 10));
			builder.addGene(0, m(v(7000, 1000, 1500, 500), v(0, 10000, 0, 0), v(0, 0, 6000, 4000), v(0, 0, 4000, 6000)));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("soybean");
			Crop _soybean = new Crop(soybean, "Soybean", 6, 1600, 2, 5, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("soybean_pod", (25 + access.rng().nextInt(20 + access.info().grain)) / 16));
				}
			};
			soybean.builder().setCrop(_soybean);
			builder.addDefSpecie(_soybean, v(8, 0, 5, 0, 0), v(10, 10, 5, 10, 10));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("rutabaga");
			Crop _rutabaga = new Crop(rutabaga, "Rutabaga", 5, 1900, 2, 4, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("rutabaga", (35 + access.rng().nextInt(15 + access.info().grain)) / 40));
				}
			};
			rutabaga.builder().setCrop(_rutabaga);
			builder.addDefSpecie(_rutabaga, v(0, 0, 15, 15, 0), v(15, 15, 10, 10, 5));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("potato");
			Crop _potato = new Crop(potato, "Potato", 6, 1300, 3, 0, 0)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed((55 + access.rng().nextInt(15 + access.info().grain)) / 20, access.info()));
				}
			};
			potato.builder().setCrop(_potato);
			builder.addDefSpecie(_potato, v(5, 5, 20, 0, 3), v(15, 15, 5, 5, 10));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("sweet_potato");
			Crop _sweet_potato = new Crop(sweet_potato, "Sweet Potato", 5, 1500, 3, 0, 0)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed((55 + access.rng().nextInt(15 + access.info().grain)) / 20, access.info()));
				}
			};
			sweet_potato.builder().setCrop(_sweet_potato);
			builder.addDefSpecie(_sweet_potato, v(5, 10, 16, 0, 3), v(15, 5, 9, 5, 10));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("reed");
			Crop _reed = new Crop(reed, "Reed", 5, 1400, 3, 0, 0)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed((35 + access.rng().nextInt(15 + access.info().grain)) / 20, access.info()));
				}
			};
			reed.builder().setCrop(_reed);
			builder.addDefSpecie(_reed, v(20, 0, 0, 0, 0), v(35, 5, 10, 5, 5));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("ramie");
			Crop _ramie = new Crop(ramie, "Ramie", 7, 900, 2, 6, 4)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("ramie_fiber", (10 + access.rng().nextInt(15 + access.info().grain)) / 15));
				}
			};
			ramie.builder().setCrop(_ramie);
			builder.addDefSpecie(_ramie, v(15, 5, 0, 5, 0), v(10, 5, 15, 5, 5));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("flax");
			Crop _flax = new Crop(flax, "Flax", 5, 1500, 3, 0, 0)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					//				TODO list.add(iCropRelated.getSubItem("rutabaga", (35 + access.rng().nextInt(15 + access.info().grain)) / 40));
				}
			};
			flax.builder().setCrop(_flax);
			builder.addDefSpecie(_flax, v(15, 5, 0, 5, 0), v(10, 5, 15, 5, 5));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("cotton");
			Crop _cotton = new Crop(cotton, "Cotton", 5, 1400, 2, 4, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("cotton", (35 + access.rng().nextInt(15 + access.info().grain)) / 20));
				}
			};
			cotton.builder().setCrop(_cotton);
			builder.addDefSpecie(_cotton, v(5, 0, 8, 5, 0), v(10, 15, 12, 5, 5));
			CropOrder.ORDER.addFamily(builder.build());
		}
		{
			CropFamily.Builder builder = CropFamily.builder("cabbage");
			Crop _wild_cabbage = new Crop(wild_cabbage, "Wild Cabbage", 5, 1600, 2, 4, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("wild_cabbage_leaf", (35 + access.rng().nextInt(15 + access.info().grain)) / 30));
				}
			};
			Crop _cabbage = new Crop(cabbage, "Cabbage", 6, 1600, 2, 5, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("cabbage", (35 + access.rng().nextInt(15 + access.info().grain)) / 40));
				}
			};
			Crop _brussels_sprouts = new Crop(brussels_sprouts, "Brussels Sprouts", 6, 1600, 2, 5, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("brussels_sprouts", (35 + access.rng().nextInt(15 + access.info().grain)) / 20));
				}
			};
			Crop _purple_cabbage = new Crop(purple_cabbage, "Purple Cabbage", 6, 1600, 2, 5, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("purple_cabbage", (35 + access.rng().nextInt(15 + access.info().grain)) / 40));
				}
			};
			Crop _cauliflower = new Crop(cauliflower, "Cauliflower", 6, 1600, 2, 5, 5)
			{
				@Override
				public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
				{
					list.add(applyChildSeed(1 + access.rng().nextInt(5 + access.info().vitality) / 30, access.info()));
					list.add(iCropRelated.getSubItem("cauliflower", (35 + access.rng().nextInt(15 + access.info().grain)) / 40));
				}
			};
			wild_cabbage.builder().setCrop(_wild_cabbage);
			cabbage.builder().setCrop(_cabbage);
			brussels_sprouts.builder().setCrop(_brussels_sprouts);
			purple_cabbage.builder().setCrop(_purple_cabbage);
			cauliflower.builder().setCrop(_cauliflower);
			builder.addSpecie(_cauliflower, 0b0010, 0b0000, v(5, 5, 0, 8, 0), v(5, 10, 5, 8, 10));
			builder.addSpecie(_brussels_sprouts, 0b1100, 0b0000, v(5, 5, 0, 8, 0), v(5, 10, 5, 8, 10));
			builder.addSpecie(_purple_cabbage, 0b1001, 0b0000, v(5, 5, 0, 8, 0), v(5, 10, 5, 8, 10));
			builder.addSpecie(_cabbage, 0b1000, 0b0000, v(5, 5, 0, 8, 0), v(5, 10, 5, 8, 10));
			builder.addDefSpecie(_wild_cabbage, v(4, 0, 5, 5, 0), v(5, 10, 5, 8, 15));
			CropOrder.ORDER.addFamily(builder.build());
		}
		
		ramie_dry.builder().setUnificationMaterial(ramie);
		
		potato.builder().addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[] { 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F }));
		sweet_potato.builder().addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[] { 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F }));
		reed.builder().addProperty(MP.property_edible, new PropertyEdible(0.0F, 2.0F, 3.0F, new float[] { 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F }));
	}
	
	// Crops
	public static final Mat	wheat				= new Mat(9001, FLE.MODID, "wheat", "Wheat", "Wheat");
	public static final Mat	millet				= new Mat(9002, FLE.MODID, "millet", "Millet", "Millet");
	public static final Mat	soybean				= new Mat(9003, FLE.MODID, "soybean", "Soybean", "Soybean");
	public static final Mat	potato				= new Mat(9004, FLE.MODID, "potato", "Potato", "Potato");
	public static final Mat	sweet_potato		= new Mat(9005, FLE.MODID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat	cabbage				= new Mat(9006, FLE.MODID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat	reed				= new Mat(9007, FLE.MODID, "reed", "Reed", "Reed");
	public static final Mat	flax				= new Mat(9008, FLE.MODID, "flax", "Flax", "Flax");
	public static final Mat	cotton				= new Mat(9009, FLE.MODID, "cotton", "Cotton", "Cotton");
	public static final Mat	ramie				= new Mat(9010, FLE.MODID, "ramie", "Ramie", "Ramie");
	public static final Mat	wild_cabbage		= new Mat(9011, FLE.MODID, "wild_cabbage", "WildCabbage", "Wild Cabbage");
	public static final Mat	brussels_sprouts	= new Mat(9012, FLE.MODID, "brussels_sprouts", "BrusselsSprouts", "Brussels Sprouts");
	public static final Mat	cauliflower			= new Mat(9013, FLE.MODID, "cauliflower", "Cauliflower", "Cauliflower");
	public static final Mat	purple_cabbage		= new Mat(9014, FLE.MODID, "purple_cabbage", "PurpleCabbage", "Purple Cabbage");
	public static final Mat	rutabaga			= new Mat(9015, FLE.MODID, "rutabaga", "Rutabaga", "Rutabaga");
}
