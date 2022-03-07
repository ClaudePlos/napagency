package pl.kskowronski.data.service.admin;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import pl.kskowronski.data.entity.admin.AgencyForLogin;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.global.NapUserService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class AgencyForLoginDataProvider extends AbstractBackEndDataProvider<AgencyForLogin, CrudFilter> {

    // A real app should hook up something like JPA
    List<AgencyForLogin> DATABASE;
    private AgencyForLoginService agencyForLoginService;
    private Consumer<Long> sizeChangeListener;


    public AgencyForLoginDataProvider(AgencyForLoginService agencyForLoginService, ClientService clientService, NapUserService napUserService) {
        this.agencyForLoginService = agencyForLoginService;
        DATABASE = new ArrayList<>(agencyForLoginService.findAll());
        DATABASE.stream().forEach( item -> {
            item.setNapUser(napUserService.findById(item.getUserId()).get());
            item.setClient(clientService.findByKlKod(item.getKlKod()));
        });
    }

    private static Predicate<AgencyForLogin> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<AgencyForLogin>) person -> {
                    try {
                        Object value = valueOf(constraint.getKey(), person);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    private static Comparator<AgencyForLogin> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<AgencyForLogin> comparator = Comparator.comparing(person ->
                                (Comparable) valueOf(sortClause.getKey(), person)
                        );

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;

                    } catch (Exception ex) {
                        return (Comparator<AgencyForLogin>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, AgencyForLogin person) {
        try {
            Field field = AgencyForLogin.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Stream<AgencyForLogin> fetchFromBackEnd(Query<AgencyForLogin, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<AgencyForLogin> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<AgencyForLogin, CrudFilter> query) {
        // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    public void persist(AgencyForLogin item) {
        if (item.getId() == null) {
            Comparator<AgencyForLogin> comparator = Comparator.comparing( AgencyForLogin::getId );
            Integer max = DATABASE.stream().max(comparator).get().getId();
            item.setId(max + 1);
        }

        final Optional<AgencyForLogin> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            agencyForLoginService.deleteById(item.getId());
            DATABASE.add(position, item);
            agencyForLoginService.save(item);
        } else {
            DATABASE.add(item);
            agencyForLoginService.save(item);
        }
    }

    Optional<AgencyForLogin> find(Integer id) {
        return DATABASE
                .stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public void delete(AgencyForLogin item) {
        agencyForLoginService.deleteById(item.getId());
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }
}
