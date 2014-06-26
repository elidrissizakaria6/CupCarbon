package route.ws.impl;

import geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import route.ws.IRouteService;
import route.ws.Route;
import route.ws.exception.RouteServiceException;
import util.CollectionsUtils;
import util.HttpUtils;

public class OSRMRouteService implements IRouteService {

	private static final String OSRM_ROUTE_SERVICE_URL = "http://router.project-osrm.org/viaroute?hl=fr";
	private static final String ERROR_MESSAGE = "une erreur est survenue lors de l'appel du service des routes";
	private static final String USER_AGENT = "mozilla";
	private static final int CONNECTION_TIMEOUT = 1000;
	private static final int SOCKET_TIMEOUT = 2000;
	private HttpClient httpClient ;
	
	public OSRMRouteService(){
		configureHttpClient();
	}

	private void configureHttpClient() {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
		requestBuilder = requestBuilder.setConnectionRequestTimeout(SOCKET_TIMEOUT);

		HttpClientBuilder builder = HttpClientBuilder.create();     
		builder.setDefaultRequestConfig(requestBuilder.build());
		httpClient = builder.build();
	}

	@Override
	public Route getRoute(Point source, Point destination)
			throws RouteServiceException {
		if (source == null || destination == null) {
			return null;
		}
		try {
			List<Point> points = createPointsListFrom2Points(source, destination);
			return getRoute(points);
		} catch (Exception ex) {
			throw new RouteServiceException(ERROR_MESSAGE, ex);
		}
	}

	private List<Point> createPointsListFrom2Points(Point source, Point destination) {
		List<Point> points = new ArrayList<Point>();
		points.add(source);
		points.add(destination);
		return points;
	}

	@Override
	public Route getRoute(List<Point> points) throws RouteServiceException {
		if (CollectionsUtils.isEmpty(points)) {
			return null;
		}
		try {
			HttpResponse response = tryExecuteRequest(points);
			String responseBody = HttpUtils.getResponseBody(response);
			return new Route(parseResponse(responseBody));
		} catch (Exception ex) {
			throw new RouteServiceException(ERROR_MESSAGE, ex);
		}
	}

	private HttpResponse tryExecuteRequest(List<Point> points)
			throws ClientProtocolException, IOException, RouteServiceException {
		HttpResponse response = doExecuteRequest(points);
		if (!HttpUtils.isOKResponse(response)) {
			throw new RouteServiceException(ERROR_MESSAGE);
		}
		return response;
	}


	public HttpResponse doExecuteRequest(List<Point> points)
			throws ClientProtocolException, IOException {
		String locParameter = createLocParameter(points);
		String url = OSRM_ROUTE_SERVICE_URL
				+ locParameter + "&output=gpx";
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		return httpClient.execute(request);
	}

	private String createLocParameter(List<Point> points) {
		String locParameter = "";
		for (Point point : points) {
			locParameter += "&loc=" + point.getX() + "," + point.getY();
		}
		return locParameter;
	}
	
	private List<Point> parseResponse(String response) {
		final String[] s1 = response.split("lat=\"");
		List<Point> routePoints = new ArrayList<Point>();
		for (int i = 1; i < s1.length; i++) {
			String[] s2 = s1[i].split("\"");
			routePoints.add(new Point(Double.valueOf(s2[0]), Double
					.valueOf(s2[2])));
		}
		return routePoints;
	}

	public static void main(String[] args) {
		OSRMRouteService service = new OSRMRouteService();
		Point source = new Point(48, -4);
		Point destination = new Point(49, -4);
		try {
			Route route = service.getRoute(source, destination);
			System.out.println(route);
		} catch (RouteServiceException e) {
			e.printStackTrace();
		}
		List<Point> points = new ArrayList<Point>();
		points.add(source);
		points.add(destination);
		points.add(new Point(50,-3));
		try {
			Route route = service.getRoute(points);
			System.out.println(route);
		} catch (RouteServiceException e) {
			e.printStackTrace();
		}
	}

}
