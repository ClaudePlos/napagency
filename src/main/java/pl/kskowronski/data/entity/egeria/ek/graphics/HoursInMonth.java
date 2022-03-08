package pl.kskowronski.data.entity.egeria.ek.graphics;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EKT_GODZINY_W_MIESIACU")
public class HoursInMonth {

    @Id
    @Column(name = "GWM_ID")
    private Integer gwmId;

    @Temporal(TemporalType.DATE)
    @Column(name = "GWM_DATA")
    private Date date;

    @Column(name = "GWM_PRC_ID")
    private Integer gwmPrcId;

    @Column(name = "GWM_RG_KOD")
    private String gwmRgCode;

    @Column(name = "GWM_LICZBA_GODZIN")
    private Double gwmNumberOfHours;

    @Column(name = "GWM_GODZIN_DOD")
    private Double gwmAdditionalHours;

    @Transient
    private String rgName;

    public Integer getGwmId() {
        return gwmId;
    }

    public void setGwmId(Integer gwmId) {
        this.gwmId = gwmId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getGwmPrcId() {
        return gwmPrcId;
    }

    public void setGwmPrcId(Integer gwmPrcId) {
        this.gwmPrcId = gwmPrcId;
    }

    public String getGwmRgCode() {
        return gwmRgCode;
    }

    public void setGwmRgCode(String gwmRgCode) {
        this.gwmRgCode = gwmRgCode;
    }

    public Double getGwmNumberOfHours() {
        return gwmNumberOfHours;
    }

    public void setGwmNumberOfHours(Double gwmNumberOfHours) {
        this.gwmNumberOfHours = gwmNumberOfHours;
    }

    public Double getGwmAdditionalHours() {
        return gwmAdditionalHours;
    }

    public void setGwmAdditionalHours(Double gwmAdditionalHours) {
        this.gwmAdditionalHours = gwmAdditionalHours;
    }

    public String getRgName() {
        return rgName;
    }

    public void setRgName(String rgName) {
        this.rgName = rgName;
    }
}
