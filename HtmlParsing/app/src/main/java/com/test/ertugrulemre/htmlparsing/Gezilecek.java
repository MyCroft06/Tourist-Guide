package com.test.ertugrulemre.htmlparsing;

public class Gezilecek {

    private String  isim;
    private String  aciklama;
    private String  adres;
    private String  location;

    public Gezilecek(String location, String isim, String aciklama, String adres) {
        super();
        this.isim = isim;
        this.aciklama = aciklama;
        this.location = location;
        this.adres = adres;
    }

    @Override
    public String toString() {
        return isim;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String isAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String isLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String isAdres() { return adres; }

    public void setAdres(String adres) {
        this.adres = adres;
    }

}
