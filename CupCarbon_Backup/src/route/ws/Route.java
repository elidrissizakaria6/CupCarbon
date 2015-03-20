package route.ws;

import geometry.Point;

import java.util.List;

import util.CollectionsUtils;

public class Route {
	
	private List<Point> nodes ;

	public Route(List<Point> nodes) {
		super();
		this.nodes = nodes;
	}

	public List<Point> getNodes() {
		return nodes;
	}
	
	public boolean isEmpty(){
		return CollectionsUtils.isEmpty(nodes);
	}
	
	public boolean isNotEmpty(){
		return !isEmpty();
	}

	@Override
	public String toString() {
		return "Route [nodes=" + nodes + "]";
	}

}
