public abstract class Fajl {
    private String naziv;

    public Fajl() {
    }

    public Fajl(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public abstract int velicina();

    @Override
    public String toString() {
        String sufix;
        int velicina = this.velicina();

        if(velicina < 1024) {
            sufix = "B";
        }
        else if(velicina < (1024*1024)) {
            sufix = "KB";
            velicina /= 1024;
        }
        else if (velicina < (1024*1024*1024)){
            sufix = "MB";
            velicina /= (1024*1024);
        }
        else {
            sufix = "GB";
            velicina /= (1024*1024*1024);
        }

        return naziv + " " + velicina + sufix;
    }
}
