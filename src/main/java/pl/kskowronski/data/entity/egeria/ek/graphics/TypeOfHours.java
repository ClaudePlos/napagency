package pl.kskowronski.data.entity.egeria.ek.graphics;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ek_rodzaje_godzin")
public class TypeOfHours {

    @Id
    @Column(name = "RG_KOD")
    private String rgCode;

    @Column(name = "RG_NAZWA")
    private String rgName;

    public String getRgCode() {
        return rgCode;
    }

    public void setRgCode(String rgCode) {
        this.rgCode = rgCode;
    }

    public String getRgName() {
        return rgName;
    }

    public void setRgName(String rgName) {
        this.rgName = rgName;
    }
}
