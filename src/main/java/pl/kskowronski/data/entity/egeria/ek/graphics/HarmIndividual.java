package pl.kskowronski.data.entity.egeria.ek.graphics;


import javax.persistence.*;
import java.text.SimpleDateFormat;
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
    private Double hiHoursPlan;

    @Column(name = "HI_PRZEPRACOWANE")
    private Double hiHoursOverworked;

    @Column(name = "HI_ZMIANA")
    private String hiNameHarm;

    @Column(name = "HI_RODZAJ")
    private String hiType;

    @Transient
    private String absenceName;

    @Transient
    private String day;

    @Transient
    private String hhFrom ;

    @Transient
    private String hhTo;

    @Transient
    private Integer prcNumer;

    @Transient
    private String prcNazwisko;

    @Transient
    private String skKod;

    @Transient
    private String prcImie;

    @Transient
    private String pesel;

    @Transient
    private String hiDateS;

    @Transient
    private String bonus;

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

    public Double getHiHoursPlan() {
        return hiHoursPlan;
    }

    public void setHiHoursPlan(Double hiHoursPlan) {
        this.hiHoursPlan = hiHoursPlan;
    }

    public Double getHiHoursOverworked() {
        return hiHoursOverworked;
    }

    public void setHiHoursOverworked(Double hiHoursOverworked) {
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

    public String getHhFrom() {
        return hhFrom;
    }

    public void setHhFrom(String hhFrom) {
        this.hhFrom = hhFrom;
    }

    public String getHhTo() {
        return hhTo;
    }

    public void setHhTo(String hhTo) {
        this.hhTo = hhTo;
    }

    public Integer getPrcNumer() {
        return prcNumer;
    }

    public void setPrcNumer(Integer prcNumer) {
        this.prcNumer = prcNumer;
    }

    public String getPrcNazwisko() {
        return prcNazwisko;
    }

    public void setPrcNazwisko(String prcNazwisko) {
        this.prcNazwisko = prcNazwisko;
    }

    public String getPrcImie() {
        return prcImie;
    }

    public void setPrcImie(String prcImie) {
        this.prcImie = prcImie;
    }

    public String getSkKod() {
        return skKod;
    }

    public void setSkKod(String skKod) {
        this.skKod = skKod;
    }

    public String getHiDateS() {
        return hiDateS;
    }

    public void setHiDateS(String hiDateS) {
        this.hiDateS = hiDateS;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
