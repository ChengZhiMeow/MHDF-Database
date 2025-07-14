package cn.chengzhiya.mhdfdatabase.dao;

import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

@Getter
public abstract class AbstractDaoManager<V, K> {
    private final MHDFDatabase instance;

    private final ThreadLocal<Dao<V, K>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
                    Class<V> clazz = (Class<V>) type.getActualTypeArguments()[0];

                    return DaoManager.createDao(this.getInstance().getDatabaseService().getConnectionSource(), clazz);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    public AbstractDaoManager(MHDFDatabase instance) {
        this.instance = instance;
    }

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    private Dao<V, K> getDao() {
        return this.daoThread.get();
    }

    /**
     * 获取数据实例l列表
     *
     * @return 数据实例l列表
     */
    @SneakyThrows
    public List<V> getList() {
        return this.getDao().queryForAll();
    }

    /**
     * 获取查询构建器实例
     *
     * @return 查询构建器实例
     */
    public QueryBuilder<V, K> getQueryBuilder() {
        return this.getDao().queryBuilder();
    }

    /**
     * 执行查询实例
     *
     * @param qb 查询实例
     * @return 查询数据实例结果列表
     */
    @SneakyThrows
    public List<V> queryForList(QueryBuilder<V, K> qb) {
        return qb.query();
    }

    /**
     * 执行查询实例
     *
     * @param qb 查询实例
     * @return 查询数据实例结果列表
     */
    @SneakyThrows
    public List<V> queryForList(QueryBuilder<V, K> qb, List<V> defaultValue) {
        List<V> value = this.queryForList(qb);
        return value == null ? defaultValue : value;
    }

    /**
     * 执行查询实例
     *
     * @param qb 查询实例
     * @return 查询数据实例结果
     */
    @SneakyThrows
    public V queryFirst(QueryBuilder<V, K> qb) {
        return qb.queryForFirst();
    }

    /**
     * 执行查询实例
     *
     * @param qb           查询实例
     * @param defaultValue 默认数据实例
     * @return 数据实例
     */
    @SneakyThrows
    public V queryFirstOrDefault(QueryBuilder<V, K> qb, V defaultValue) {
        V value = this.queryFirst(qb);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取指定ID实例的数据实例
     *
     * @param id ID实例
     * @return 数据实例
     */
    @SneakyThrows
    public V getById(K id) {
        return this.getDao().queryForId(id);
    }

    /**
     * 获取指定ID实例的数据实例
     *
     * @param id           ID实例
     * @param defaultValue 默认数据实例
     * @return 数据实例
     */
    @SneakyThrows
    public V getByIdOrDefault(K id, V defaultValue) {
        V value = this.getById(id);
        return value == null ? defaultValue : value;
    }

    /**
     * 删除数据
     *
     * @param entity 数据实例
     */
    @SneakyThrows
    public void delete(V entity) {
        this.getDao().delete(entity);
    }

    /**
     * 删除数据
     *
     * @param entity 数据实例
     * @param async  异步处理
     */
    public void delete(V entity, boolean async) {
        if (async) {
            this.getInstance().getDatabaseThread().execute(() -> this.delete(entity));
            return;
        }

        this.delete(entity);
    }

    /**
     * 更新数据
     *
     * @param entity 数据实例
     */
    @SneakyThrows
    public void update(V entity) {
        this.getDao().createOrUpdate(entity);
    }

    /**
     * 更新数据
     *
     * @param entity 数据实例
     * @param async  异步处理
     */
    @SneakyThrows
    public void update(V entity, boolean async) {
        if (async) {
            this.getInstance().getDatabaseThread().execute(() -> this.update(entity));
            return;
        }

        this.update(entity);
    }
}
