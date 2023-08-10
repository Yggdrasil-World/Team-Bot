package de.overcraft.logging;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public interface Markers {
    Marker INTERACTION_MARKER = MarkerManager.getMarker("INTERACTION");
    Marker SLASHCOMMANDINTERACTION_MARKER = MarkerManager.getMarker("SC_INTERACTION").setParents(INTERACTION_MARKER);
}
