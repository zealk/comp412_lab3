package backend;

import struct.Op_reg;

public class Allocator {

	private main.Scheduler s;
	private int[] SRToVR;
	private int[] LU;
	private int vrname;
	
	public Allocator(main.Scheduler Scheduler) {
		s = Scheduler;
		SRToVR = new int[s.max_sr + 1];
		LU = new int[s.max_sr + 1];
		vrname = 0;
		for (int i = 0 ; i < LU.length ;i++) {
			LU[i] = Integer.MAX_VALUE;
			SRToVR[i] = -1;
		}
	}

	public void update(Op_reg op, int index) {
		if (op == null)
			return;
		if (SRToVR[op.sr] == -1) {
			SRToVR[op.sr] = vrname++;
		}
		op.vr = SRToVR[op.sr];
		op.nextuse = LU[op.sr];
		LU[op.sr] = index;
	}
	
	public void assign_vr() {
		s.max_living_vr = 0;
		int ins_number = s.insts.size();
		for (int i = ins_number - 1 ; i >= 0 ; i--) {

			Op_reg defReg = s.insts.get(i).getDefReg();
			update(defReg,i);
			if (defReg != null) {
				SRToVR[defReg.sr] = -1;
				LU[defReg.sr] = Integer.MAX_VALUE;
			}
			update(s.insts.get(i).getUseReg(0),i);
			update(s.insts.get(i).getUseReg(1),i);
			
			calc_living();
		}
		//show();
	}
	
	private void calc_living() {
		int living = 0;
		for (int i = 0; i < SRToVR.length ;i++) {
			if (SRToVR[i] >= 0)
				living ++;
		}
		if (living > s.max_living_vr)
			s.max_living_vr = living;
	}

	public boolean valid_op_reg(int ins_idx,int op_idx) {
		if (s.insts.get(ins_idx).operand[op_idx] != null && 
				s.insts.get(ins_idx).operand[op_idx] instanceof Op_reg)
			return true;
		return false;
	}

	public void run() {
		assign_vr();
	}

	public void show() {
		System.out.println("Instructions:");
		for (int i = 0 ; i < s.insts.size() ;i++) {
			s.insts.get(i).showDetail();
		}
	}
	
}
