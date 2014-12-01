package struct;

public class Regs {
	
	public int Size;
	public int[] Name;
	public int[] Next;
	public int[] Stack;
	public int Stacktop;
	/*VRToPR 
	 * >=  0: vr is in pr
	 *  = -1: vr has not used or freed
	 *  = -2: vr has been spilled and stored in memory
	 */
	public int[] VRToPR;
	/*For result direct from loadI, rematerializable
	 * >=  0: actual instant number
	 *  = -1: not vaild
	 */
	public int[] VRToIns;
	
	/*
	 * If VRClean[i] = true,
	 * you can spill without generate store code,
	 * because the value of VRi is already in stack
	 */
	public boolean[] VRClean;

	public Regs(int pr_c, int vr_c) {
		Size = pr_c;
		Name = new int[pr_c];
		Next = new int[pr_c];
		Stack = new int[vr_c];
		Stacktop = 0;
		VRToPR = new int[vr_c];
		VRToIns = new int[vr_c];
		VRClean = new boolean[vr_c];
		for (int i = 0 ; i < pr_c ;i++) {
			Name[i] = -1;
			Next[i] = -1;
		}
		for (int i = 0 ; i < vr_c ;i++) {
			VRToPR[i] = -1;
			Stack[i] = -1;
			VRToIns[i] = -1;
			VRClean[i] = false;
		}
	}

	public boolean push(int i) {
		if (Stacktop == Stack.length) {
			System.err.println("Try to push to full class stack!");
			return false;
		}
		Stack[Stacktop++] = i;
		return true;
	}
	
	public int pop() {
		if (Stacktop == 0) {
			System.err.println("Try to pop from empty class stack!");
		}
		
		return Stack[--Stacktop];
	}
	
	public void show_reg() {
		System.out.println("VRToPR:");
		String ret1 = "";
		String ret2 = "";
		String ret3 = "";
		for (int i = 0 ; i < VRToPR.length ; i++) {
			ret1 += String.format("|%-4d", i);
			ret2 += "-----";
			ret3 += String.format("|%-4s", VRToPR[i]==-1?"-":""+VRToPR[i]);
		}
		System.out.println(ret1);
		System.out.println(ret2);
		System.out.println(ret3);
		
		System.out.println("PRToVR:");
		ret1 = "";
		ret2 = "";
		ret3 = "";
		String ret4 = "";
		for (int i = 0 ; i < Size ; i++) {
			ret1 += String.format("|%-4d", i);
			ret2 += "-----";
			ret3 += String.format("|%-4s", Name[i]==-1?"-":""+Name[i]);
			ret4 += String.format("|%-4s", Next[i]==-1?"-":""+Next[i]);
		}
		System.out.println(ret1);
		System.out.println(ret2);
		System.out.println(ret3);
		System.out.println(ret4);
	}
	
	public String stacktostr() {
		String ret = "";
		ret += Stacktop;
		ret += " || ";
		for (int i = 0 ; i < Stacktop ;i++) {
			ret += Stack[i];
			ret += " | ";
		}
		return ret;
	}
	
}
