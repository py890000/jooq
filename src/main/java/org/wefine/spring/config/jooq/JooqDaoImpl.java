package org.wefine.spring.config.jooq;

import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.row;

/**
 * 数据库操作基类.
 * <p>
 * 参考<code>org.jooq.impl.JooqDaoImpl</code>， 而实现的用于在Spring环境下使用的JOOQ_DAO的类。
 *
 * @author wefine
 */
@Slf4j
public abstract class JooqDaoImpl<R extends UpdatableRecord<R>, P, T> implements JooqDao<R, P, T> {

    private final Table<R> table;
    private final Class<P> type;
    @Resource
    protected DSLContext dsl;
    private RecordMapper<R, P> mapper;

    // -------------------------------------------------------------------------
    // XXX: Constructors and initialisation
    // -------------------------------------------------------------------------
    protected JooqDaoImpl(Table<R> table, Class<P> type, DSLContext dsl) {
        this.table = table;
        this.type = type;
        this.dsl = dsl;

        this.mapper = dsl.configuration().recordMapperProvider().provide(table.recordType(), type);
    }

    /**
     * [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
     * then we should let the database apply DEFAULT values
     */
    private static void resetChangedOnNotNull(Record record) {
        int size = record.size();

        for (int i = 0; i < size; i++) {
            if (record.get(i) == null) {
                if (!record.field(i).getDataType().nullable()) {
                    record.changed(i, false);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: JooqDao API
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method to provide custom implementations.
     */
    @Override
    public /* non-final */ RecordMapper<R, P> mapper() {
        return mapper;
    }

    @Override
    public /* non-final */ void insert(P object) {
        insert(singletonList(object));
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ void insert(P... objects) {
        insert(asList(objects));
    }

    @Override
    public /* non-final */ void insert(Collection<P> objects) {

        // Execute a batch INSERT
        if (objects.size() > 1) {
            dsl.batchInsert(records(objects, false)).execute();
        }

        // Execute a regular INSERT
        else if (objects.size() == 1) {
            records(objects, false).get(0).insert();
        }
    }

    @Override
    public /* non-final */ void update(P object) {
        update(singletonList(object));
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ void update(P... objects) {
        update(asList(objects));
    }

    @Override
    public /* non-final */ void update(Collection<P> objects) {

        // Execute a batch UPDATE
        if (objects.size() > 1) {
            dsl.batchUpdate(records(objects, true)).execute();
        }

        // Execute a regular UPDATE
        else if (objects.size() == 1) {
            records(objects, true).get(0).update();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ void delete(P... objects) {
        delete(asList(objects));
    }

    @Override
    public /* non-final */ void delete(Collection<P> objects) {

        // Execute a batch DELETE
        if (objects.size() > 1) {
            dsl.batchDelete(records(objects, true)).execute();
        }

        // Execute a regular DELETE
        else if (objects.size() == 1) {
            records(objects, true).get(0).delete();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ void deleteById(T... ids) {
        deleteById(asList(ids));
    }

    @Override
    public /* non-final */ void deleteById(Collection<T> ids) {
        Field<?>[] pk = pk();

        if (pk != null) {
            dsl.delete(table).where(equal(pk, ids)).execute();
        }
    }

    @Override
    public /* non-final */ boolean exists(P object) {
        return existsById(getId(object));
    }

    @Override
    public /* non-final */ boolean existsById(T id) {
        Field<?>[] pk = pk();

        return pk != null && dsl.selectCount().from(table).where(equal(pk, id)).fetchOne(0, Integer.class) > 0;
    }

    @Override
    public /* non-final */ long count() {
        return dsl
                .selectCount()
                .from(table)
                .fetchOne(0, Long.class);
    }

    @Override
    public /* non-final */ List<P> findAll() {
        return dsl
                .selectFrom(table)
                .fetch()
                .map(mapper());
    }

    @Override
    public /* non-final */ P findById(T id) {
        Field<?>[] pk = pk();
        R record = null;

        if (pk != null) {
            record = dsl
                    .selectFrom(table)
                    .where(equal(pk, id))
                    .fetchOne();
        }

        return record == null ? null : mapper().map(record);
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ <Z> List<P> fetch(Field<Z> field, Z... values) {
        return dsl
                .selectFrom(table)
                .where(field.in(values))
                .fetch()
                .map(mapper());
    }

    @Override
    public /* non-final */ <Z> P fetchOne(Field<Z> field, Z value) {
        R record = dsl
                .selectFrom(table)
                .where(field.equal(value))
                .fetchOne();

        return record == null ? null : mapper().map(record);
    }

    @Override
    public /* non-final */ <Z> Optional<P> fetchOptional(Field<Z> field, Z value) {
        return Optional.ofNullable(fetchOne(field, value));
    }

    @Override
    public /* non-final */ Table<R> getTable() {
        return table;
    }

    // ------------------------------------------------------------------------
    // XXX: Template methods for generated subclasses
    // ------------------------------------------------------------------------

    @Override
    public /* non-final */ Class<P> getType() {
        return type;
    }


    // ------------------------------------------------------------------------
    // XXX: Private utility methods
    // ------------------------------------------------------------------------

    protected abstract T getId(P object);

    @SuppressWarnings("unchecked")
    private /* non-final */ Condition equal(Field<?>[] pk, T id) {
        if (pk.length == 1) {
            return ((Field<Object>) pk[0]).equal(pk[0].getDataType().convert(id));
        }

        // [#2573] Composite key T types are of type Record[N]
        else {
            return row(pk).equal((Record) id);
        }
    }

    @SuppressWarnings({"all", "unchecked", "rawtypes"})
    private /* non-final */ Condition equal(Field<?>[] pk, Collection<T> ids) {
        if (pk.length == 1) {
            if (ids.size() == 1) {
                return equal(pk, ids.iterator().next());
            } else {
                return pk[0].in(pk[0].getDataType().convert(ids));
            }
        }

        // [#2573] Composite key T types are of type Record[N]
        else {
            return row(pk).in(ids.toArray(new Record[0]));
        }
    }

    private /* non-final */ Field<?>[] pk() {
        UniqueKey<?> key = table.getPrimaryKey();
        return key == null ? null : key.getFieldsArray();
    }

    private /* non-final */ List<R> records(Collection<P> objects, boolean forUpdate) {
        List<R> result = new ArrayList<>();
        Field<?>[] pk = pk();

        for (P object : objects) {
            R record = dsl.newRecord(table, object);

            if (forUpdate && pk != null)
                for (Field<?> field : pk)
                    record.changed(field, false);

            resetChangedOnNotNull(record);
            result.add(record);
        }

        return result;
    }

    @Override
    public List<P> search(Pageable pageable) {
        return search(null, pageable);
    }

    @Override
    public List<P> search(String searchTerm, Pageable pageable) {
        log.info("Finding {} todo entries for page {} by using search term: {}",
                pageable.getPageSize(),
                pageable.getPageNumber(),
                searchTerm
        );

        if (searchTerm != null) {
            searchTerm = searchTerm.trim();

        }
        String likeExpression = "%" + searchTerm + "%";

        List<R> queryResults = dsl.selectFrom(table)
                .where(createWhereConditions(likeExpression))
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetchInto(R);

        return null;
    }


    private Condition createWhereConditions(String likeExpression) {
        return table.DESCRIPTION.likeIgnoreCase(likeExpression)
                .or(table.TITLE.likeIgnoreCase(likeExpression));
    }

    private Collection<SortField<?>> getSortFields(Sort sortSpecification) {
        log.debug("Getting sort fields from sort specification: {}", sortSpecification);
        Collection<SortField<?>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            log.debug("No sort specification found. Returning empty collection -> no sorting is done.");
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();
            log.debug("Getting sort field with name: {} and direction: {}", sortFieldName, sortDirection);

            TableField tableField = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField getTableField(String sortFieldName) {
        TableField sortField = null;
        try {
            java.lang.reflect.Field tableField = table.getClass().getField(sortFieldName);
            sortField = (TableField) tableField.get(table);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }


    private SortField<?> convertTableFieldToSortField(TableField tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        } else {
            return tableField.desc();
        }
    }
}
