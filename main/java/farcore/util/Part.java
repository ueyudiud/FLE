package farcore.util;

import java.util.Locale;

import farcore.collection.Register;
import farcore.enums.EnumParticleSize;
import farcore.substance.PhaseDiagram.Phase;
import flapi.util.SubTag;

public class Part
{
	private static final Register<Part> partRegister = new Register();

	public static final Part single			= part("single", EnumParticleSize.colloid, 1);
	
	public static final Part cube 			= part("cube", EnumParticleSize.block, 5184);
	public static final Part dirt 			= part("dirt", EnumParticleSize.colloid, 5184);
	public static final Part sand			= part("sand", EnumParticleSize.dust, 5184);
	public static final Part oreCube		= part("ore.cube", EnumParticleSize.block, 5184);
	public static final Part ore			= part("ore", EnumParticleSize.chunk, 1296);
	public static final Part cubeSimple		= part("cube.simple", EnumParticleSize.block, 5184);

	public static final Part liquid			= part("liquid",		EnumParticleSize.fluid, 	Phase.LIQUID,		1);
	public static final Part gas			= part("gas",			EnumParticleSize.fluid, 	Phase.GAS,			1);
	public static final Part dust			= part("dust",			EnumParticleSize.dust,		Phase.SOLID, 120F,	576);
	public static final Part powder			= part("powder",		EnumParticleSize.dust,		Phase.SOLID, 225F,	576);
	public static final Part ingot			= part("ingot",			EnumParticleSize.ingotic,	Phase.SOLID, 1F,	576);
	public static final Part ingotDouble	= part("ingot.double",	EnumParticleSize.ingotic,	Phase.SOLID, 0.9F,	1152);
	public static final Part ingotQuadruple	= part("ingot.quadruple",EnumParticleSize.ingotic,	Phase.SOLID, 0.8F,	2304);
	public static final Part plate			= part("plate",			EnumParticleSize.ingotic,	Phase.SOLID, 2F,	576);
	public static final Part plateDouble	= part("plate.double",	EnumParticleSize.ingotic,	Phase.SOLID, 1.7F,	1152);
	public static final Part plateQuadruple	= part("plate.quadruple",EnumParticleSize.ingotic,	Phase.SOLID, 1.5F,	2304);
	public static final Part nugget			= part("nugget",		EnumParticleSize.nuggetic,	Phase.SOLID, 4.0F,	64);
	public static final Part chunk			= part("chunk",			EnumParticleSize.nuggetic,	Phase.SOLID, 2.5F,	144);
	public static final Part waste			= part("waste",			EnumParticleSize.ingotic,	Phase.SOLID, 8.0F,	144);
	public static final Part stick			= part("stick",			EnumParticleSize.ingotic,	Phase.SOLID, 32F,	192);
	
	public static final Part stickLong		= part("stick.long",	EnumParticleSize.ingotic,	Phase.SOLID, 24F,	576);
	public static final Part stickSimple	= part("stick.simple",	EnumParticleSize.ingotic,	Phase.SOLID, 32F,	192);
	public static final Part stickShort		= part("stick.short",	EnumParticleSize.nuggetic,	Phase.SOLID, 40F,	128);
	public static final Part dustCube		= part("dust.cube",		EnumParticleSize.dust,		Phase.SOLID, 120F,	5184);
	public static final Part dustLarge		= part("dust.large",	EnumParticleSize.dust,		Phase.SOLID, 120F,	1296);
	public static final Part dustSimple		= part("dust.simple",	EnumParticleSize.dust,		Phase.SOLID, 120F,	576);
	public static final Part dustSmall		= part("dust.small",	EnumParticleSize.dust,		Phase.SOLID, 120F,	144);
	public static final Part dustTiny		= part("dust.tiny",		EnumParticleSize.dust,		Phase.SOLID, 120F,	64);
	public static final Part dustExiguity	= part("dust.exiguity",	EnumParticleSize.dust,		Phase.SOLID, 120F,	16);
	public static final Part powderCube		= part("powder.cube",	EnumParticleSize.dust,		Phase.SOLID, 225F,	5184);
	public static final Part powderLarge	= part("powder.large",	EnumParticleSize.dust,		Phase.SOLID, 225F,	1296);
	public static final Part powderSimple	= part("powder.simple",	EnumParticleSize.dust,		Phase.SOLID, 225F,	576);
	public static final Part powderSmall	= part("powder.small",	EnumParticleSize.dust,		Phase.SOLID, 225F,	144);
	public static final Part powderTiny		= part("powder.tiny",	EnumParticleSize.dust,		Phase.SOLID, 225F,	64);
	public static final Part powderExiguity	= part("powder.exiguity",EnumParticleSize.dust,		Phase.SOLID, 225F,	16);
	public static final Part chunkLarge		= part("chunk.large",	EnumParticleSize.ingotic,	Phase.SOLID, 1.7F,	576);
	public static final Part chunkSimple	= part("chunk.simple",	EnumParticleSize.nuggetic,	Phase.SOLID, 2.5F,	144);
	public static final Part wasteSimple	= part("waste.simple",	EnumParticleSize.ingotic,	Phase.SOLID, 24F,	144);
	public static final Part wasteSmall		= part("waste.small",	EnumParticleSize.ingotic,	Phase.SOLID, 48F,	64);
	public static final Part wasteTiny		= part("waste.tiny",	EnumParticleSize.ingotic,	Phase.SOLID, 96F,	16);
	
