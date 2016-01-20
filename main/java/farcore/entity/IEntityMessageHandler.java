package farcore.entity;

public interface IEntityMessageHandler
{
	void process(byte type, Object obj);
}