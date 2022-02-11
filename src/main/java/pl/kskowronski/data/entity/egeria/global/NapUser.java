package pl.kskowronski.data.entity.egeria.global;


import pl.kskowronski.data.Role;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "NAP_UZYTKOWNIK")
public class NapUser {

    @Id
    @Column(name = "PRC_ID")
    private Integer prcId;

    @Column(name = "UZ_NAZWA")
    private String username;

    @Column(name = "UZ_HASLO_ZAKODOWANE")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Transient
    private Set<Role> roles;

    public NapUser() {
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
