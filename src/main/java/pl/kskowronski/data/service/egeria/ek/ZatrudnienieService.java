package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.WymiarEtatu;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.service.egeria.global.ConsolidationService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZatrudnienieService extends CrudService<Zatrudnienie, Integer> {

    @PersistenceContext
    private EntityManager em;

    private ConsolidationService consolidationService;

    private ZatrudnienieRepo repo;

    public ZatrudnienieService(@Autowired ZatrudnienieRepo repo, ConsolidationService consolidationService) {
        this.repo = repo;
        this.consolidationService = consolidationService;
    }

    @Override
    protected ZatrudnienieRepo getRepository() {
        return repo;
    }

    @Autowired
    WymiarEtatuRepo wymiarEtatuRepo;


    private SimpleDateFormat dfYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    private MapperDate mapperDate = new MapperDate();


    public List<Pracownik> getWorkersEmployedOnTheAgency( String okres, Long typeContract, Integer klKodAgency ){
        consolidationService.setConsolidateCompany();
        List<Pracownik> listaAktPracNaSk = new ArrayList<Pracownik>();
        String sql = "select distinct prc_id, prc_numer, prc_nazwisko, prc_imie, prc_pesel, zat_wymiar, zat_status\n" +
                "from ek_zatrudnienie, ek_pracownicy\n";
        sql += "where (COALESCE(zat_data_do, to_date('2099', 'YYYY')) >= to_date('" + okres + "', 'YYYY-MM')\n" +
                "and zat_data_zmiany <= last_day(to_date('" + okres + "', 'YYYY-MM')))\n" +
                "and zat_typ_umowy = " + typeContract + "\n" +
                "and zat_frm_id = 300325 \n" +
                "and zat_um_id in (select um_id from knt_umowy where um_kl_kod_ma_strone = " + klKodAgency + ")\n" +
                "and zat_prc_id = prc_id\n";
        sql += " order by prc_nazwisko, prc_imie";

        List result = em.createNativeQuery(sql).getResultList();
        for (Iterator iter = result.iterator(); iter.hasNext();)
        {
            Object[] ob = (Object[]) iter.next();
            Pracownik prac = new Pracownik();
            Integer prcId = ((BigDecimal) ob[0]).intValue();
            Integer prcNumer = ((BigDecimal) ob[1]).intValue();
            prac.setPrcId(Integer.valueOf(prcId.toString()));
            prac.setPrcNumer(Integer.valueOf(prcNumer.toString()));
            prac.setPrcNazwisko((String) ob[2]);
            prac.setPrcImie((String) ob[3]);
            prac.setPrcPesel((String) ob[4]);

            if ( typeContract == 0 ){
                Zatrudnienie zat = new Zatrudnienie();
                List<Zatrudnienie> listZat = new ArrayList<>();
                Optional<WymiarEtatu> wymEtatu =  wymiarEtatuRepo.findById( ((BigDecimal) ob[5]).toString() );
                zat.setWymiarEtatu(wymEtatu.get());
                zat.setDef0( ((BigDecimal) ob[6]).toString() );
                listZat.add(zat);
                prac.setZatrudnienia(listZat);
            }

            listaAktPracNaSk.add(prac);
        }
        return listaAktPracNaSk;
    }


    public Optional<List<Zatrudnienie>> getActualContractForWorker(Integer prcId, String period) throws ParseException {
        //consolidationService.setConsolidateCompany();
        Date dataOd = dfYYYYMMDD.parse(period + "-01");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataOd);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //last day month
        Date dataDo = cal.getTime();

        Optional<List<Zatrudnienie>> contracts = Optional.of(new ArrayList<>());
        try {
            contracts = Optional.ofNullable(em.createQuery("select z from Zatrudnienie z where z.zatPrcId = :prcId "
                    + "and z.zatDataZmiany <= :dataDo and COALESCE(z.zatDataDo, :dataOd) >= :dataOd "
                    + "and z.zatTypUmowy = 0")
                    .setParameter("prcId", prcId)
                    .setParameter("dataOd", dataOd, TemporalType.DATE)
                    .setParameter("dataDo", dataDo, TemporalType.DATE)
                    .getResultList());
        } catch ( Exception ex ){
            System.out.println(ex.getMessage());
        }

        if ( contracts.isPresent() ){
            contracts.get().stream().forEach( z -> {
                z.setWymiarEtatu(wymiarEtatuRepo.findById(z.getZatWymiar().toString()).get());
            });
        }
        return contracts;
    }

    public Optional<List<Zatrudnienie>> getActualContractUzForWorker(Integer prcId, String period) throws ParseException {
        //consolidationService.setConsolidateCompany();
        Date dataOd = dfYYYYMMDD.parse(period + "-01");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataOd);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //last day month
        Date dataDo = cal.getTime();

        Optional<List<Zatrudnienie>> contracts = Optional.of(new ArrayList<>());
        try {
            contracts = Optional.ofNullable(em.createQuery("select z from Zatrudnienie z where z.zatPrcId = :prcId "
                    + "and z.zatDataZmiany <= :dataDo and COALESCE(z.zatDataDo, :dataOd) >= :dataOd "
                    + "and z.zatTypUmowy in (2)")
                    .setParameter("prcId", prcId)
                    .setParameter("dataOd", dataOd, TemporalType.DATE)
                    .setParameter("dataDo", dataDo, TemporalType.DATE)
                    .getResultList());
        } catch ( Exception ex ){
            System.out.println(ex.getMessage());
        }


        return contracts;
    }

    public List<Pracownik> getPracownikZatrudNaSkMc(Integer prcIdForm, String okres, Integer frmId, Long typeContract ){
        //consolidationService.setConsolidateCompanyOnCompany(frmId);
        List<Pracownik> listaAktPracNaSk = new ArrayList<Pracownik>();
        // todo KS usunąć stanowiska kosztow administracji
        String sql = "select distinct prc_id, prc_numer, prc_nazwisko, prc_imie, prc_pesel, zat_wymiar, zat_status\n" +
                "from ek_zatrudnienie, ek_pracownicy\n";
        sql += "where (COALESCE(zat_data_do, to_date('2099', 'YYYY')) >= to_date('" + okres + "', 'YYYY-MM')\n" +
                "and zat_data_zmiany <= last_day(to_date('" + okres + "', 'YYYY-MM')))\n" +
                "and zat_typ_umowy = " + typeContract + "\n" +
                "and zat_prc_id = prc_id\n";
        sql += "and zat_prc_id = " + prcIdForm + " order by prc_nazwisko, prc_imie";

        List result = em.createNativeQuery(sql).getResultList();
        for (Iterator iter = result.iterator(); iter.hasNext();)
        {
            Object[] ob = (Object[]) iter.next();
            Pracownik prac = new Pracownik();
            Integer prcId = ((BigDecimal) ob[0]).intValue();
            Integer prcNumer = ((BigDecimal) ob[1]).intValue();
            prac.setPrcId(Integer.valueOf(prcId.toString()));
            prac.setPrcNumer(Integer.valueOf(prcNumer.toString()));
            prac.setPrcNazwisko((String) ob[2]);
            prac.setPrcImie((String) ob[3]);
            prac.setPrcPesel((String) ob[4]);

            if ( typeContract == 0 ){
                Zatrudnienie zat = new Zatrudnienie();
                List<Zatrudnienie> listZat = new ArrayList<>();
                Optional<WymiarEtatu> wymEtatu =  wymiarEtatuRepo.findById( ((BigDecimal) ob[5]).toString() );
                zat.setWymiarEtatu(wymEtatu.get());
                zat.setDef0( ((BigDecimal) ob[6]).toString() );
                listZat.add(zat);
                prac.setZatrudnienia(listZat);
            }

            listaAktPracNaSk.add(prac);
        }
        return listaAktPracNaSk;
    }


}
