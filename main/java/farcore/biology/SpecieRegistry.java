package farcore.biology;

import net.minecraft.nbt.NBTTagCompound;

import farcore.collection.Register;
import farcore.nbt.NBTLoader;
import farcore.nbt.NBTSaver;

public class SpecieRegistry
{
	// NBT Saver and Loader start
	public static final NBTSaver SAVER = new NBTSaver()
	{
		@Override
		public void save(String name, Object obj, NBTTagCompound nbt)
		{
			IBiology biology = (IBiology) obj;
			if (biology != null)
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setString("specie", biology.getSpecie().fullName());
				nbt.setTag(name, biology.writeToNBT(nbt1));
			}
		}
		
		@Override
		public boolean canSave(Class clazz)
		{
			return IBiology.class.isAssignableFrom(clazz);
		}
	};
	public static final NBTLoader LOADER = new NBTLoader()
	{
		@Override
		public ISpecies load(String name, NBTTagCompound tag)
		{
			ISpecies species = register
					.get(tag.getCompoundTag(name).getString("specie"));
			if (species != null)
			{
				species.instance().readFromNBT(tag.getCompoundTag(name));
			}
			return null;
		}
		
		@Override
		public boolean canLoad(Class clazz)
		{
			return IBiology.class.isAssignableFrom(clazz);
		}
	};
	
	private static final Register<ISpecies> register = new Register<ISpecies>();
	
	public static void registrySpecie(ISpecies specie)
	{
		if (register.contain(specie.name()))
			throw new RuntimeException("The specie name " + specie.name()
					+ " had already registered.");
		register.register(specie, specie.fullName());
	}
	
	public static <T extends IBiology> T variation(T biology)
	{
		DNA dna = biology.getDNA();
		dna.variation(biology.getDeformity());
		return null;
	}
}