package test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
///*************************************************************************
// *  Compilation:  javac Maze.java
// *  Execution:    java Maze.java N
// *  Dependecies:  StdDraw.java
// *
// *  Generates a perfect N-by-N maze using depth-first search with a stack.
// *
// *  % java Maze 62
// *
// *  % java Maze 61
// *
// *  Note: this program generalizes nicely to finding a random tree
// *        in a graph.
// *
// *************************************************************************/

public class Maze {
    private int N;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    private boolean done = false;
    
    public State start;
    public State end;

    public Maze(int N) {
        this.N = N;
        StdDraw.setXscale(0, N+2);
        StdDraw.setYscale(0, N+2);
        init();
        generate();
    }

    private void init() {
        // initialize border cells as xxxxxxxxxxxalready visited
        visited = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++) visited[x][0] = visited[x][N+1] = true;
        for (int y = 0; y < N+2; y++) visited[0][y] = visited[N+1][y] = true;


        // initialze all walls as present
        north = new boolean[N+2][N+2];
        east  = new boolean[N+2][N+2];
        south = new boolean[N+2][N+2];
        west  = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++)
            for (int y = 0; y < N+2; y++)
                north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
    }


    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = Math.random();
                if (r < 0.25 && !visited[x][y+1]) {
                    north[x][y] = south[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r >= 0.25 && r < 0.50 && !visited[x+1][y]) {
                    east[x][y] = west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r >= 0.5 && r < 0.75 && !visited[x][y-1]) {
                    south[x][y] = north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r >= 0.75 && r < 1.00 && !visited[x-1][y]) {
                    west[x][y] = east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);

/*
        // delete some random walls
        for (int i = 0; i < N; i++) {
            int x = (int) (1 + Math.random() * (N-1));
            int y = (int) (1 + Math.random() * (N-1));
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = (int) (N / 2 + Math.random() * (N / 2));
            int y = (int) (N / 2 + Math.random() * (N / 2));
            east[x][y] = west[x+1][y] = true;
        }
*/
     
    }



    // solve the maze using depth-first search
    private void solve(int x, int y) {
////        if (x == 0 || y == 0 || x == N+1 || y == N+1) return;
//        if (done || visited[x][y]) return;
//        visited[x][y] = true;
//
//        StdDraw.setPenColor(StdDraw.BLUE);
//        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
//        StdDraw.show(30); 
//
////         reached middle (destination)
//        if (x == N/2 && y == N/2) done = true;
//
//        if (!north[x][y]) solve(x, y + 1);
//        if (!east[x][y])  solve(x + 1, y);
//        if (!south[x][y]) solve(x, y - 1);
//        if (!west[x][y])  solve(x - 1, y);
//
//        if (done) return;
//        
//    	
//
//        StdDraw.setPenColor(StdDraw.GRAY);
//        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
//        StdDraw.show(30);
    	
//    	
    	State current = new State(x, y);
    	State goal = new State(N/2, N/2);
    	System.out.println("start searching");
    	Map<String,String> result = aStar(current, goal);
    	Deque<State> list = new ArrayDeque<State>();
    	State current_traversal = goal;
    	while(current_traversal.agent_x != current.agent_x || current_traversal.agent_y != current.agent_y){
    		list.push(current_traversal);
    		String current_traversal_text = result.get(current_traversal.toString());
    		String[] kaka = current_traversal_text.split(" ");
    		int meo = Integer.parseInt(kaka[0]);
    		int cho = Integer.parseInt(kaka[1]);
    		current_traversal = new State(meo, cho);
    	}
//    	
    	while(!list.isEmpty()){
    		State element = list.pop();
          StdDraw.setPenColor(StdDraw.BLUE);
          StdDraw.filledCircle(element.agent_x + 0.5, element.agent_y + 0.5, 0.25);
          StdDraw.show(30);    		
    	}
    	
    }

    // solve the maze starting from the start state
    public void solve() {
        for (int x = 1; x <= N; x++)
            for (int y = 1; y <= N; y++)
                visited[x][y] = false;
        done = false;
        solve(1, 1);
    }

    // draw the maze
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(N/2.0 + 0.5, N/2.0 + 0.5, 0.375); // draw exit
        StdDraw.filledCircle(1.5, 1.5, 0.375);				   // draw entrance

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= N; x++) {
            for (int y = 1; y <= N; y++) {
                if (south[x][y]) StdDraw.line(x, y, x + 1, y);
                if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
                if (west[x][y])  StdDraw.line(x, y, x, y + 1);
                if (east[x][y])  StdDraw.line(x + 1, y, x + 1, y + 1);
            }
        }
        
        StdDraw.show(1000);
    }
    
    //below this line is Adick
    
    double distBetween (State currentState, State neighbor){
		return Math.abs(currentState.agent_x - neighbor.agent_x)
				+ Math.abs(currentState.agent_y - neighbor.agent_y);
	}
	
    double heuristicEst(State current){  //  m,n are the monster's coordinate
		State goal = new State(N/2, N/2);
		return distBetween(current, goal);
	}
	
	List<State> neighborStates(State current){
		List<State> list_states = new ArrayList<State>();
		int x = current.agent_x, y = current.agent_y;
		if (!north[x][y]) list_states.add(new State(x, y + 1));
        if (!east[x][y])  list_states.add(new State(x + 1, y));
        if (!south[x][y]) list_states.add(new State(x, y - 1));
        if (!west[x][y])  list_states.add(new State(x - 1, y));
		return list_states;
	}
	
	State getLowest(Set<String> openSet, Map<String,Double> fScore){
		double lowest = Double.POSITIVE_INFINITY;
		String lowestState = "";
		for (String s : openSet){
			if (fScore.get(s) < lowest){
				lowest = fScore.get(s);
				lowestState = s;
			}
		}
		String[] result = lowestState.split(" ");
		int x = Integer.parseInt(result[0]);
		int y = Integer.parseInt(result[1]);
		return new State(x, y);
	}
	
	Map<String,String> aStar(State start, State goal){
		Map<String,String> cameFrom = new HashMap<String, String>();
		Set<String> openSet = new HashSet<String>();
		openSet.add(start.toString());
		Set<String> closedSet = new HashSet<String>();
		Map<String,Double> gScore = new HashMap<String, Double>();
		Map<String,Double> fScore = new HashMap<String, Double>();
		gScore.put(start.toString(), 0.0);
		fScore.put(start.toString(), gScore.get(start.toString()) + heuristicEst(start));
		
		System.out.println("begining");
		
		while (openSet.size() != 0){
			State current = getLowest(openSet, fScore);
			
//			System.out.println("[LOOP]");
			
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledCircle(current.agent_x + 0.5, current.agent_y + 0.5, 0.25);
	        StdDraw.show(30);
			
			if (current.agent_x == goal.agent_x && current.agent_y == goal.agent_y){
				break;
			}
			
			openSet.remove(current.toString());
			closedSet.add(current.toString());
			
//			System.out.println(openSet);
//			System.out.println(closedSet);
			
			for (State neighbor :  neighborStates(current)){
				if (closedSet.contains(neighbor.toString()) || openSet.contains(neighbor.toString()))
					continue;
				Double tentative_gScore = gScore.get(current.toString()) + distBetween(current, neighbor);
				if (closedSet.contains(neighbor.toString()) && tentative_gScore < gScore.get(neighbor.toString()))
					continue;
				if (!closedSet.contains(neighbor.toString())
						|| tentative_gScore < gScore.get(neighbor)){
					cameFrom.put(neighbor.toString(), current.toString());
					gScore.put(neighbor.toString(), tentative_gScore);
					fScore.put(neighbor.toString(), gScore.get(neighbor.toString()) + heuristicEst(current));
					if (!openSet.contains(neighbor.toString())){
						openSet.add(neighbor.toString());
					}
				}
			}
		}
		return cameFrom;
		
	}



    // a test client
    public static void main(String[] args) {
        int N = 20;// should be  > 10
        Maze maze = new Maze(N);
        StdDraw.show(0);
        maze.draw();
        maze.solve();
//        maze.aStar(new State(1, 1), new State(N/2, N/2));
    }

}