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
		int ins_number = s.insts.size();
		Op_reg tmpReg;
		for (int i = ins_number - 1 ; i >= 0 ; i--) {

			tmpReg = s.insts.get(i).getDefReg();
			update(tmpReg,i);
			if (tmpReg != null) {
				SRToVR[tmpReg.sr] = -1;
				LU[tmpReg.sr] = Integer.MAX_VALUE;
			}
			tmpReg = s.insts.get(i).getUseReg(0);
			update(tmpReg,i);
			tmpReg = s.insts.get(i).getUseReg(1);
			update(tmpReg,i);
			
		}
		//show();
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
