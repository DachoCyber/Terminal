public enum Enkodiranje {
    UTF8, UTF16, ASCII;

    public int velicinaEnkodiranjaKaraktera() {
        switch (this) {
            case ASCII : return  1;
            case UTF8 : return 2;
            case UTF16 : return 4;
        };
        return 1;
    }

    public static Enkodiranje odStringa(String str){
        switch (str) {
            case "ASCII" : return ASCII;
            case "UTF8" : return UTF8;
            case "UTF16" : return UTF16;
        };
        return ASCII;
    }
}
