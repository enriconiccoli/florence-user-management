package it.florence.assignment.user_management.repository;

import it.florence.assignment.user_management.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepositoryImpl{

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findUsersByNameSurname(String name, String surname) {


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.hasLength(name)){
            predicates.add(criteriaBuilder.equal(userRoot.get("name"), name));
        }
        if(StringUtils.hasLength(surname)){
            predicates.add(criteriaBuilder.equal(userRoot.get("surname"), surname));
        }

        if(predicates.size() == 2){
            query.where(criteriaBuilder.and(predicates.get(0), predicates.get(1)));
        }
        else {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(query).getResultList();
    }

}
