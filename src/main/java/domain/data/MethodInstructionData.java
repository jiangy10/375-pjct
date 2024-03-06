package domain.data;

public class MethodInstructionData {
	private String name;
	private String owner;
	
	public MethodInstructionData( String methodCall,String owner) {
		this.name = methodCall;
		this.owner = owner;
	}

	public String getName(){
		return this.name;
	}
	public String getOwner(){
		return this.owner;
	}
	
}
