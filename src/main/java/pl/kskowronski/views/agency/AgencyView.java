package pl.kskowronski.views.agency;


import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.checkerframework.checker.units.qual.C;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("Agency")
@Route(value = "agency", layout = MainLayout.class)
@RolesAllowed("user")
public class AgencyView extends VerticalLayout {

    private ClientService clientService;

    public AgencyView(ClientService clientService) {
        this.clientService = clientService;

        var listAgency = getSelectAgency();

        add(listAgency);
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( query -> clientService.findAllAgency("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()));
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        selectAgency.setLabel("Agencja");
        selectAgency.addValueChangeListener( e -> {
            generateListWorkersForAgency();
        });
        return selectAgency;
    }

    private void generateListWorkersForAgency() {

    }

}
