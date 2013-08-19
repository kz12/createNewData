

public abstract class BasicNode {

	// The minimum zoom degree that this element could be drawn
	protected String name1;
	protected String name2;
	protected String cname;

	// protected Float lengthbr;
	protected float lengthbr = 0f;
	protected BasicNode child1;
	protected BasicNode child2;

	protected String popstab;
	protected String redlist;

	protected int richness_val;

	protected String cutname;
	protected String newName;
	
	public static String newDataString;
	

	protected BasicNode() {
	}

	protected void initialLeafNode(String data) {
		this.child1 = null;
		this.child2 = null;
		this.richness_val = 0;
		setLengthbr(data);

		if (data.length() > 0) {
			setLeafNames(data);
		} else {
			this.name1 = null;
			this.name2 = null;
		}
	}

	protected void setLeafNames(String data) {
		int lengthcut = data.indexOf('{');
		if (lengthcut != -1) {
			this.cname = data.substring(lengthcut + 1, data.length() - 1);
			data = data.substring(0, lengthcut);

			lengthcut = cname.indexOf('_');
			// Log.d("debug", "redlist: " + this.cname);

			if (lengthcut == -1) {
				this.popstab = "U";
				this.redlist = "NE";
			} else {
				redlist = cname.substring(lengthcut + 1, lengthcut + 3);
				popstab = cname.substring(lengthcut + 4, lengthcut + 5);

				this.cname = this.cname.substring(0, lengthcut);
			}
		}

		lengthcut = data.indexOf('_');

		if (lengthcut == -1) {
			this.name1 = data;
			this.name2 = null;
		} else {
			this.name1 = data.substring(lengthcut + 1, data.length());
			this.name2 = data.substring(0, lengthcut);
		}
	}

	protected void initialNonLeafNode(String cutname) {
		cutname = setLengthbr(cutname); // at this stage cutname no longer has
										// length data associated with it.
		this.richness_val = 0;
		if (cutname.length() > 0 && !isFloat(cutname)) {
			setNames(cutname);
		} else {
			this.name1 = null;
			this.name2 = null;
			this.cname = null;
		}
	}

	protected boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	protected void setNames(String cutname) {
		int lengthcut = cutname.indexOf('{');
		if (lengthcut != -1) {
			setNameInDetail(cutname, lengthcut);
		} else {
			this.cname = null;
			this.name1 = cutname;
			this.name2 = null;
		}
	}

	protected void setNameInDetail(String cutname, int lengthcut) {
		this.cname = cutname.substring(lengthcut + 1, cutname.length() - 1);
		// TODO: why test if lengthcut equals 1 rather than equals 0
		if (lengthcut != 1) {
			this.name1 = cutname.substring(0, lengthcut);
		} else {
			this.name1 = null;
		}
		// now we need to split [] out of cname and replace * with ,
		lengthcut = cname.indexOf('[');
		if (lengthcut == -1) {
			this.name2 = null;
		} else {
			this.name2 = (this.cname).substring(lengthcut + 1,
					(this.cname).length() - 1);
			this.cname = (this.cname).substring(0, lengthcut);
		}
		this.cname = this.cname.replace('*', ',');
	}

	protected String[] cutDataToThreeParts(String data) {
		int bracketscount = 0, cut = 0, end = 0;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == '(') {
				bracketscount++;
			}
			if (data.charAt(i) == ')') {
				bracketscount--;
			}
			if (data.charAt(i) == ',') {
				if (bracketscount == 1) {
					cut = i;
				}
			}
			if (bracketscount == 0) {
				end = i;
				i = data.length() + 1;
			}
		} // for loop: i loops from 0 to. Find the comma inside the outer
			// brackets

		String cut1 = new String(data.substring(1, cut));
		String cut2 = new String(data.substring(cut + 1, end));
		String cutname = new String(data.substring(end + 1, data.length()));
		return new String[] { cut1, cut2, cutname };
	}

	protected String removeTailComma(String data) {
		if (data.charAt(data.length() - 1) == ';') {
			data = data.substring(0, data.length() - 1);
		}
		return data;
	}

	protected String setLengthbr(String cutname) {
		int lengthcut;
		lengthcut = cutname.indexOf(':');
		if (lengthcut == -1) {
			this.lengthbr = 0f;
			return cutname;
		} else {
			this.lengthbr = Float.parseFloat(cutname.substring(lengthcut + 1,
					cutname.length()));
			return cutname.substring(0, lengthcut);
		}
	}

	public String toString() {
		if( child1!= null)
			return Float.toString(lengthbr) + ", " + child1.toString() + ", " + child2.toString();
//			return Integer.toString(richness_val) + "\n" + child1.toString() + "\n" + child2.toString();
		else {
//			return Integer.toString(richness_val);
			return Float.toString(lengthbr);
		}
	}

	protected void init() {
		// TODO Auto-generated method stub
		richness_calc();
		age_calc();
	}
	
	protected int richness_calc() {
		if (this.child1 != null) {
			this.richness_val = (((this.child1).richness_calc()) + ((this.child2)
					.richness_calc()));
		} else {
			if (this.richness_val <= 0) {
				this.richness_val = 1;
			}
		}
		return (this.richness_val);

	}

	protected String name_calc() {
		if (this.child1 != null) {
			if (((this.child1).name_calc()) == ((this.child2).name_calc())) {
				this.name2 = ((this.child1).name2);
			}
		}
		return (this.name2);
	}


	protected float age_calc() {
		float length_temp;
		length_temp = lengthbr;
		if (this.child1 != null) {
			(this.lengthbr) = (this.child2).age_calc();
			(this.lengthbr) = (this.child1).age_calc();
			return ((length_temp) + (this.lengthbr));
		} else {
			(this.lengthbr) = 0f;
			return (length_temp);
		}
	}
	
	public void generateNewData(){
		int lengthcut;
		lengthcut = cutname.indexOf(':');
		if (lengthcut == -1) {
			newName = new String(cutname + "[" + richness_val + "_" 
			+ lengthbr + "]");
		} else {
//			this.lengthbr = Float.parseFloat(cutname.substring(lengthcut + 1,
//					cutname.length()));
//			return cutname.substring(0, lengthcut);
			newName =  new String(cutname.substring(0, lengthcut) + "[" 
					+ richness_val + "_" + lengthbr + "]");
		}
		if( child1 != null){
			child1.generateNewData();
			child2.generateNewData();
		}
	}

	public String storeNewData(){
		if( child1 != null ){
			return "(" + child1.storeNewData() + "," + 
					child2.storeNewData() + ")" + newName;
		}
		else {
			return newName;
		}
	}
}

class CircleNode extends BasicNode {

	public CircleNode(){
		
	}

	public CircleNode(String data) {
		super();
		data = removeTailComma(data);

		String cut1 = null;
		String cut2 = null;
		String[] cutnames;

		cutnames = cutDataToThreeParts(data);
		cut1 = cutnames[0];
		cut2 = cutnames[1];
		cutname = new String(cutnames[2]);

		initialNonLeafNode(cutname);

		if (cut1.charAt(0) == '(') {
			child1 = new CircleNode(cut1);
		} else {
			child1 = new LeafNode(cut1);
		}

		if (cut2.charAt(0) == '(') {
			child2 = new CircleNode(cut2);
		} else {
			child2 = new LeafNode(cut2);
		}
	}
}

class LeafNode extends BasicNode {
	public LeafNode(String data) {
		super();
		cutname = new String(data);
		initialLeafNode(data);
	}

	public LeafNode() {
	}
}