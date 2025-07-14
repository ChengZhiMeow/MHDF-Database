package cn.chengzhiya.mhdfdatabase.interfaces;

import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import lombok.Getter;

@Getter
public abstract class DatabaseService {
    private final MHDFDatabase instance;

    public DatabaseService(MHDFDatabase instance) {
        this.instance = instance;
    }

    /**
     * 获取数据库类型
     *
     * @return 数据库类型实例
     */
    abstract public String getType();

    /**
     * 设置连接配置实例
     */
    abstract public DatabaseConnectConfig getConnectConfig();

    /**
     * 设置连接配置实例
     *
     * @param connectConfig 连接配置实例
     */
    abstract public void setConnectConfig(DatabaseConnectConfig connectConfig);

    /**
     * 连接数据库
     */
    public void connect() {
        this.getInstance().getDatabaseThread().start();
        this.onConnect();
    }

    /**
     * 连接数据库时
     */
    abstract public void onConnect();

    /**
     * 关闭数据库连接
     */
    public void close() {
        this.getInstance().getDatabaseThread().kill();
        this.onClose();
    }

    /**
     * 关闭数据库连接时
     */
    abstract public void onClose();

    /**
     * 获取连接源实例
     *
     * @return 连接源实例
     */
    abstract public DataSourceConnectionSource getConnectionSource();
}
