package farcore.substance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import farcore.collection.ArrayStandardStackList;
import farcore.collection.abs.Stack;

public class Ion implements IParticle
{
	private static Map<String, Ion> register = new HashMap();

	private Stack<IParticle>[] particles;
	
	public static Set<String> getIonNameSet()
	{
		return register.keySet();
	}
	
	public static Collection<Ion> getIonCollection()
	{
		return register.values();
	}
	
	public static Ion ion(String name)
	{
		return register.containsKey(name) ? register.get(name) : new Ion(name);
	}
	
	private final String name;
	
	private Ion(String chemName)
	{
		if(register.containsKey(chemName))
		{
			particles = register.get(chemName).particles;
		}
		else
		{
			particles = new ChemBuilder(chemName).loadAll();
			register.put(chemName, this);
		}
		this.name = chemName;
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
	public ArrayStandardStackList<Atom> getAtomContain()
	{
		ArrayStandardStackList<Atom> list = new ArrayStandardStackList<Atom>();
		for(Stack<IParticle> particle : particles)
			for(Stack<Atom> atom : particle.get().getAtomContain())
				list.add(atom.obj, particle.size * atom.size);
		return list;
	}

	@Override
	public long getParticleCount(IParticle p, boolean divide)
	{
		if(p == null) return 0;
		if(equals(p)) return 1;
		if(divide)
		{
			long size = 0;
			for(Stack<IParticle> particle : particles)
				size += particle.size() * particle.get().getParticleCount(p, divide);
		}
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
		return obj instanceof Ion ? ((Ion) obj).name.equals(name) : false;
	}
}