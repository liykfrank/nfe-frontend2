package @packageName@.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import @packageName@.model.entity.Resource;
import @packageName@.pojo.ResourceSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CustomResourceRepositoryImpl implements CustomResourceRepository {

    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    /**
     * Creates the repository with a criteria builder.
     */
    public CustomResourceRepositoryImpl(EntityManager entityManager) {

        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Page<Resource> find(ResourceSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        return new PageImpl<>(getResourceList(searchCriteria, pageable, sort), pageable,
                getCount(searchCriteria));
    }

    private List<Resource> getResourceList(ResourceSearchCriteria searchCriteria,
            Pageable pageable, Sort sort) {

        CriteriaQuery<Resource> criteriaQuery = criteriaBuilder.createQuery(BsplinkFile.class);
        Root<Resource> resource = criteriaQuery.from(Resource.class);

        addSearchConditions(criteriaQuery, searchCriteria, resource);
        addOrder(criteriaQuery, resource, sort);

        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int maxResults = pageable.getPageSize();

        return entityManager
                .createQuery(criteriaQuery)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    private void addSearchConditions(CriteriaQuery<?> criteriaQuery,
            ResourceSearchCriteria searchCriteria, Root<Resource> root) {

        List<Predicate> conditions = new ArrayList<>();

        addEqualConditionIfValueIsPresent(searchCriteria.getId(), root.get("id"),
                conditions);

        addEqualConditionIfValueIsPresent(searchCriteria.getDescription(), root.get("description"),
                conditions);

        addCreatedDateConditionsIfValuesArePresent(searchCriteria, root, conditions);
        
        addModifiedDateConditionsIfValuesArePresent(searchCriteria, root, conditions);

        if (!conditions.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[] {})));
        }
    }
    
    private void addEqualConditionIfValueIsPresent(Object value, Path<Object> path,
            List<Predicate> conditions) {

        if (value != null) {

            conditions.add(criteriaBuilder.equal(path, value));
        }
    }
    
    private void addCreatedDateConditionsIfValuesArePresent(ResourceSearchCriteria searchCriteria,
            Root<Resource> root, List<Predicate> conditions) {

        if (searchCriteria.getMinCreatedInstant() != null) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"),
                    searchCriteria.getMinUploadDateTime()));
        }

        if (searchCriteria.getMaxCreatedInstant() != null) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"),
                    searchCriteria.getMaxUploadDateTime()));
        }
    }
    
    private void addModifiedDateConditionsIfValuesArePresent(ResourceSearchCriteria searchCriteria,
            Root<Resource> root, List<Predicate> conditions) {
        
        if (searchCriteria.getMinModifiedInstant() != null) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("modified"),
                    searchCriteria.getMinUploadDateTime()));
        }
        
        if (searchCriteria.getMaxModifiedInstant() != null) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("modified"),
                    searchCriteria.getMaxUploadDateTime()));
        }
    }
    
    private void addOrder(CriteriaQuery<Resource> criteriaQuery, Root<Resource> root,
            Sort sort) {

        if (!sort.isSorted()) {
            return;
        }

        List<Order> order = new ArrayList<>();

        sort.forEach(x -> {
            if (x.isAscending()) {
                order.add(criteriaBuilder.asc(root.get(x.getProperty())));
            } else {
                order.add(criteriaBuilder.desc(root.get(x.getProperty())));
            }
        });

        criteriaQuery.orderBy(order);
    }
    
    private Long getCount(ResourceSearchCriteria searchCriteria) {

        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Resource> root = criteriaQuery.from(Resource.class);

        addSearchConditions(criteriaQuery, searchCriteria, root);
        criteriaQuery.select(criteriaBuilder.count(root));

        return entityManager
                .createQuery(criteriaQuery)
                .getSingleResult();
    }
}
