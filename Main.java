import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {
    public static FajlSistem fs = new FajlSistem();

    public void pisi(String path, String text) {
        try(FileWriter fw = new FileWriter(path, true)) {
            for(int i = 0; i < text.length(); i++) {
                fw.write(text.charAt(i));
            }
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

    public static void ucitaj(String path) {
        try {
            /*
            * U listi Stringova nalazi se sadrzaj fajla src/fajlovi.txt.
            *
            *
            * */

            List<String> linije = Files.readAllLines(Paths.get(path));
            for(String l : linije) { // For petlja cita jednu po jednu liniju niza linije(List<String>)
                String[] tokeni = l.split(" "); // Oblik linije fajla izgleda npr users/dalibor.jpg 1000  1000 1 i tokeni[0]
                // = users/dalibor.txt, tokeki[1] = 1000, ... split(regex : " ") razdvaja fajl po space
                String[] putanja = tokeni[0].split("/"); // putanja je token[0] podeljen po /
                Direktorijum trenutni = fs.getKoren(); // trenutni direktorijum je na pocetku koreni
                for(int i=0; i<putanja.length-1; i++) {     //idemo do pretposlednjeg tokena, jer poslednji token nije Direktorijum vec Fajl
                    String dir = putanja[i];               //na pocetku je putanja[0] = users npr, i onda se dodaje u 
                    if(trenutni.sadrzi(dir)) {
                        trenutni = (Direktorijum) trenutni.getFajlovi().get(dir);
                    }
                    else {
                        Direktorijum novi = new Direktorijum(dir);
                        novi.setNadDirektorijum(trenutni);
                        trenutni.dodajFajl(novi);
                        trenutni = novi;
                    }
                }
                String naziv = putanja[putanja.length-1];
                Fajl novi;
                // Ako je slika, onda ima tokeni linija izgleda kao: putanja visina sirina uBoji,
                // pa je tokeni.length == 4
                if(tokeni.length == 4) {
                    int sirina = Integer.parseInt(tokeni[1]);
                    int visina = Integer.parseInt(tokeni[2]);
                    boolean uBoji = Integer.parseInt(tokeni[3]) == 1;
                    novi = new Slika(naziv, sirina, visina, uBoji);
                }
                else if(tokeni.length == 3) {
                    int brojKaraktera = Integer.parseInt(tokeni[1]);
                    Enkodiranje enkodiranje = Enkodiranje.odStringa(tokeni[2]);
                    novi = new TekstualniFajl(naziv, brojKaraktera, enkodiranje);
                }
                else {
                    novi = new Direktorijum(naziv);
                }
                trenutni.dodajFajl(novi);
            }
        }
        catch (NumberFormatException e) {
            System.err.println("Problem sa parsiranjem!");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("Nije uspelo citanje iz fajla!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        ucitaj("src/fajlovi.txt");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBoxRoot = new VBox(20);
        vBoxRoot.setPadding(new Insets(10, 10, 10, 10));

        HBox hBox = new HBox();
        hBox.setSpacing(10);

        RadioButton rbNaziv = new RadioButton("Sort po nazivu");
        RadioButton rbVelicina = new RadioButton("Sort po velicini");
        ToggleGroup tg = new ToggleGroup();
        rbNaziv.setToggleGroup(tg);
        rbNaziv.setSelected(true);
        rbVelicina.setToggleGroup(tg);

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(380);
        textArea.setPrefHeight(130);

        vBoxRoot.getChildren().addAll(hBox, rbNaziv, rbVelicina, textArea);

        //----------------------------------------------------------------------------------

        Label lblTerminal = new Label("Terminal:");
        lblTerminal.setMinSize(70, 25);

        TextField textField = new TextField();
        textField.setPrefWidth(250);
        textField.setPrefHeight(20);

        Button btnIzvrsi = new Button("Izvrsi");
        btnIzvrsi.setPrefWidth(100);
        btnIzvrsi.setPrefHeight(20);

        hBox.getChildren().addAll(lblTerminal, textField, btnIzvrsi);

        //----------------------------------------------------------------------------------

        btnIzvrsi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String naredba = textField.getText().trim();

                if(naredba.equals("ls")) {
                    textArea.setText(fs.getTrenutniDirektorijum().getNaziv());
                    textArea.appendText("\n");

                    if(rbNaziv.isSelected()){
                        textArea.appendText(fs.ls(new KomparatorFajlovaPoNazivu()));
                    }
                    else {
                        textArea.appendText(fs.ls(new KomparatorFajlovaPoVelicini()));
                    }
                }
                else if(naredba.equals("pwd")) {
                    textArea.setText(fs.apsolutnaPutanja());
                }
                else if(naredba.startsWith("cd") && naredba.split(" ").length == 2) {
                    try {
                        fs.cd(naredba.split(" ")[1].trim());
                        textArea.setText("Tekuci direktorijum: " + fs.getTrenutniDirektorijum().getNaziv());
                        String fileText = fs.pwd();
                        String filePath = "src/fajlovi.txt";
                        pisi(filePath, fileText);
                    }
                    catch (IllegalArgumentException ex) {
                        textArea.setText(ex.getMessage());
                    }
                } else if(naredba.startsWith("mkdir")) {
                    try {
                        fs.mkdir(naredba.split(" ")[1].trim());
                        textArea.setText("Napravljen direktorijum " + naredba.split(" ")[1].trim());
                        String fileText = fs.pwd();
                        String filePath = "src/fajlovi.txt";
                        pisi(filePath, fileText);
                    } catch(IllegalArgumentException ex) {
                        textArea.setText(ex.getMessage());
                    }
                } else if(naredba.startsWith("touch")) {
                    try {
                        fs.touch(naredba.split(" ")[1].trim());
                        textArea.setText("Napravljen fajl " + naredba.split(" ")[1].trim());
                    } catch(IllegalArgumentException ex) {
                        textArea.setText(ex.getMessage());
                    }
                }
            }
        });

        //----------------------------------------------------------------------------------

        Scene scene = new Scene(vBoxRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fajl sistem");
        primaryStage.show();
    }
}
