package es.uji.ei102720mgph.SANA.model;

public class PostalCodeMunicipality implements Comparable<PostalCodeMunicipality> {
    private String municipality;
    private String postalCode;

    public PostalCodeMunicipality(){
        super();
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int compareTo(PostalCodeMunicipality altre) {
        return this.getPostalCode().compareTo(altre.getPostalCode());
    }

    @Override
    public String toString() {
        return "PostalCodeMunicipality{" +
                "municipality='" + municipality + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
