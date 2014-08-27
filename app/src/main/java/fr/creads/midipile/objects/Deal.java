package fr.creads.midipile.objects;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class Deal {

    private Integer id;
    private Integer quantite;
    private float valeur;

    private String nom;
    private String desription;
    private String descriptionBreve;
    private String societe;
    private String descriptionSociete;
    private String website;
    private String eshop;
    private String facebook;
    private String twitter;
    private String pinterest;
    private String appStore;
    private String playStore;

    private String logo;

    private Integer fbAppId;
    private String fb;


    private String dateCreation;
    private String dateLancement;
    private String dateFin;

    @Override
    public String toString() {
        return "Deal{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
                '}';
    }
}
