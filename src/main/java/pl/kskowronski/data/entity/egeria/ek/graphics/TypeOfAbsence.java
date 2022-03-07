package pl.kskowronski.data.entity.egeria.ek.graphics;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ek_rodzaje_absencji")
public class TypeOfAbsence {

    @Id
    @Column(name = "RDA_ID")
    private Integer rdaId;

    @Column(name = "RDA_KOD")
    private String rdaCode;

    @Column(name = "RDA_NAZWA")
    private String rdaName;

    public Integer getRdaId() {
        return rdaId;
    }

    public void setRdaId(Integer rdaId) {
        this.rdaId = rdaId;
    }

    public String getRdaCode() {
        return rdaCode;
    }

    public void setRdaCode(String rdaCode) {
        this.rdaCode = rdaCode;
    }

    public String getRdaName() {
        return rdaName;
    }

    public void setRdaName(String rdaName) {
        this.rdaName = rdaName;
    }
}
