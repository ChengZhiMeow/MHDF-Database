package cn.chengzhiya.mhdfdatabase.impl;

import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.interfaces.DatabaseService;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
public final class MySQLDatabaseServiceImpl extends DatabaseService {
    @Setter
    private DatabaseConnectConfig connectConfig;
    private HikariDataSource hikariDataSource;
    private DataSourceConnectionSource connectionSource;

    @Override
    public String getType() {
        return "mysql";
    }

    @Override
    @SneakyThrows
    public void onConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败");
        }

        String databaseUrl = "jdbc:mysql://" + this.getConnectConfig().getHost() + "/" + this.getConnectConfig().getDatabase();

        HikariConfig config = new HikariConfig();
        this.getConnectConfig().getPramHashMap().forEach(config::addDataSourceProperty);
        config.setJdbcUrl(databaseUrl);
        config.setUsername(this.getConnectConfig().getUser());
        config.setPassword(this.getConnectConfig().getPassword());
        this.hikariDataSource = new HikariDataSource(config);

        this.connectionSource = new DataSourceConnectionSource(this.hikariDataSource, databaseUrl);
    }

    @Override
    @SneakyThrows
    public void onClose() {
        this.connectionSource.close();
        this.hikariDataSource.close();
    }
}
