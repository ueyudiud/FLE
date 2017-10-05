/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import static farcore.data.M.bristlegrass;
import static farcore.lib.bio.GMPredicate.explicit;
import static farcore.lib.bio.GMPredicate.implicit;
import static fle.loader.IBF.iCropRelated;

import farcore.data.M;
import farcore.data.MP;
import farcore.lib.bio.FamilyTemplate;
import farcore.lib.crop.Crop;
import farcore.lib.crop.CropTemplate;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyEdible;
import fle.core.FLE;
import nebula.base.Judgable;

/**
 * @author ueyudiud
 */
public class Crops
{
	public static void init()
	{
		CropTemplate crop1;
		FamilyTemplate<Crop, ICropAccess> family1;
		wheat		.builder().setCrop((crop1 = new CropTemplate(wheat, "Wheat", 7, 800)).setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(6, 5, 2).setDrop(()-> iCropRelated.getSubItem("wheat"), 2));
		family1 = new FamilyTemplate<>("millet");
		bristlegrass.builder().setCrop((crop1 = new CropTemplate(bristlegrass, "Birstlegrass", 7, 600)).setFamily(family1).setMultiplicationProp(4, 6, 2).setDrop(()-> iCropRelated.getSubItem("bristlegrass"), 3));
		family1.addBaseSpecie(crop1);
		millet		.builder().setCrop((crop1 = new CropTemplate(millet, "Millet", 7, 900)).setFamily(family1).setMultiplicationProp(3, 6, 2).setDrop(()-> iCropRelated.getSubItem("millet"), 2));
		family1.addJudgable(Judgable.or(explicit(1), explicit(2), explicit(3), explicit(4)), crop1);
		soybean		.builder().setCrop((crop1 = new CropTemplate(soybean, "Soybean", 6, 1600)).setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(5, 5, 2).setDrop(()-> iCropRelated.getSubItem("soybean_pod"), 2));
		rutabaga	.builder().setCrop((crop1 = new CropTemplate(rutabaga, "Rutabaga", 5, 1900)).setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(4, 5, 2).setDrop(()-> iCropRelated.getSubItem("rutabaga"), 1));
		potato		.builder().setCrop((crop1 = new CropTemplate(potato, "Potato", 6, 1300)).setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(-1, 0, 0).setSeedMul(2));
		sweet_potato.builder().setCrop((crop1 = new CropTemplate(sweet_potato, "SweetPotato", 5, 1500)).setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(-1, 0, 0).setSeedMul(2));
		reed	.builder().setCrop((crop1 = new CropTemplate(reed, "Reed", 5, 1400))	.setFamily(new FamilyTemplate<>(crop1)).setMultiplicationProp(-1, 0, 0).setSeedMul(3));
		flax	.builder().setCrop((crop1 = new CropTemplate(flax, "Flax", 5, 1500))	.setFamily(new FamilyTemplate<>(crop1)));
		cotton	.builder().setCrop((crop1 = new CropTemplate(cotton, "Catton", 5, 1400)).setFamily(new FamilyTemplate<>(crop1)).setDrop(() -> iCropRelated.getSubItem("cotton"), 2));
		family1 = new FamilyTemplate<>("cabbage");
		wild_cabbage	.builder().setCrop((crop1 = new CropTemplate(wild_cabbage, "Wild Cabbage", 5, 1600))		.setFamily(family1).setMultiplicationProp(4, 5, 2).setDrop(()-> iCropRelated.getSubItem("wild_cabbage_leaf"), 1));
		family1.addBaseSpecie(crop1);
		cabbage			.builder().setCrop((crop1 = new CropTemplate(cabbage, "Cabbage", 6, 1600))			.setFamily(family1).setMultiplicationProp(5, 5, 2).setDrop(()-> iCropRelated.getSubItem("cabbage"), 1));
		family1.addJudgable(Judgable.and(explicit(0), implicit(1), implicit(2), implicit(3)), crop1);
		brussels_sprouts.builder().setCrop((crop1 = new CropTemplate(brussels_sprouts, "Brussels Sprouts", 5, 1600))	.setFamily(family1).setMultiplicationProp(4, 5, 2).setDrop(()-> iCropRelated.getSubItem("brussels_sprouts"), 2));
		family1.addJudgable(Judgable.and(explicit(0), explicit(1), implicit(3)), crop1);
		purple_cabbage	.builder().setCrop((crop1 = new CropTemplate(purple_cabbage, "Purple Cabbage", 5, 1600))	.setFamily(family1).setMultiplicationProp(4, 5, 2).setDrop(()-> iCropRelated.getSubItem("purple_cabbage"), 1));
		family1.addJudgable(Judgable.and(explicit(0), implicit(1), explicit(2), implicit(3)), crop1);
		cauliflower		.builder().setCrop((crop1 = new CropTemplate(cauliflower, "Cauliflower", 6, 1600))		.setFamily(family1).setMultiplicationProp(5, 5, 2).setDrop(()-> iCropRelated.getSubItem("cauliflower"), 1));
		family1.addJudgable(explicit(3), crop1);
		
		M.ramie_dry.builder().setUnificationMaterial(ramie);
		
		potato      .builder().addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[]{3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F}));
		sweet_potato.builder().addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[]{3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F}));
		reed        .builder().addProperty(MP.property_edible, new PropertyEdible(0.0F, 2.0F, 3.0F, new float[]{0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F}));
	}
	
	//Crops
	public static final Mat wheat			= new Mat(9001, FLE.MODID, "wheat", "Wheat", "Wheat");
	public static final Mat millet			= new Mat(9002, FLE.MODID, "millet", "Millet", "Millet");
	public static final Mat soybean			= new Mat(9003, FLE.MODID, "soybean", "Soybean", "Soybean");
	public static final Mat potato			= new Mat(9004, FLE.MODID, "potato", "Potato", "Potato");
	public static final Mat sweet_potato	= new Mat(9005, FLE.MODID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat cabbage			= new Mat(9006, FLE.MODID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat reed			= new Mat(9007, FLE.MODID, "reed", "Reed", "Reed");
	public static final Mat flax			= new Mat(9008, FLE.MODID, "flax", "Flax", "Flax");
	public static final Mat cotton			= new Mat(9009, FLE.MODID, "cotton", "Cotton", "Cotton");
	public static final Mat ramie			= new Mat(9010, FLE.MODID, "ramie", "Ramie", "Ramie");
	public static final Mat wild_cabbage	= new Mat(9011, FLE.MODID, "wild_cabbage", "WildCabbage", "Wild Cabbage");
	public static final Mat brussels_sprouts= new Mat(9012, FLE.MODID, "brussels_sprouts", "BrusselsSprouts", "Brussels Sprouts");
	public static final Mat cauliflower		= new Mat(9013, FLE.MODID, "cauliflower", "Cauliflower", "Cauliflower");
	public static final Mat purple_cabbage	= new Mat(9014, FLE.MODID, "purple_cabbage", "PurpleCabbage", "Purple Cabbage");
	public static final Mat rutabaga		= new Mat(9015, FLE.MODID, "rutabaga", "Rutabaga", "Rutabaga");
}