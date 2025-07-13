package cn.chengzhiya.mhdfdatabase.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DatabaseConfig {
    private String type;
    private DatabaseConnectConfig connectConfig;

    public void setType(String type) {
        this.type = type.toLowerCase();
    }
}
