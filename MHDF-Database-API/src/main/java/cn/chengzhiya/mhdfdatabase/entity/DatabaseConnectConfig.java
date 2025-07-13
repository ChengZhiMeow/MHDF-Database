package cn.chengzhiya.mhdfdatabase.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public final class DatabaseConnectConfig {
    private final ConcurrentHashMap<String, String> pramHashMap = new ConcurrentHashMap<>();
    private String host;
    private String database;
    private String user;
    private String password;
    private File file;

    public DatabaseConnectConfig() {
        this.getPramHashMap().put("useUnicode", "true");
        this.getPramHashMap().put("characterEncoding", "utf8");
        this.getPramHashMap().put("cachePrepStmts", "true");
        this.getPramHashMap().put("prepStmtCacheSize", "250");
        this.getPramHashMap().put("prepStmtCacheSqlLimit", "2048");
        this.getPramHashMap().put("autoReconnect", "true");
        this.getPramHashMap().put("serverTimezone", TimeZone.getDefault().getID());
    }
}
