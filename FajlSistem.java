import java.awt.*;
import java.io.BufferedWriter;
import java.util.Comparator;
import java.io.*;
import java.util.Map;

public class FajlSistem {
    private Direktorijum koren;
    private Direktorijum trenutniDirektorijum;

    public FajlSistem() {
        koren = new Direktorijum("/");
        koren.setKoren(true);
        trenutniDirektorijum = koren;
    }

    public Direktorijum getKoren() {
        return koren;
    }

    public Direktorijum getTrenutniDirektorijum() {
        return trenutniDirektorijum;
    }

    public String ls(Comparator<Fajl> komparator) {
        for(Map.Entry<String, Fajl> fajls : trenutniDirektorijum.getFajlovi().entrySet()) {
            System.out.println(fajls.getKey() + "->" + fajls.getValue() + "\n");
        }
        return trenutniDirektorijum.sadrzaj(komparator);
    }

    public String apsolutnaPutanja(){
        Direktorijum td = trenutniDirektorijum;

        if (td.isKoren())
            return "/";

        String apsolutnaPutanja = "";
        while(!td.isKoren()) {
            apsolutnaPutanja = "/" + td.getNaziv() + apsolutnaPutanja;
            td = td.getNadDirektorijum();
        }
        return apsolutnaPutanja;
    }

    public String pwd() {
        return this.apsolutnaPutanja();
    }

    public void cd(String dir) {
        if((!dir.equals("..") && !trenutniDirektorijum.getFajlovi().containsKey(dir)) ||
                (dir.equals("..") && trenutniDirektorijum.isKoren()))
            throw new IllegalArgumentException("Nije moguce prebaciti se u direktorijum " + dir + "!");
        else if(dir.equals(".."))
            trenutniDirektorijum = trenutniDirektorijum.getNadDirektorijum();
        else
            trenutniDirektorijum = (Direktorijum) trenutniDirektorijum.getFajlovi().get(dir);
    }
    public void mkdir(String dir) {
        Direktorijum newDir = new Direktorijum(dir);
        if(trenutniDirektorijum.getFajlovi().containsKey(dir)) {
            throw new IllegalArgumentException("Direktorijum sa tim imenom vec postoji!");
        } else {
            trenutniDirektorijum  .getFajlovi().put(dir, newDir);
            newDir.setNadDirektorijum(trenutniDirektorijum);
        }
    }
    public void touch(String fileName) {
        if (trenutniDirektorijum.getFajlovi().containsKey(fileName)) {
            throw new IllegalArgumentException("Fajl sa tim imenom vec postoji!");
        }
        String[] nameAndExt = fileName.split("\\.");
        Fajl newFajl;

        if (nameAndExt[1].equals("jpg") || nameAndExt[1].equals("png")) {
            newFajl = new Slika(fileName, 0, 0, false);
        } else if (nameAndExt[1].equals("txt")) {
            newFajl = new TekstualniFajl(fileName, 0, Enkodiranje.ASCII);
        } else {
            throw new IllegalArgumentException("Nepoznat tip fajla: " + nameAndExt[1]);
        }

        trenutniDirektorijum.getFajlovi().put(fileName, newFajl);

        // Write absolute path of the new file to 'src/fajlovi.txt'
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fajlovi.txt", true))) {
            writer.write("\n" + this.apsolutnaPutanja() + fileName);
        } catch (IOException e) {
            System.err.println("Greska pri pisanju u fajl: " + e.getMessage());
        }
    }
}
