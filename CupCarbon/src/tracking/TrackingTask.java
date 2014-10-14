package tracking;

import geometry.Point;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import map.Layer;
import route.ws.IRouteService;
import route.ws.Route;
import route.ws.RouteServiceFactory;
import route.ws.exception.RouteServiceException;
import tracking.ws.TargetWS;
import tracking.ws.TrackerWS;
import utilities.MapCalc;
import device.Device;
import device.Marker;
import device.MobileWithRadio;
import device.Sensor;

public class TrackingTask implements Runnable {

	private List<TrackingMarker> trackingMarkers;
	private boolean drawLinks = true;
	private boolean drawArrows = false;
	private boolean drawMarkers = false;

	private String trackerIdName = "";
	private String targetIdName = "";

	private Thread thread;

	// real mode parameters
	private boolean realTrackingMode = false;
	private Point realTrackerLocation = null;
	private Point realTargetLocation = null;
	private Sensor realTracker = null;
	private MobileWithRadio realTarget;

	private static boolean underSimulation = false;

	public TrackingTask(String trackerId, String targetId, boolean realTrackingMode) {
		trackingMarkers = new ArrayList<TrackingMarker>();
		this.targetIdName = targetId;
		this.trackerIdName = trackerId;
		this.realTrackingMode = realTrackingMode;
		if (realTrackingMode) {
			initRealTrackingMode();
		}
	}

	private void initRealTrackingMode() {
		realTracker = new Sensor(0, 0, 10, 10);
		realTarget = new MobileWithRadio(0, 0, 10, 10);
		// TODO realTracker.setNodeIdName() ou equivalent
	}

	public void draw(Graphics g) {
		//if (CollectionsUtils.isNotEmpty(trackingMarkers) && underSimulation) {
		if (!trackingMarkers.isEmpty() && underSimulation) {	
			try {
				double x1 = 0;
				double y1 = 0;
				double x2 = 0;
				double y2 = 0;
				double dx = 0;
				double dy = 0;
				double alpha = 0;
				int lx1 = 0;
				int ly1 = 0;
				int lx2 = 0;
				int ly2 = 0;
				int[] coord;
				if (realTrackingMode) {
					drawTargetAndTracker(g);
				}
				if (drawMarkers) {
					for (TrackingMarker marker : trackingMarkers) {
						marker.draw(g);
					}
				}
				if (drawLinks && trackingMarkers.size() > 0) {
					boolean firstTime = true;
					for (Marker marker : trackingMarkers) {
						if (firstTime) {
							firstTime = false;
							x1 = marker.getX();
							y1 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x1, y1);
							lx1 = coord[0];
							ly1 = coord[1];
							// lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
							// ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
							g.setColor(Color.blue);
							g.drawOval((int) lx1 - 5, (int) ly1 - 5, (int) 10,
									(int) 10);
						} else {
							x2 = marker.getX();
							y2 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x2, y2);
							lx2 = coord[0];
							ly2 = coord[1];
							// lx2 = MapCalc.geoToIntPixelMapX(x2, y2);
							// ly2 = MapCalc.geoToIntPixelMapY(x2, y2);

							g.setColor(Color.blue);

							// Draw the link between markers
							g.drawLine((int) lx1, (int) ly1, (int) lx2,
									(int) ly2);
							// Draw arrows
							if (drawArrows) {
								dx = lx2 - lx1;
								dy = ly2 - ly1;
								alpha = Math.atan(dy / dx);
								alpha = 180 * alpha / Math.PI;
								if ((dx >= 0 && dy >= 0)
										|| (dx >= 0 && dy <= 0))
									g.fillArc((int) lx2 - 15, (int) ly2 - 15,
											30, 30, 180 - (int) alpha - 10, 20);
								else
									g.fillArc((int) lx2 - 15, (int) ly2 - 15,
											30, 30, -(int) alpha - 10, 20);
							}
							x1 = marker.getX();
							y1 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x1, y1);
							lx1 = coord[0];
							ly1 = coord[1];
							// lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
							// ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void drawTargetAndTracker(Graphics g) {
		realTracker.setX(realTrackerLocation.getX());
		realTracker.setY(realTrackerLocation.getY());
		realTracker.draw(g);

		realTarget.setX(realTargetLocation.getX());
		realTarget.setY(realTargetLocation.getY());
		realTarget.draw(g);
	}

	public void start() {
		underSimulation = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (underSimulation) {
			try {
				Thread.sleep(Device.moveSpeed / 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			recalculateRoute();
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		underSimulation = false;
		if (thread != null) {
			thread.stop();
		}

	}

	public void deleteAll() {
		TrackingMarker point;
		for (Iterator<TrackingMarker> iterator = trackingMarkers.iterator(); iterator.hasNext();) {
			point = iterator.next();
			Layer.getMapViewer().removeMouseListener(point);
			Layer.getMapViewer().removeMouseMotionListener(point);
			Layer.getMapViewer().removeKeyListener(point);
			iterator.remove();
			point = null;
		}
	}

	private void prepareRouteForDraw(Route route) {
		deleteAll();
		for (Point node : route.getNodes()) {
			trackingMarkers.add(new TrackingMarker(node.getX(), node.getY(), 5));
			Layer.getMapViewer().repaint();
		}
	}

	public void recalculateRoute() {
		final Point point1 = TrackerWS.getCoords(trackerIdName);
		final Point point2 = TargetWS.getCoords(targetIdName);
		if (point1 != null && point2 != null) {
			IRouteService routeService = RouteServiceFactory.getNewServiceInstance();
			try {
				final Route route = routeService.getRoute(point1, point2);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (route != null && route.isNotEmpty()) {
							prepareRouteForDraw(route);
						}
						if (realTrackingMode) {
							realTrackerLocation = point1;
							realTargetLocation = point2;
						}
					}
				});
			} catch (RouteServiceException e) {
				e.printStackTrace();
			}
		}
	}
	
}
