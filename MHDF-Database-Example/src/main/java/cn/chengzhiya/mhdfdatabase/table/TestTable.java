package cn.chengzhiya.mhdfdatabase.table;

import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class TestTable {
    @DatabaseField(id = true)
    private String user;
    @DatabaseField(canBeNull = false)
    private Double money;
}