	static
	{
		stickLong.setBasePart(stick);
		stickSimple.setEquivalencePart(stick);
		stickShort.setBasePart(stick);
		dustCube.setBasePart(dust);
		dustLarge.setBasePart(dust);
		dustSimple.setEquivalencePart(dust);
		dustSmall.setBasePart(dust);
		dustTiny.setBasePart(dust);
		dustExiguity.setBasePart(dust);
		powderCube.setBasePart(powder);
		powderLarge.setBasePart(powder);
		powderSimple.setEquivalencePart(powder);
		powderSmall.setBasePart(powder);
		powderTiny.setBasePart(powder);
		powderExiguity.setBasePart(powder);
		chunkLarge.setBasePart(chunk);
		chunkSimple.setEquivalencePart(chunk);
		wasteSimple.setEquivalencePart(waste);
		wasteSmall.setBasePart(waste);
		wasteTiny.setBasePart(waste);
		ingotDouble.setBasePart(ingot);
		ingotQuadruple.setBasePart(ingot);
		plateDouble.setBasePart(plate);
		plateQuadruple.setBasePart(plate);
	}
	
	public static Register<Part> values()
	{
		return partRegister;
	}
	
	public static Part get(String name)
	{
		return partRegister.get(name);
	}

	public static Part part(String name, EnumParticleSize size, int resolution)
	{
		return part(name, size, Phase.SOLID, resolution);
	}
	public static Part part(String name, EnumParticleSize size, Phase phase, int resolution)
	{
		return part(name, size, phase, phase.area, resolution);
	}
	public static Part part(String name, EnumParticleSize size, Phase phase, float specificArea, int resolution)
	{
		return partRegister.contain(name) ? partRegister.get(name) : 
			new Part(name, resolution, size, phase, specificArea, SubTag.getNewSubTag("PART_" + name.toUpperCase(Locale.ENGLISH)));
	}
	
	public final String name;
	public final int resolution;
	public final EnumParticleSize size;
	public final Phase phase;
	/** Handle in reaction system, the standard solid (A block) is 1.0 */
	public final float specificArea;
	private Part equivalencePart = this;
	private Part basePart = this;
	public SubTag partTag;

	private Part(String name, int resolution, EnumParticleSize size, Phase phase, float sa, SubTag tag)
	{
		if(name == null || name.length() == 0) throw new IllegalArgumentException("Part name can not be null or nothing!");
		this.name = name;
		this.specificArea = sa;
		this.resolution = resolution;
		this.partTag = tag;
		this.size = size;
		this.phase = phase;
		partRegister.register(this, name);
	}

	public Part setBasePart(Part part)
	{
		this.basePart = part;
		return this;
	}
	
	public Part setEquivalencePart(Part part)
	{
		this.basePart = this.equivalencePart = part;
		return this;
	}
	
	public Part getEquivalencePart()
	{
		return equivalencePart;
	}
	
	public Part getBasePart()
	{
		return basePart;
	}
}