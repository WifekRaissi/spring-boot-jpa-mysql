package com.axeane.SpringBootMysql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "salarie")
public class Salarie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);

    @NotEmpty
    @NotNull
    private String nom;

    @NotEmpty
    @NotNull
    private String prenom;

    @NotNull
    private BigDecimal salaire;

    @NotEmpty
    @NotNull
    @Size(max = 256, message = "address should have maximum 256 characters")
    private String adresse;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Departement departement;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getAdresse() {
        return adresse;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Required
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Salarie() {
    }

    public Salarie(Long id, String nom, String prenom, BigDecimal salaire, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.adresse = adresse;
    }

    public Salarie(String nom, String prenom, BigDecimal salaire, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.adresse = adresse;
        id = count.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Salarie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                ", adresse='" + adresse + '\'' +
                ", departement=" + departement +
                '}';
    }

    public Salarie(@NotEmpty @NotNull String nom, @NotEmpty @NotNull String prenom, @NotNull BigDecimal salaire, @NotEmpty @NotNull @Size(max = 256, message = "address should have maximum 256 characters") String adresse, Departement departement) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.adresse = adresse;
        this.departement = departement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salarie salarie = (Salarie) o;
        if (id != salarie.id) return false;
        if (!nom.equals(salarie.nom)) return false;
        if (!prenom.equals(salarie.prenom)) return false;
        if (!salaire.equals(salarie.salaire)) return false;
        if (!adresse.equals(salarie.adresse)) return false;
        return departement.equals(salarie.departement);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + salaire.hashCode();
        result = 31 * result + adresse.hashCode();
        result = 31 * result + departement.hashCode();
        return result;
    }
}
