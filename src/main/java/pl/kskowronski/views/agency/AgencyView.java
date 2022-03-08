package pl.kskowronski.views.agency;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.service.admin.AgencyForLoginService;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.ek.graphics.HarmIndividualService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInMonthService;
import pl.kskowronski.data.service.egeria.global.NapUserService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.componets.PeriodLayout;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Agency")
@Route(value = "agency", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("user")
public class AgencyView extends VerticalLayout {

    private ClientService clientService;
    private ZatrudnienieService zatrudnienieService;
    private HarmIndividualService harmIndividualService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<Pracownik> grid = new Grid<>(Pracownik.class, false);
    private ComboBox<Client> listAgency;
    private List<Client> agencyList = new ArrayList<>();

    @Autowired
    private WorkCardView workCardView;

    public AgencyView(ClientService clientService, ZatrudnienieService zatrudnienieService, NapUserService napUserService, HoursInMonthService hoursInMonthService
            , HarmIndividualService harmIndividualService, HoursInDayService hoursInDayService, AgencyForLoginService agencyForLoginService) {
        this.clientService = clientService;
        this.zatrudnienieService = zatrudnienieService;
        this.harmIndividualService = harmIndividualService;
        this.workCardView = new WorkCardView(this.harmIndividualService, hoursInDayService, hoursInMonthService);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user  = napUserService.findByUsername(userDetails.getUsername());
        var agencyForUser = agencyForLoginService.findAllForUser(user.get().getPrcId());
        agencyForUser.stream().forEach( item -> {
            Client client = clientService.findByKlKod(item.getKlKod());
            agencyList.add(client);
        });

        listAgency = getSelectAgency();

        this.setHeight("100%");
        grid.setHeightFull();

        grid.addColumn(Pracownik::getPrcNumer).setHeader("Numer");
        grid.addColumn(Pracownik::getPrcNazwisko).setHeader("Nazwisko");
        grid.addColumn(Pracownik::getPrcImie).setHeader("Imie");
        grid.addColumn(Pracownik::getPrcPesel).setHeader("Pesel");
        grid.addComponentColumn(worker -> {
            Button viewWorkCard = new Button("Karta Pracy");
            viewWorkCard.addClickListener( e -> {
                workCardView.openView(worker);
            });
            return viewWorkCard;
        });


        periodText.addClickListener( e -> {
            generateListWorkersForAgency();
        });

        add(new HorizontalLayout(periodText, new Label("Agencja:"), listAgency), grid);
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( agencyList );
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        selectAgency.addValueChangeListener( e -> {
            generateListWorkersForAgency();
        });
        return selectAgency;
    }

    private void generateListWorkersForAgency() {
        var workers = zatrudnienieService.getWorkersEmployedOnTheAgency( periodText.getPeriod(), Long.valueOf("0"), listAgency.getValue().getKlKod() );
        grid.setItems(workers);
    }

}
