import java.util.Comparator;
import java.util.Map;

public class KomparatorFajlovaPoVelicini implements Comparator<Fajl> {
    @Override
    public int compare(Fajl f1, Fajl f2) {
        return -Integer.compare(f1.velicina(), f2.velicina());
    }

}
