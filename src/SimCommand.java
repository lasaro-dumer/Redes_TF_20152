public enum SimCommand{
	Ping("Ping"),Trace("Trace");
	private final String tipo;
	SimCommand(String tipo){
		this.tipo = tipo;
	}

	public String getTipo(){
		return tipo;
	}
}
