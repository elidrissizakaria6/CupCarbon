package route.ws;

import route.ws.impl.OSRMRouteService;

public class RouteServiceFactory {
	
	
	public static IRouteService getNewServiceInstance(){
		return new OSRMRouteService();
	}

}
