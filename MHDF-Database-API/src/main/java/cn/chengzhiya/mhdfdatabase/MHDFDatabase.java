package cn.chengzhiya.mhdfdatabase;

import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.exception.NoDatabaseServiceImplException;
import cn.chengzhiya.mhdfdatabase.interfaces.DatabaseService;
import cn.chengzhiya.mhdfdatabase.runnable.MHDFDatabaseThread;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public final class MHDFDatabase {
    @Getter(value = AccessLevel.PRIVATE)
    private final CopyOnWriteArraySet<Class<? extends DatabaseService>> databaseServiceClassList = new CopyOnWriteArraySet<>();
    @Getter(value = AccessLevel.PRIVATE)
    private final ConcurrentHashMap<Class<?>, String> tableHashMap = new ConcurrentHashMap<>();

    private final MHDFDatabaseThread databaseThread = new MHDFDatabaseThread();

    private final DatabaseConfig config;
    private DatabaseService databaseService;

    @SafeVarargs
    public MHDFDatabase(DatabaseConfig config, Class<? extends DatabaseService>... databaseServiceClasses) throws NoDatabaseServiceImplException {
        this.config = config;
        this.databaseServiceClassList.addAll(List.of(databaseServiceClasses));

        for (Class<? extends DatabaseService> clazz : this.getDatabaseServiceClassList()) {
            try {
                this.databaseService = clazz.getConstructor(MHDFDatabase.class).newInstance(this);

                if (!this.getConfig().getType().equals(this.getDatabaseService().getType())) {
                    continue;
                }

                this.getDatabaseService().setConnectConfig(this.getConfig().getConnectConfig());
                return;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        throw new NoDatabaseServiceImplException(this.getConfig().getType());
    }

    /**
     * 添加表
     *
     * @param clazz 类实例
     * @param table 表名称
     */
    public void addTable(Class<?> clazz, String table) {
        this.getTableHashMap().put(clazz, table);
    }

    /**
     * 添加表
     *
     * @param clazz 类实例
     */
    public void addTable(Class<?> clazz) {
        DatabaseTable annotation = clazz.getDeclaredAnnotation(DatabaseTable.class);
        if (annotation == null) {
            throw new IllegalArgumentException("在 " + clazz + " 中没有找到 DatabaseTable 注解");
        }

        String tableName = annotation.tableName();
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("在 " + clazz + " 中没有找到有效的表名称");
        }

        this.addTable(clazz, tableName);
    }

    /**
     * 获取表配置实例
     *
     * @param clazz 类实例
     * @return 表配置实例
     */
    @SneakyThrows
    public <T> DatabaseTableConfig<T> getTableConfig(Class<T> clazz) {
        DatabaseTableConfig<T> tableConfig = DatabaseTableConfig.fromClass(
                this.getDatabaseService().getConnectionSource().getDatabaseType(),
                clazz
        );
        tableConfig.setTableName(this.getTableHashMap().get(clazz));

        return tableConfig;
    }

    /**
     * 创建所有表
     */
    @SneakyThrows
    public void createAllTable() {
        for (Class<?> clazz : this.getTableHashMap().keySet()) {
            try {
                TableUtils.createTableIfNotExists(this.getDatabaseService().getConnectionSource(), getTableConfig(clazz));
            } catch (Exception e) {
                // ORMLite 中如果表存在还是会重复创建 index 索引,所以需要忽略这个报错
                if (e.getCause() != null && e.getCause().toString().contains("Duplicate key name")) {
                    continue;
                }

                throw new RuntimeException(e);
            }
        }
    }
}
