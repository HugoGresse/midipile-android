package fr.creads.midipile.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class User implements Parcelable {

    private String email;
    private String civilite;
    private String nom;
    private String prenom;
    private String rue;
    private String rue_bis;
    private String ville;
    private String pays;


    private String code_postal;
    private String mobile;
    private String newsletter;

    private String fid;

    private String chance;
    private String credit;

    private String wxsseHeaders;

    private List<Badge> badges = new ArrayList<Badge>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(email);
        parcel.writeString(civilite);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(rue);
        parcel.writeString(rue_bis);
        parcel.writeString(ville);
        parcel.writeString(pays);

        parcel.writeString(newsletter);
        parcel.writeString(code_postal);
        parcel.writeString(mobile);
        parcel.writeString(chance);
        parcel.writeString(fid);
        parcel.writeString(credit);

        parcel.writeTypedList(badges);
    }

    public User(){}

    public User(Parcel in) {
        email = in.readString();
        nom = in.readString();
        prenom = in.readString();
        rue = in.readString();
        rue_bis = in.readString();
        civilite = in.readString();
        ville = in.readString();
        pays = in.readString();

        newsletter = in.readString();
        code_postal = in.readString();
        mobile = in.readString();
        chance = in.readString();
        fid = in.readString();
        credit = in.readString();


        in.readTypedList(badges, Badge.CREATOR);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getRue_bis() {
        return rue_bis;
    }

    public void setRue_bis(String rue_bis) {
        this.rue_bis = rue_bis;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(String newsletter) {
        this.newsletter = newsletter;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getChance() {
        Integer chanceInt;
        chanceInt = (Integer) Integer.parseInt(chance);
        if(null == chanceInt) {
            chanceInt = 0;
        }
        return chanceInt;
    }

    public String getChanceString() {
        return chance;
    }

    public String getXwsseHeader(){
        return wxsseHeaders;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public void setXwsseHeader(String header){
        wxsseHeaders = header;
    }
}
