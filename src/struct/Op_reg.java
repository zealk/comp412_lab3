package struct;

public class Op_reg extends Operand {
	
	public int sr;
	public int vr;
	//public int pr;
	public int nextuse;
	public boolean reservePR;
	
	public static final int TOSTRING_SR = 0;
	public static final int TOSTRING_VR = TOSTRING_SR + 1;
	public static final int TOSTRING_PR = TOSTRING_VR + 1;
	
	public Op_reg(int reg_c) {
		sr = reg_c;
		vr = -1;
		//pr = -1;
		reservePR = false; 
		nextuse = Integer.MAX_VALUE;
	}
	
	public String toString() {
		return "r" + sr;
	}

	@Override
	public String showDetail() {
		String ret = String.format("|%-5d|%-5d|%-5d|%-7s", 
				sr,vr,-1,(nextuse==Integer.MAX_VALUE?"∞":""+nextuse));
		return ret;
	}

	@Override
	public String toRString(int reg_type) {
		switch(reg_type) {
		case TOSTRING_SR:
			return "r" + sr;
		case TOSTRING_VR:
			return "r" + vr;
		case TOSTRING_PR:
			return "r" + -1;
		}
		return null;
	}

	@Override
	public boolean equals(Operand op) {
		if (op instanceof Op_reg) {
			return (vr == ((Op_reg) op).vr);
		}
		return false;
	}
}
