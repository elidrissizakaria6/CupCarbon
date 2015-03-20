package route.ws;

import geometry.Point;

import java.util.List;

import route.ws.exception.RouteServiceException;

public interface IRouteService {
	
	public Route getRoute(Point source , Point destination) throws RouteServiceException;
	
	public Route getRoute(List<Point> points) throws RouteServiceException;

}
