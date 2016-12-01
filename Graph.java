import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {
	
	HashMap<String, Vertex> vMap;
	public static final double INFINITO = Double.MAX_VALUE;
	DB x;
	
	public Graph(DB db){
		vMap = new HashMap<>();
		x = db;
	}
	
	private Vertex getVertex(String name){
		if(vMap.containsKey(name)){
			return vMap.get(name);
		} 
		
		Vertex n = new Vertex(name);
		vMap.put(name, n);
		return n;
	}
	
	public int getWeight(String name1, String name2){
		return (int)getVertex(name1).getEdge(name2).cost;
	}
	
	public void addEdge(String origen, String destino){
		Vertex v = getVertex(origen);
		Vertex w = getVertex(destino);
		v.adj.add(new Edge(v,w));
		w.adj.add(new Edge(w,v));
		w.adj.add(new Edge(v,w));
		v.adj.add(new Edge(w,v));
	}
	
	public void resetAll(){
		for(Vertex v : vMap.values()){
			v.reset();
		}
	}
	
	public String BFS(String name){
		Queue<Vertex> x = new LinkedList<>();
		String s = "";
		Vertex u;
		
		x.add(getVertex(name));
		
		while(!x.isEmpty()){
			u = x.poll();
			u.m++;
			s += " ["+u.name+"] ";
			for(Edge e : u.adj){
				if(e.dst.m == 0){
					e.dst.m++;
					e.dst.dist+=e.cost+1;
					e.dst.prv = u;
					x.add(e.dst);
				}
			}
				
		}
		
		return s;
	}
	
	public String DFS(String name){
		
		resetAll();
		int time = 0;
		StringBuilder path = new StringBuilder();
		visit(time, this.getVertex(name), path);
		return path.toString();
	}
	
	public int visit(int time, Vertex u, StringBuilder path){
		time += 1;
		u.dist = time;
		u.m = 1;
		for(Edge e : u.adj){
			Vertex v = e.dst;
			if(v.m==0){
				v.prv = u;
				time = this.visit(time, v, path);
			}
		}
		u.m = 2;
		time = time+1;
		path.append(" ["+u.name+"] ");
		return time;
	}
	
	public class Vertex {
		
		String name;
		LinkedList<Edge> adj;
		int m;
		double dist;
		Vertex prv;
		
		public Vertex(String nm){
			name = nm;
			adj = new LinkedList<>();
		}  
		
		public Edge getEdge(String name){
			Edge ed=null;
			addEdge(this.name,name);
			Iterator<Edge> it = adj.iterator();

			while(it.hasNext()){
				ed = it.next();
				if(ed.dst.name == name){
					ed.setCost();
					return ed;
				}
			}
			return ed;
			
		}
		
		public void reset(){
			m=0;
			prv = null;
			dist = Graph.INFINITO;
		}
	}
	
	public class Edge {
		Vertex dst, or;
		double cost;
		
		public Edge(Vertex v, Vertex w){
			dst = v;
			or = w;
			setCost();
		}
		
		public void setCost(){
			cost = x.getHowSimilar(or.name, dst.name);
		}
	}	
}
