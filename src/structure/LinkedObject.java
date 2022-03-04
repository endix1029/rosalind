package structure;

public class LinkedObject {
	public Object cargo = null;
	public LinkedObject next = null;
	public LinkedObject prev = null;
	
	public LinkedObject(Object cargo) {
		this.cargo = cargo;
	}
	
	public void load(Object cargo) {
		this.cargo = cargo;
	}
	
	public void append(LinkedObject obj) {
		this.next = obj;
		obj.prev = this;
	}
	
	public void attach(LinkedObject lnk) {
		lnk.next = this;
		this.prev = lnk;
	}
	
	public void detach() {
		this.prev.next = this.next;
		this.next.prev = this.prev;
		this.next = null;
		this.prev = null;
	}
}
