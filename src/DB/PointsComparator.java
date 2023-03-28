package DB;

import org.json.simple.JSONObject;

import java.util.Comparator;

public class PointsComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        return Integer.compare((Integer) o2.get("Points"), (Integer) o1.get("Points"));
    }
}
