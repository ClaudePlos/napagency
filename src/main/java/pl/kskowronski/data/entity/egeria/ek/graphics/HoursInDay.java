package pl.kskowronski.data.entity.egeria.ek.graphics;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EK_GODZINY_W_DNIU")
public class HoursInDay {

    @Id
    @Column(name = "GWD_ID")
    private Integer gwdId;

    @Column(name = "GWD_HI_ID")
    private Integer gwdHiId;

    @Temporal(TemporalType.TIME)
    @Column(name = "GWD_OD")
    private Date hFrom;

    @Temporal(TemporalType.TIME)
    @Column(name = "GWD_DO")
    private Date hTo;

    @Column(name = "GWD_RG_KOD")
    private String gwdRgCode;

    @Transient
    private String rgName;

    public Integer getGwdId() {
        return gwdId;
    }

    public void setGwdId(Integer gwdId) {
        this.gwdId = gwdId;
    }

    public Integer getGwdHiId() {
        return gwdHiId;
    }

    public void setGwdHiId(Integer gwdHiId) {
        this.gwdHiId = gwdHiId;
    }

    public Date gethFrom() {
        return hFrom;
    }

    public void sethFrom(Date hFrom) {
        this.hFrom = hFrom;
    }

    public Date gethTo() {
        return hTo;
    }

    public void sethTo(Date hTo) {
        this.hTo = hTo;
    }

    public String getGwdRgCode() {
        return gwdRgCode;
    }

    public void setGwdRgCode(String gwdRgCode) {
        this.gwdRgCode = gwdRgCode;
    }

    public String getRgName() {
        return rgName;
    }

    public void setRgName(String rgName) {
        this.rgName = rgName;
    }
}
