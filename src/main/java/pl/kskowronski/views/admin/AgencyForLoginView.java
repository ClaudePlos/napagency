package pl.kskowronski.views.admin;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.data.entity.admin.AgencyForLogin;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.admin.AgencyForLoginDataProvider;
import pl.kskowronski.data.service.admin.AgencyForLoginService;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.global.NapUserService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;

@PageTitle("AgencyForLoginView")
@Route(value = "agforlogin", layout = MainLayout.class)
@RolesAllowed("admin")
public class AgencyForLoginView extends Div {

    private NapUserService napUserService;
    private ClientService clientService;
    private AgencyForLoginService agencyForLoginService;

    private Crud<AgencyForLogin> crud;

    private String USER_NAME = "userName";
    private String AGENCY_NAME = "agencyName";
    private String EDIT_COLUMN = "vaadin-crud-edit-column";

    public AgencyForLoginView(NapUserService napUserService, ClientService clientService, AgencyForLoginService agencyForLoginService) {
        this.napUserService = napUserService;
        this.clientService = clientService;
        this.agencyForLoginService = agencyForLoginService;

        setHeight("100%");
        crud = new Crud<>(
                AgencyForLogin.class,
                createEditor()
        );
        crud.setHeightFull();

        setupGrid();
        setupDataProvider();

        add( new HorizontalLayout(crud));
    }

    private void setupGrid() {
        Grid<AgencyForLogin> grid = crud.getGrid();

        // Only show these columns (all columns shown by default):
        List<String> visibleColumns = Arrays.asList(
                USER_NAME,
                AGENCY_NAME,
                EDIT_COLUMN
        );
        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        // Reorder the columns (alphabetical by default)
        grid.setColumnOrder(
                grid.getColumnByKey(USER_NAME),
                grid.getColumnByKey(AGENCY_NAME),
                grid.getColumnByKey(EDIT_COLUMN)
        );
    }

    private void setupDataProvider() {
        AgencyForLoginDataProvider dataProvider = new AgencyForLoginDataProvider(agencyForLoginService, clientService, napUserService);
        crud.setDataProvider(dataProvider);
        crud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        crud.addSaveListener(saveEvent ->
                dataProvider.persist(saveEvent.getItem())
        );
    }


    private CrudEditor<AgencyForLogin> createEditor() {

        ComboBox<NapUser> selectUser = getSelectUser();
        ComboBox<Client> selectSkKod = getSelectAgency();

        FormLayout form = new FormLayout(selectUser, selectSkKod);

        Binder<AgencyForLogin> binder = new Binder<>(AgencyForLogin.class);
        binder.forField(selectUser).asRequired().bind(AgencyForLogin::getNapUser, AgencyForLogin::setNapUser);
        binder.forField(selectSkKod).asRequired().bind(AgencyForLogin::getClient, AgencyForLogin::setClient);


        return new BinderCrudEditor<>(binder, form);
    }

    private ComboBox<NapUser> getSelectUser() {
        ComboBox<NapUser> selectUser = new ComboBox<>();
        selectUser.setItems( query -> napUserService.findAll("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()) );
        selectUser.setItemLabelGenerator(NapUser::getUsername);
        selectUser.setLabel("Login");
        return selectUser;
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( query -> clientService.findAllAgency("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()));
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        return selectAgency;
    }


}
