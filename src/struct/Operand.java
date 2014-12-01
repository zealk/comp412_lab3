package struct;

public abstract class Operand {

	public abstract String showDetail();

	public abstract String toRString(int reg_type);

	public abstract boolean equals(Operand op);
}