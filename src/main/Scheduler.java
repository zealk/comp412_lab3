package main;

import java.util.List;

import backend.Allocator;
import backend.DGBuilder;
import backend.Sched;
import struct.DGEdge;
import struct.Instruction;
import struct.Op_reg;
import frontend.Scanner;

public class Scheduler {

	private Scanner scanner;
	private Allocator allocator;
	private DGBuilder dgbuilder;
	private Sched sched;
	public List<Instruction> insts;
	public int max_sr;
	public int max_living_vr;
	public List<Instruction> ready;
	
	public String fn;
	
	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.init(args);
	}
	
	private void help() {
		System.out.println("Arguments:");
		System.out.println("-h : help");
		System.out.println("<filename> : input file name");
	}
	
	private void init(String[] args) {
		
		if (args.length != 1) {
			help();
			return;
		}
		
		for (String arg:args) {
			if (arg.equals("-h")) {
				help();
				return;
			}
		}
		String filename = "default";
		filename = args[0];
		
		fn = filename;
		
		scanner = new Scanner(this,filename);
		scanner.run();

		allocator = new Allocator(this);
		allocator.run();
		//printR(Op_reg.TOSTRING_VR);
	
		dgbuilder = new DGBuilder(this);
		dgbuilder.run();
		//printGraphvic();
		
		sched = new Sched(this);
		sched.run();
	}
	
	public void printR(int reg_type) {
		for (int i = 0 ; i < insts.size(); i++) {
			System.out.println(insts.get(i).toRString(reg_type));
		}
	}
	
	public void printGraphvic() {
		System.out.println("digraph test");
		System.out.println("{");
		for (int i = 0 ; i < insts.size() ; i ++) {
			System.out.print("\t" + (i+1) + " [label = \"" + (i+1) + "\\n ");
			System.out.print(insts.get(i).toRString(Op_reg.TOSTRING_VR));
			System.out.println("\"]");
		}
		for (int i = 0 ; i < insts.size() ; i ++) {
			if (insts.get(i).parents != null) {
				for (DGEdge ins_j_edge : insts.get(i).parents) {			
					int j = insts.indexOf(ins_j_edge.node);
					System.out.println("\t" + (j+1) + " -> " + (i+1));
				}
			}
		}
		System.out.println("}");
	}
	
}
