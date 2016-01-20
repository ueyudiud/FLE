package farcore.substance;

import java.util.HashMap;
import java.util.Map;

import farcore.collection.ArrayStandardStackList;
import farcore.collection.abs.Stack;
import farcore.util.FleLog;

public class AtomRadical implements IParticle
{
	private static final Map<String, AtomRadical> register = new HashMap();
	public static AtomRadical radical(String chemName)
	{
		if(register.containsKey(chemName))
			return register.get(chemName);
		return new AtomRadical(chemName);
	}
	private static Stack<IParticle>[] loadParticles(String name)
	{
		ChemBuilder builder = new ChemBuilder(name);
		try
		{			
			return builder.loadAll();
		}
		catch(Exception exception)
		{
			FleLog.error("The particles register with name " + name + " couldn't decode to particles!", exception);
			return new Stack[0];
		}
	}
	
	private Stack<IParticle>[] particles;
	private final String name;
	
	private AtomRadical(String name)
	{
		if(register.containsKey(name))
		{
			particles = register.get(name).particles;
		}
		else
		{
			particles = loadParticles(name);
			register.put(name, this);
		}
		this.name = name;
	}

	@Override
	public ArrayStandardStackList<Atom> getAtomContain()
	{
		ArrayStandardStackList<Atom> list = new ArrayStandardStackList<Atom>();
		for(Stack<IParticle> particle : particles)
			for(Stack<Atom> atom : particle.get().getAtomContain())
				list.add(atom.obj, particle.size * atom.size);
		return list;
	}
	
	@Override
	public long getSize()
	{
		long size = 0;
		for(Stack<IParticle> particle : particles)
			size += particle.size() * particle.get().getSize();
		return size;
	}

	@Override
	public long getParticleCount(IParticle p, boolean matchIon)
	{
		if(p == null) return 0;
		if(equals(p)) return 1;
		long size = 0;
		for(Stack<IParticle> particle : particles)
			size += particle.size() * particle.get().getParticleCount(p, matchIon);
		return 0;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof AtomRadical ? ((AtomRadical) obj).name.equals(name) : false;
	}
}