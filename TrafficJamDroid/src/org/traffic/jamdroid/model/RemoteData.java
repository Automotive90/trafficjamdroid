/*
 * Copyright (c) 2011, Daniel Kuenne
 * 
 * This file is part of TrafficJamDroid.
 *
 * TrafficJamDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrafficJamDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TrafficJamDroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.traffic.jamdroid.model;

import java.util.LinkedList;
import java.util.List;

import org.osmdroid.views.overlay.Overlay;
import org.traffic.jamdroid.views.overlays.LocationOverlay;
import org.traffic.jamdroid.views.overlays.RoadOverlay;

/**
 * Class to store all information send by the server.
 * 
 * @author Daniel Kuenne
 * @version $LastChangedRevision: 225 $
 */
public class RemoteData {

	/** The one and only instance */
	private static RemoteData instance = new RemoteData();

	/** A flag that indicates whether congestions exist or not */
	private boolean hasCongestions;

	/** A list of all overlays generated by the server data */
	private final List<Overlay> overlays;
	/** A list of all overlays used for the basic system */
	private final List<Overlay> systemOverlays;

	/**
	 * Default constructor. (For invocation by <code>getInstance</code> in this
	 * singleton.)
	 */
	private RemoteData() {
		hasCongestions = false;
		overlays = new LinkedList<Overlay>();
		systemOverlays = new LinkedList<Overlay>();
	}

	/**
	 * Checks whether congestions are on the map.
	 * 
	 * @return The status
	 */
	public boolean hasCongestions() {
		return hasCongestions;
	}

	/**
	 * Sets the status of the congestions.
	 * 
	 * @param hasCongestions
	 *            The new status
	 */
	public void setHasCongestions(boolean hasCongestions) {
		this.hasCongestions = hasCongestions;
	}

	/**
	 * Deletes the route.
	 */
	public void deleteRouteOverlay() {
		Overlay kill = null;
		for (Overlay over : systemOverlays) {
			if (over instanceof RoadOverlay)
				kill = over;
		}
		if (kill != null) {
			systemOverlays.remove(kill);
		}
	}

	/**
	 * Adds a new overlay for the map.
	 * 
	 * @param overlay
	 *            The new overlay
	 */
	public void addOverlay(final Overlay overlay) {
		addOverlay(overlay, false);
	}

	/**
	 * Adds a new overlay with a specified status.
	 * 
	 * @param overlay
	 *            The new overlay
	 * @param isSystem
	 *            The status
	 */
	public void addOverlay(final Overlay overlay, final boolean isSystem) {
		if (overlay instanceof RoadOverlay && isSystem) {
			for (int i = 0; i < systemOverlays.size(); i++) {
				if (systemOverlays.get(i) instanceof RoadOverlay) {
					systemOverlays.remove(i);
				}
			}
			systemOverlays.add(0, overlay);
			return;
		}

		if (isSystem) {
			systemOverlays.add(overlay);
		} else {
			overlays.add(overlay);
		}
	}

	/**
	 * Adds a list of overlays.
	 * 
	 * @param overlays
	 *            The list
	 */
	public void addOverlays(final List<Overlay> overlays) {
		overlays.addAll(overlays);
	}

	/**
	 * Deletes all overlays.
	 */
	public void clearAllOverlays() {
		overlays.clear();
		systemOverlays.clear();
	}

	/**
	 * Deletes only the overlays created with the server-data.
	 */
	public void clearOverlays() {
		overlays.clear();
	}

	/**
	 * Returns a list of all overlays.
	 * 
	 * @return The list
	 */
	public List<Overlay> getOverlays() {
		final List<Overlay> l = new LinkedList<Overlay>();
		l.addAll(overlays);
		l.addAll(systemOverlays);
		return l;
	}

	/**
	 * Returns the {@link LocationOverlay}.
	 * 
	 * @return The overlay
	 */
	public LocationOverlay getLocationOverlay() {
		for (Overlay over : systemOverlays) {
			if (over instanceof LocationOverlay) {
				return (LocationOverlay) over;
			}
		}
		return null;
	}

	/**
	 * Returns the one and only instance.
	 * 
	 * @return The singleton
	 */
	public static RemoteData getInstance() {
		return instance;
	}
}