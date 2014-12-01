package struct;

import java.util.Set;
import java.util.HashSet;

public class Instruction  {
	
    public static final String REG = "r(\\d+)";
    public static final String COMMA = ",";
    public static final String ASSIGN = "=>";
    public static final String INS = "(\\d+)";
    public static final String SPACE = "\\s+";
    public static final String OSPACE = "\\s*";
	
    public static final String LOAD = "^load" + SPACE + REG + OSPACE 
    		+ ASSIGN + OSPACE + REG + "$";
	public static final String LOADI = "^loadI" + SPACE + INS + OSPACE 
    		+ ASSIGN + OSPACE + REG + "$";
	public static final String STORE = "^store" + SPACE + REG + OSPACE 
    		+ ASSIGN + OSPACE + REG + "$";
	public static final String ADD = "^add" + SPACE + REG + OSPACE 
			+ COMMA + OSPACE + REG + OSPACE + ASSIGN + OSPACE + REG + "$";
	public static final String SUB = "^sub" + SPACE + REG + OSPACE 
			+ COMMA + OSPACE + REG + OSPACE + ASSIGN + OSPACE + REG + "$";
	public static final String MULT = "^mult" + SPACE + REG + OSPACE 
			+ COMMA + OSPACE + REG + OSPACE + ASSIGN + OSPACE + REG + "$";
	public static final String LSHIFT = "^lshift" + SPACE + REG + OSPACE 
			+ COMMA + OSPACE + REG + OSPACE + ASSIGN + OSPACE + REG + "$";
	public static final String RSHIFT = "^rshift" + SPACE + REG + OSPACE 
			+ COMMA + OSPACE + REG + OSPACE + ASSIGN + OSPACE + REG + "$";
	public static final String OUTPUT = "^output" + SPACE + INS + "$";
	public static final String NOP = "^nop$";
	
	public static final String INSTRUCTION[] = new String[] {
			Instruction.LOAD,
			Instruction.LOADI,
			Instruction.STORE,
			Instruction.ADD,
			Instruction.SUB,
			Instruction.MULT,
			Instruction.LSHIFT,
			Instruction.RSHIFT,
			Instruction.OUTPUT,
			Instruction.NOP,
		};
	
	public enum optype {
		load,
		loadI,
		store,
		add,
		sub,
		mult,
		lshift,
		rshift,
		output,
		nop,
	}
	
	public optype operation;
	public Operand operand[];
	
	public Set<Instruction> parents;
	public Set<Instruction> children;
	
	
	
	public void DGNode_init() {
		parents = new HashSet<Instruction>();
		children = new HashSet<Instruction>();
	}
	
	
	public Op_reg getUseReg(int i) {
		if (i < 0 || i > 1) {
			return null;
		}
		if (operation == optype.store && i == 1) {
				i++;
		}
		if (operand != null && operand[i] != null && operand[i] instanceof Op_reg) {
			return (Op_reg)operand[i];
		}
		return null;
	}
	
	
	public Op_reg getDefReg() {
		if (operation == optype.store) {
			return null;
		}
		if (operand != null && operand[2] != null && operand[2] instanceof Op_reg) {
			return (Op_reg)operand[2];
		}
		return null;
	}
	
	
	public Op_ins getIns() {
		if (operand != null && operand[0] != null && operand[0] instanceof Op_ins) {
			return (Op_ins)operand[0];
		}
		return null;
	}
	
	
	public static Instruction.optype getType(int i) {
		return Instruction.optype.values()[i];
	}
	
	
	public void init (optype type, int[] oprds) {
		operation = type;
		operand = new Operand[3];
		switch(type) {
		case load:
		case store:
			operand[0] = new Op_reg(oprds[0]);
			operand[2] = new Op_reg(oprds[1]);
			break;
		case loadI:
			operand[0] = new Op_ins(oprds[0]);
			operand[2] = new Op_reg(oprds[1]);
			break;
		case add:
		case sub:
		case mult:
		case lshift:
		case rshift:
			operand[0] = new Op_reg(oprds[0]);
			operand[1] = new Op_reg(oprds[1]);
			operand[2] = new Op_reg(oprds[2]);
			break;
		case output:
			operand[0] = new Op_ins(oprds[0]);
			break;
		case nop:
			break;
		default:
			break;
			
		}
	}
	
	public String toString() {
		String ret = operation.toString();
		ret += "\t";
		switch (operation) {
		case load:
		case loadI:
		case store:
			ret += operand[0];
			ret += " => ";
			ret += operand[2];
			break;
		case add:
		case sub:
		case mult:
		case lshift:
		case rshift:
			ret += operand[0];
			ret += " , ";
			ret += operand[1];
			ret += " => ";
			ret += operand[2];
			break;
		case output:
			ret += operand[0];
			break;
		case nop:
			break;
		default:
			break;
			
		}
		return ret;
	}

	public void showDetail() {
		String ret = String.format("%-6s", operation);
		for (int i = 0 ; i < 3 ;i++) {
			if (operand[i] == null) {
				ret += String.format("|%-25s","-");
			} else {
				ret += operand[i].showDetail();
			}
		}
		System.out.println(ret);
	}

	public String toRString(int reg_type) {
		String ret = operation.toString();
		ret += " ";
		switch (operation) {
		case load:
		case loadI:
		case store:
			ret += operand[0].toRString(reg_type);
			ret += " => ";
			ret += operand[2].toRString(reg_type);
			break;
		case add:
		case sub:
		case mult:
		case lshift:
		case rshift:
			ret += operand[0].toRString(reg_type);
			ret += " , ";
			ret += operand[1].toRString(reg_type);
			ret += " => ";
			ret += operand[2].toRString(reg_type);
			break;
		case output:
			ret += operand[0].toRString(reg_type);
			break;
		case nop:
			break;
		default:
			break;
			
		}
		return ret;
	}

	
}
