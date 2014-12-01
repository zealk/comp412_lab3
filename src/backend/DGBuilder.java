package backend;


import java.util.List;

import struct.Instruction;
import struct.Op_reg;
import main.Scheduler;

public class DGBuilder {

	public Scheduler s;
	
	public DGBuilder(Scheduler scheduler) {
		s = scheduler;
	}

	public void run() {
		buildDG();
	}

	private void buildDG() {
		List<Instruction> ins = s.insts;
		Instruction ins_i,ins_j;
		Op_reg i_defR,j_useR;
		for (int i = 0 ; i < ins.size() ;i ++) {
			ins_i = ins.get(i);
			

			for (int j = i + 1 ; j < ins.size(); j++) {
				ins_j = ins.get(j);
				
				//flag for if ins_j is the next first output
				boolean output_first_output = true;
				
				boolean store_first_store = true;
				boolean store_first_load = true;
				boolean store_first_output = true;
				
				//dependency for value
				i_defR = ins_i.getDefReg();
				if (i_defR != null) {				
					j_useR = ins_j.getUseReg(0);
					if (j_useR != null && j_useR.equals(i_defR)) {
						createEdge(ins_j,ins_i);
					}
					j_useR = ins_j.getUseReg(1);
					if (j_useR != null && j_useR.equals(i_defR)) {
						createEdge(ins_j,ins_i);
					}
				}
				
				//dependency for load, store, output
				//for load and output, each store after need an edge
				if (ins_i.operation == Instruction.optype.load
						|| ins_i.operation == Instruction.optype.output) {
					if (ins_j.operation == Instruction.optype.store) {
						createEdge(ins_j,ins_i);
					}
				}
				
				//for output, next output need an edge
				if (output_first_output && ins_i.operation == Instruction.optype.output
						&& ins_j.operation == Instruction.optype.output) {
					output_first_output = false;
					createEdge(ins_j,ins_i);
				}
				
				//for store, next output, next store and next load need an edge
				if (ins_i.operation == Instruction.optype.store) {
					if (store_first_store 	&& ins_j.operation == Instruction.optype.store) {
						store_first_store = false;
						createEdge(ins_j,ins_i);
					}
					
					if (store_first_load 	&& ins_j.operation == Instruction.optype.load) {
						store_first_load = false;
						createEdge(ins_j,ins_i);
					}
					
					if (store_first_output 	&& ins_j.operation == Instruction.optype.output) {
						store_first_output = false;
						createEdge(ins_j,ins_i);
					}
					
				}
			}
			

			
			
		}
	}

	/**
	 * create an edge in the dependent graph
	 * child <- parent 
	 * means parent depends on child
	 * @param parent
	 * @param child
	 */
	private void createEdge(Instruction parent, Instruction child) {
		if (parent.children == null)
			parent.DGNode_init();
		if (child.parents == null)
			child.DGNode_init();
		parent.children.add(child);
		child.parents.add(parent);
	}

}
