package pl.kskowronski.data.service.egeria.ckk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ClientService extends CrudService<Client, Integer> {

    private ClientRepo repo;

    public ClientService(@Autowired ClientRepo repo) {
        this.repo = repo;
    }

    @Override
    protected ClientRepo getRepository() {
        return repo;
    }

    public Optional<Client> getClientByKlKod(Integer klKod){ return repo.getClientByKlKod(klKod); };


    public Stream<Client> findAllAgency(String filterString, int page, int pageSize){
        String likeFilter = "%" + filterString + "%";
        Stream<Client> list = repo.findAllWithPagination(likeFilter, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "kld_nazwa"))).stream();
        return list;
    }

}