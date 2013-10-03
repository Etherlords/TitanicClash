package utils.io;

public interface IStreamOperator
{
	Object getValue();
	void setValue(Object value);
	Boolean isStaticSize();
}