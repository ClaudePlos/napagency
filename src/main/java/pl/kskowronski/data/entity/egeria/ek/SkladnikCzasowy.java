package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ek_skladniki_czasowe")
public class SkladnikCzasowy {

    @Id
    @Column(name = "SKCZ_ID")
    private Integer skczId;

    @Column(name = "SKCZ_PRC_ID")
    private Integer skczPrcId;

    @Column(name = "SKCZ_DSK_ID")
    private Integer skczDskId;

    @Column(name = "SKCZ_DATA_OD")
    private LocalDate skczDataOd;

    @Column(name = "SKCZ_DATA_DO")
    private LocalDate skczDataDo;

    @Column(name = "SKCZ_KwotaDod")
    private Double skczKwotaDod;

    @Transient
    private String componentName;

    public SkladnikCzasowy() {
    }

    public Integer getSkczId() {
        return skczId;
    }

    public void setSkczId(Integer skczId) {
        this.skczId = skczId;
    }

    public Integer getSkczPrcId() {
        return skczPrcId;
    }

    public void setSkczPrcId(Integer skczPrcId) {
        this.skczPrcId = skczPrcId;
    }

    public Integer getSkczDskId() {
        return skczDskId;
    }

    public void setSkczDskId(Integer skczDskId) {
        this.skczDskId = skczDskId;
    }

    public LocalDate getSkczDataOd() {
        return skczDataOd;
    }

    public void setSkczDataOd(LocalDate skczDataOd) {
        this.skczDataOd = skczDataOd;
    }

    public LocalDate getSkczDataDo() {
        return skczDataDo;
    }

    public void setSkczDataDo(LocalDate skczDataDo) {
        this.skczDataDo = skczDataDo;
    }

    public Double getSkczKwotaDod() {
        return skczKwotaDod;
    }

    public void setSkczKwotaDod(Double skczKwotaDod) {
        this.skczKwotaDod = skczKwotaDod;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
