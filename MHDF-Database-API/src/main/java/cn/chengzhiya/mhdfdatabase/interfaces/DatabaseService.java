package cn.chengzhiya.mhdfdatabase.interfaces;

import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;

public abstract class DatabaseService {
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
        MHDFDatabase.instance.getDatabaseThread().start();
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
        MHDFDatabase.instance.getDatabaseThread().kill();
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
