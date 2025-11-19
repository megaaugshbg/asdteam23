// Path.java
import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<City> cities;
    private int totalDistance;

    public Path() {
        this.cities = new ArrayList<>();
        this.totalDistance = 0;
    }

    public void addCity(City city, int distance) {
        cities.add(city);
        totalDistance += distance;
    }

    public List<City> getCities() { return cities; }
    public int getTotalDistance() { return totalDistance; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cities.size(); i++) {
            sb.append(cities.get(i).getName());
            if (i < cities.size() - 1) {
                sb.append(" → ");
            }
        }
        sb.append(" (").append(totalDistance).append(" km)");
        return sb.toString();
    }
}