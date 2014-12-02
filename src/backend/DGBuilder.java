package backend;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import struct.DGEdge;
import struct.Instruction;
import struct.Op_reg;
import main.Scheduler;

public class DGBuilder {

	public Scheduler s;
	public Map<Integer,Integer> knownValue;
	
	public DGBuilder(Scheduler scheduler) {
		s = scheduler;
		knownValue = new HashMap<Integer, Integer>();
	}

	public void run() {
		buildDG();
	}

	private void buildDG() {
		List<Instruction> ins = s.insts;
		Instruction ins_i;
		Op_reg i_defR,i_useR;
		
		Map<Integer,Integer> reg_def_idx = new HashMap<Integer, Integer>();
		Instruction prev_store = null;
		Instruction prev_output = null;
		List<Instruction> prev_load_output = new ArrayList<Instruction>();
		List<Instruction> prev_stores = new ArrayList<Instruction>();
		
		s.ready = new LinkedList<Instruction>();
		
		for (int i = 0 ; i < ins.size() ;i ++) {
			ins_i = ins.get(i);
			
			//value dependency
			i_useR = ins_i.getUseReg(0);
			if (i_useR != null) {
				Integer def_idx = reg_def_idx.get(i_useR.vr);
				assert(def_idx != null);
				createDependentEdge(ins_i,ins.get(def_idx));
			}
			i_useR = ins_i.getUseReg(1);
			if (i_useR != null) {
				Integer def_idx = reg_def_idx.get(i_useR.vr);
				assert(def_idx != null);
				createDependentEdge(ins_i,ins.get(def_idx));
			}
			
			//load store output dependency
			if (ins_i.operation ==  Instruction.optype.load) {
				//need an edge to most recent store
				if (prev_store != null) {
					//createDependentEdge(ins_i,prev_store);
					
					for (int j = prev_stores.size() -1 ; j > -1  ;j--) {
						if (!Unrelated(ins_i.getUseReg(0).vr, prev_stores.get(j).getUseReg(1).vr)) {
							createDependentEdge(ins_i,prev_stores.get(j));
							break;
						}
					}
				}
				
			} else if (ins_i.operation ==  Instruction.optype.output) {
				//need an edge to most recent store
				if (prev_store != null) {
					//createDependentEdge(ins_i,prev_store);
					for (int j = prev_stores.size() -1 ; j > -1 ;j--) {
						if (!UnrelatedIns(ins_i.getIns().value, prev_stores.get(j).getUseReg(1).vr)) {
							createDependentEdge(ins_i,prev_stores.get(j));
							break;
						}
					}
				}
				//need an edge to most recent output
				if (prev_output != null) {
					createSerializeEdge(ins_i,prev_output);
				}
			} else if (ins_i.operation ==  Instruction.optype.store) {
				//need an edge to most recent store
				if (prev_store != null) {
					createSerializeEdge(ins_i,prev_store);
				}
				for (Instruction load_or_output : prev_load_output) {
					createSerializeEdge(ins_i,load_or_output);
				}
			}
			
			updateKnownValue(ins_i);
			
			//initialize ready_count
			if (ins_i.children != null)
				ins_i.ready_count = ins_i.children.size();
			else
				ins_i.ready_count = 0;
			
			if (ins_i.ready_count == 0) {
				s.ready.add(ins_i);
			}
			
			//Update reg_def_idx
			i_defR = ins_i.getDefReg();
			if (i_defR != null) {
				reg_def_idx.put(i_defR.vr, i);
			}
			
			//Update memory accessing
			if (ins_i.operation ==  Instruction.optype.load) {
				prev_load_output.add(ins_i);
				
			} else if (ins_i.operation ==  Instruction.optype.output) {
				prev_load_output.add(ins_i);
				prev_output = ins_i;
				
			} else if (ins_i.operation ==  Instruction.optype.store) {
				prev_store = ins_i;
				prev_stores.add(ins_i);
			}
			
		}
		 
	}

	private boolean UnrelatedIns(int value, int vr) {
		Integer value2 = knownValue.get(vr);
		if (value2 != null && value != value2.intValue())
			return true;
		return false;
	}

	private boolean Unrelated(int vr, int vr2) {
		Integer value = knownValue.get(vr);
		Integer value2 = knownValue.get(vr2);
		if (value != null && value2 != null && !value.equals(value2))
			return true;
		return false;
	}

	private void updateKnownValue(Instruction ins_i) {
		if (ins_i.operation == Instruction.optype.loadI) {
			knownValue.put(new Integer(ins_i.getDefReg().vr), new Integer(ins_i.getIns().value));
		} else {	//load is unpredictable
			if (ins_i.getDefReg() == null)
				return;
			knownValue.remove(ins_i.getDefReg().vr);
		}
	}

	
	/**
	 * create an edge in the dependent graph
	 * child <- parent 
	 * means parent depends on child
	 * @param parent
	 * @param child
	 */
	private void createEdge(Instruction parent, Instruction child, int len) {
		if (parent.children == null)
			parent.DGNode_init();
		if (child.parents == null)
			child.DGNode_init();
		parent.children.add(new DGEdge(child,len));
		child.parents.add(new DGEdge(parent,len));
	}

	private void createDependentEdge(Instruction parent, Instruction child) {
		createEdge(parent,child,child.latency());
	}
	
	private void createSerializeEdge(Instruction parent, Instruction child) {
		createEdge(parent,child,1);
	}
	
}
