public enum EntryType{
	Node("#NODE"),Router("#ROUTER"),RouterTable("#ROUTERTABLE");
	private final String tipo;
	EntryType(String tipo){
		this.tipo = tipo;
	}

	public String getTipo(){
		return tipo;
	}
}
