/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

/**
 * @author ueyudiud
 */
class MethodTag
{
	ClassTag owner;
	String name;
	ClassTag[] parameters;
	ClassTag result;
	
	MethodTag(ClassTag owner, String name, ClassTag[] parameters, ClassTag result)
	{
		this.owner = owner;
		this.name = name;
		this.parameters = parameters;
		this.result = result;
	}
	
	private String desc;
	
	String desc()
	{
		if (this.desc == null)
		{
			StringBuilder builder = new StringBuilder();
			builder.append('(');
			for (ClassTag tag : this.parameters)
			{
				builder.append(tag.desc());
			}
			builder.append(')');
			builder.append(this.result.desc());
			this.desc = builder.toString();
		}
		return this.desc;
	}
}
