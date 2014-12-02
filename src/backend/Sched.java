package backend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import struct.DGEdge;
import struct.Instruction;
import struct.Op_reg;
import main.Scheduler;

public class Sched {

	public Scheduler s;
	public List<Instruction> ready;
	public Set<Instruction> active;
	public Set<Instruction> done;	//used to remove operation from active set.
	public Instruction[] assigned;
	public int unemitted;
	
	public Sched(Scheduler scheduler) {
		s = scheduler;
		ready = s.ready;
		assigned = new Instruction[2];
		active = new HashSet<Instruction>();
		done = new HashSet<Instruction>();
		unemitted = s.insts.size();
	}

	public void run() {
		
		do {
			//remove those done in active and make their parents active if ready
			for (Instruction ins : active) {
				if (ins.cycle == 0) {
					done.add(ins);
				}
			}
			for (Instruction ins : done) {
				release(ins);
			}
			done.clear();
			
			assigned[0] = selectReady0();
			assigned[1] = selectReady1();
			if (assigned[0] != null)
				removeSerialize(assigned[0]);
			if (assigned[1] != null)
				removeSerialize(assigned[1]);
			
			emitOp(assigned);
			
			for (Instruction ins : active) {
				ins.cycle--;
			}
		} while ( unemitted > 0 );
	}

	private void emitOp(Instruction assigned[]) {
		System.out.print("[");
		if (assigned[0] != null ) {
			System.out.print(assigned[0].toRString(Op_reg.TOSTRING_VR));
		} else {
			System.out.print("nop");
		}
		System.out.print("; ");
		if (assigned[1] != null) {
			System.out.print(assigned[1].toRString(Op_reg.TOSTRING_VR));
		} else {
			System.out.print("nop");
		}
		System.out.println("]");
	}

	private void activate(Instruction ins) {
		ready.remove(ins);
		active.add(ins);
		ins.cycle = ins.latency();
		unemitted --;
	}
	
	private void removeSerialize(Instruction assigned) {
		Instruction parent;
		for (DGEdge parent_edge : assigned.parents) {
			if (parent_edge.len == 1) {		
				parent = parent_edge.node;
				parent.ready_count--;
				if (parent.ready_count == 0) {
					ready.add(parent);
				}
			}
		}
	}

	private Instruction selectReady1() {
		if (ready.isEmpty())
			return null;
		for (Instruction ins : ready) {
			if (ins.capable(1)) {
				activate(ins);
				return ins;
			}
		}
		return null;
	}

	private Instruction selectReady0() {
		if (ready.isEmpty())
			return null;
		for (Instruction ins : ready) {
			if (ins.capable(0)) {
				activate(ins);
				return ins;
			}
		}
		return null;
	}

	private void release(Instruction ins) {
		Instruction parent;
		for (DGEdge parent_edge : ins.parents) {
			if (parent_edge.len > 1) {		
				//don't remove len = 1 edge, it is already remove in removeSerialize when child moved in!
				parent = parent_edge.node;
				parent.ready_count--;
				if (parent.ready_count == 0) {
					ready.add(parent);
				}
			}
		}
		active.remove(ins);
	}
	

}
