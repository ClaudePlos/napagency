package pl.kskowronski.views.agency;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.componets.PeriodLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("Agency")
@Route(value = "agency", layout = MainLayout.class)
@RolesAllowed("user")
public class AgencyView extends VerticalLayout {

    private ClientService clientService;
    private ZatrudnienieService zatrudnienieService;

    private PeriodLayout periodText = new PeriodLayout(1);

    public AgencyView(ClientService clientService, ZatrudnienieService zatrudnienieService) {
        this.clientService = clientService;
        this.zatrudnienieService = zatrudnienieService;

        var listAgency = getSelectAgency();

        add(new HorizontalLayout(periodText, listAgency));
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( query -> clientService.findAllAgency("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()));
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        selectAgency.setLabel("Agencja");
        selectAgency.addValueChangeListener( e -> {
            generateListWorkersForAgency(e.getValue().getKlKod() );
        });
        return selectAgency;
    }

    private void generateListWorkersForAgency( Integer klKodAgency ) {

        var workers = zatrudnienieService.getWorkersEmployedOnTheAgency( periodText.getPeriod(), Long.valueOf("0"), klKodAgency);
        System.out.println(workers);

    }

}
