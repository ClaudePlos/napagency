package pl.kskowronski.data.entity.admin;


import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import javax.persistence.*;

@Entity
@Table(name = "NPP_AGENCY_FOR_LOGIN")
public class AgencyForLogin {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "KL_KOD")
    private Integer klKod;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "AGENCY_NAME")
    private String agencyName;

    @Transient
    private NapUser napUser;

    @Transient
    private Client client;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKlKod() {
        return klKod;
    }

    public void setKlKod(Integer klKod) {
        this.klKod = klKod;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public NapUser getNapUser() {
        return napUser;
    }

    public void setNapUser(NapUser napUser) {
        this.napUser = napUser;
        this.userId = napUser.getUzId();
        if (this.userName == null) {
            this.userName = napUser.getUsername();
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        if ( client != null){
            this.klKod = client.getKlKod();
            if (this.agencyName == null) {
                this.agencyName = client.getKldNazwa();
            }
        }

    }
}
