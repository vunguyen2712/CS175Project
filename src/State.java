public class State {
	public int agent_x;
	public int agent_y;
//	public int monster_x;
//	public int monster_y;
//	
	public State(){
		agent_x = -1;
		agent_y = -1;
//		monster_x = -1;
//		monster_y = -1;
	}

	public State(int p_x, int p_y){
		agent_x = p_x;
		agent_y = p_y;
		
//		monster_x = -1;
//		monster_y = -1;
	}
	
	@Override
	public String toString(){
		return agent_x + " " + agent_y;
	}
	
}
