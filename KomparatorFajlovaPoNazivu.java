import java.util.Comparator;

public class KomparatorFajlovaPoNazivu implements Comparator<Fajl> {
    @Override
    public int compare(Fajl f1, Fajl f2) {
        return f1.getNaziv().compareTo(f2.getNaziv());
    }
}
