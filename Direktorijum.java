import java.util.*;

public class Direktorijum extends Fajl {
    private Direktorijum nadDirektorijum;
    private boolean koren;
    private Map<String, Fajl> fajlovi = new TreeMap<>();

    public Direktorijum getNadDirektorijum() {
        return nadDirektorijum;
    }

    public void setNadDirektorijum(Direktorijum nadDirektorijum) {
        this.nadDirektorijum = nadDirektorijum;
    }

    public Direktorijum(String naziv) {
        super(naziv);
    }

    public boolean isKoren() {
        return koren;
    }

    public void setKoren(boolean koren) {
        this.koren = koren;
    }

    public Map<String, Fajl> getFajlovi() {
        return fajlovi;
    }

    public boolean sadrzi(String naziv) {
        return fajlovi.containsKey(naziv);
    }

    public void dodajFajl(Fajl f) {
        fajlovi.put(f.getNaziv(), f);
    }

    public String sadrzaj(Comparator<Fajl> komparator) {
        ArrayList<Fajl> listaFajlova = new ArrayList<>(fajlovi.values());
        Collections.sort(listaFajlova, komparator);

        StringBuilder sb = new StringBuilder();
        for(Fajl f : listaFajlova) {
            sb.append("- ").append(f.toString()).append("\n");
        }
        return new String(sb);
    }

    @Override
    public int velicina() {
        int sum = 0;

        for(Fajl f : fajlovi.values())
            sum += f.velicina();

        return sum;
    }
}
