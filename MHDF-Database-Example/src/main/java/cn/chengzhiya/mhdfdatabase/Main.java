package cn.chengzhiya.mhdfdatabase;

import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.exception.NoDatabaseServiceImplException;
import cn.chengzhiya.mhdfdatabase.impl.MySQLDatabaseServiceImpl;
import cn.chengzhiya.mhdfdatabase.table.TestTable;
import cn.chengzhiya.mhdfdatabase.util.TestTableManager;

public final class Main {
    public static void main(String[] args) {
        DatabaseConnectConfig connectConfig = new DatabaseConnectConfig();
        connectConfig.setHost("127.0.0.1:3306");
        connectConfig.setDatabase("mhdf_database");
        connectConfig.setUser("root");
        connectConfig.setPassword("root");

        DatabaseConfig config = new DatabaseConfig();
        config.setType("mysql");
        config.setConnectConfig(connectConfig);

        MHDFDatabase mhdfDatabase;
        try {
            mhdfDatabase = new MHDFDatabase(config, MySQLDatabaseServiceImpl.class);
            mhdfDatabase.getDatabaseService().connect();
        } catch (NoDatabaseServiceImplException e) {
            throw new RuntimeException(e);
        }

        mhdfDatabase.addTable(TestTable.class, "test_table");
        mhdfDatabase.createAllTable();
        System.out.println("test_table 表创建成功!");

        TestTableManager testTableManager = new TestTableManager(mhdfDatabase);

        TestTable data = new TestTable();
        data.setUser("ChengZhiMeow");
        data.setMoney(Double.MIN_VALUE);

        testTableManager.update(data);
        System.out.println("插入数据成功!");

        data.setMoney(0d);
        testTableManager.update(data);
        System.out.println("更新数据成功!");

        testTableManager.delete(data);
        System.out.println("删除数据成功!");

        mhdfDatabase.getDatabaseService().close();
    }
}
