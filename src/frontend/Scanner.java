package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import struct.Instruction;

public class Scanner {
	
	public main.Scheduler s;
	public String filename;
	
	public Scanner(main.Scheduler scheduler, String filename) {
		s = scheduler;
		this.filename = filename;
	}

	/**
	 * Try to parse a line
	 * @param line
	 * @param empty instruction structure
	 * @param line count
	 * @return
	 */
	public boolean tryToParse(String line, Instruction tmpins, int line_count) {
		String orig_line = line;
		//handle comments
		int comment_start = line.indexOf("//");
		if (comment_start > -1) {
			line = line.substring(0, comment_start);
		}
		line = line.trim();
		String line_without_comment = line;
		int i = 0;
		for ( ; i < Instruction.INSTRUCTION.length ; i++) {
		    Pattern p = Pattern.compile(Instruction.INSTRUCTION[i]);
		    Matcher m = p.matcher(line);
		    if (m.find()) {
		    	int [] oprds = new int[3];
		    	//TODO
		    	for (int j = 1 ; j <= m.groupCount() ; j++) {
		    		oprds[j-1] = Integer.parseInt(m.group(j));
		    		
		    		if (oprds[j-1] > s.max_sr) {	//save max sr
		    			s.max_sr = oprds[j-1];
		    		}
		    		
		    	}
		    	tmpins.init(Instruction.getType(i), oprds);
				//System.out.println(tmpins.toString());
		    	return true;
		    }
		}
		if (!line_without_comment.equals("")) {
			System.err.println("Line "+ line_count + " can not match : \"" + orig_line +"\"");
		}
		return false;
	}
	
	public void run() {
		s.insts = new ArrayList<Instruction>();
		Instruction tmpins = new Instruction();
		String line;
		try {
			File file = new File(filename);
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			int line_count = 0;
			while ((line = br.readLine()) != null) {
				line_count ++;
				if (!tryToParse(line,tmpins,line_count)) {
					//Failed (empty line or comment also comes here)
				} else {
					//Success, put line into list
					s.insts.add(tmpins);
					tmpins = new Instruction();
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

}
