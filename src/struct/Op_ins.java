package struct;

public class Op_ins extends Operand {
	public int value;
	
	public Op_ins(int v) {
		value = v;
	}
	
	public String toString() {
		return "" + value;
	}

	@Override
	public String showDetail() {
		String ret = String.format("|%-25d",value);
		return ret;
	}

	@Override
	public String toRString(int reg_type) {
		return "" + value;
	}

	@Override
	public boolean equals(Operand op) {
		return false;
	}
	
}
