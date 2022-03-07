package pl.kskowronski.data.entity.egeria.ek.graphics;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EK_HARMONOGRAMY_INDYWIDUALNE")
public class HarmIndividual {


    @Id
    @Column(name = "HI_ID")
    private Integer hiId;

    @Column(name = "HI_PRC_ID")
    private Integer hiPrcId;

    @Column(name = "HI_ZAT_ID")
    private Integer hiZatId;

    @Column(name = "HI_RDA_ID")
    private Integer hiRdaId;

    @Temporal(TemporalType.DATE)
    @Column(name = "HI_DZIEN")
    private Date hiDate;

    @Column(name = "HI_GODZINY")
    private Integer hiHoursPlan;

    @Column(name = "HI_PRZEPRACOWANE")
    private Integer hiHoursOverworked;

    @Column(name = "HI_ZMIANA")
    private String hiNameHarm;

    @Column(name = "HI_RODZAJ")
    private String hiType;

    @Transient
    private String absenceName;

    @Transient
    private String day;

    public Integer getHiId() {
        return hiId;
    }

    public void setHiId(Integer hiId) {
        this.hiId = hiId;
    }

    public Integer getHiPrcId() {
        return hiPrcId;
    }

    public void setHiPrcId(Integer hiPrcId) {
        this.hiPrcId = hiPrcId;
    }

    public Integer getHiZatId() {
        return hiZatId;
    }

    public void setHiZatId(Integer hiZatId) {
        this.hiZatId = hiZatId;
    }

    public Integer getHiRdaId() {
        return hiRdaId;
    }

    public void setHiRdaId(Integer hiRdaId) {
        this.hiRdaId = hiRdaId;
    }

    public Integer getHiHoursPlan() {
        return hiHoursPlan;
    }

    public void setHiHoursPlan(Integer hiHoursPlan) {
        this.hiHoursPlan = hiHoursPlan;
    }

    public Integer getHiHoursOverworked() {
        return hiHoursOverworked;
    }

    public void setHiHoursOverworked(Integer hiHoursOverworked) {
        this.hiHoursOverworked = hiHoursOverworked;
    }

    public String getHiNameHarm() {
        return hiNameHarm;
    }

    public void setHiNameHarm(String hiNameHarm) {
        this.hiNameHarm = hiNameHarm;
    }

    public String getHiType() {
        return hiType;
    }

    public void setHiType(String hiType) {
        this.hiType = hiType;
    }

    public String getAbsenceName() {
        return absenceName;
    }

    public void setAbsenceName(String absenceName) {
        this.absenceName = absenceName;
    }

    public Date getHiDate() {
        return hiDate;
    }

    public void setHiDate(Date hiDate) {
        this.hiDate = hiDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
