package org.iata.bsplink.filemanager.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CustomBsplinkFileRepositoryImpl implements CustomBsplinkFileRepository {

    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    /**
     * Creates the repository with a criteria builder.
     */
    public CustomBsplinkFileRepositoryImpl(EntityManager entityManager) {

        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Page<BsplinkFile> find(BsplinkFileSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        return new PageImpl<>(getFileList(searchCriteria, pageable, sort), pageable,
                getCount(searchCriteria));
    }

    private Long getCount(BsplinkFileSearchCriteria searchCriteria) {

        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<BsplinkFile> root = criteriaQuery.from(BsplinkFile.class);

        addSearchConditions(criteriaQuery, searchCriteria, root);
        criteriaQuery.select(criteriaBuilder.count(root));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private void addSearchConditions(CriteriaQuery<?> criteriaQuery,
            BsplinkFileSearchCriteria searchCriteria, Root<BsplinkFile> root) {

        List<Predicate> conditions = new ArrayList<>();

        addEqualConditionIfValueIsPresent(searchCriteria.getId(), root.get("id"), conditions);

        addEqualConditionIfValueIsPresent(searchCriteria.getName(), root.get("name"), conditions);

        addEqualConditionIfValueIsPresent(searchCriteria.getStatus(), root.get("status"),
                conditions);

        addNotEqualConditionIfValueIsNotPresent(searchCriteria.getStatus(), root.get("status"),
                conditions, BsplinkFileStatus.TRASHED);

        addEqualConditionIfValueIsPresent(searchCriteria.getType(), root.get("type"), conditions);

        addUloadDateConditionsIfValuesArePresent(searchCriteria, root, conditions);
        addBytesConditionIfValuesArePresent(searchCriteria, root, conditions);

        if (!conditions.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[] {})));
        }
    }

    private void addEqualConditionIfValueIsPresent(Object value, Path<Object> path,
            List<Predicate> conditions) {

        if (value != null && value instanceof List) {

            List<BsplinkFileStatus> status = (List<BsplinkFileStatus>) value;

            if (!status.isEmpty()) {
                conditions.add(criteriaBuilder.in(path).value(status));
            }

        } else if (value != null) {

            conditions.add(criteriaBuilder.equal(path, value));
        }
    }

    private void addNotEqualConditionIfValueIsNotPresent(Object value, Path<Object> path,
            List<Predicate> conditions, Object notEqualToValue) {

        if (value == null) {

            conditions.add(criteriaBuilder.notEqual(path, notEqualToValue));
        }
    }

    private void addUloadDateConditionsIfValuesArePresent(BsplinkFileSearchCriteria searchCriteria,
            Root<BsplinkFile> root, List<Predicate> conditions) {

        if (searchCriteria.getMinUploadDateTime() != null) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("uploadDateTime"),
                    searchCriteria.getMinUploadDateTime()));
        }

        if (searchCriteria.getMaxUploadDateTime() != null) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("uploadDateTime"),
                    searchCriteria.getMaxUploadDateTime()));
        }
    }

    private void addBytesConditionIfValuesArePresent(BsplinkFileSearchCriteria searchCriteria,
            Root<BsplinkFile> root, List<Predicate> conditions) {

        if (searchCriteria.getMinBytes() != null) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bytes"),
                    searchCriteria.getMinBytes()));
        }

        if (searchCriteria.getMaxBytes() != null) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("bytes"),
                    searchCriteria.getMaxBytes()));
        }
    }

    private List<BsplinkFile> getFileList(BsplinkFileSearchCriteria searchCriteria,
            Pageable pageable, Sort sort) {

        CriteriaQuery<BsplinkFile> criteriaQuery = criteriaBuilder.createQuery(BsplinkFile.class);
        Root<BsplinkFile> bsplinkFile = criteriaQuery.from(BsplinkFile.class);

        addSearchConditions(criteriaQuery, searchCriteria, bsplinkFile);
        addOrder(criteriaQuery, bsplinkFile, sort);

        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int maxResults = pageable.getPageSize();

        return entityManager.createQuery(criteriaQuery).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    private void addOrder(CriteriaQuery<BsplinkFile> criteriaQuery, Root<BsplinkFile> root,
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
}
