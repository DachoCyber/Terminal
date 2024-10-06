public class Slika extends Fajl {
    private int sirina, visina;
    private boolean uBoji;

    public Slika(String naziv, int sirina, int visina, boolean uBoji) {
        super(naziv);
        this.sirina = sirina;
        this.visina = visina;
        this.uBoji = uBoji;
    }

    @Override
    public int velicina() {
        return sirina * visina * (uBoji ? 3 : 1);
    }
}
