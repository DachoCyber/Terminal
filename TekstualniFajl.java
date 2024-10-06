public class TekstualniFajl extends Fajl {
    private int brojKaraktera;
    private Enkodiranje enkodiranje;

    public TekstualniFajl(String naziv, int brojKaraktera, Enkodiranje enkodiranje) {
        super(naziv);
        this.brojKaraktera = brojKaraktera;
        this.enkodiranje = enkodiranje;
    }

    @Override
    public int velicina() {
        return brojKaraktera * enkodiranje.velicinaEnkodiranjaKaraktera();
    }
}
