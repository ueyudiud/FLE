package farcore.item.enums;

public enum EnumItemSize
{
	large(256),
	big(64),
	medium(16),
	small(4),
	tiny(1);

	public final int size;
	public final int maxStackSize;
	
	EnumItemSize(int size)
	{
		this.size = size;
		this.maxStackSize = Math.min(64, 256 / size);
	}
}