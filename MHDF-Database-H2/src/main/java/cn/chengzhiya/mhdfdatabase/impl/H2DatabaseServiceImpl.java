package cn.chengzhiya.mhdfdatabase.impl;

import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.interfaces.DatabaseService;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;

@Getter
public final class H2DatabaseServiceImpl extends DatabaseService {
    @Setter
    private DatabaseConnectConfig connectConfig;
    private HikariDataSource hikariDataSource;
    private DataSourceConnectionSource connectionSource;

    @Override
    public String getType() {
        return "h2";
    }

    @Override
    @SneakyThrows
    public void onConnect() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败");
        }

        File file = this.getConnectConfig().getFile();
        String databaseUrl = "jdbc:h2:" + file.getAbsolutePath();

        HikariConfig config = new HikariConfig();
        this.getConnectConfig().getPramHashMap().forEach(config::addDataSourceProperty);
        config.setJdbcUrl(databaseUrl);
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
