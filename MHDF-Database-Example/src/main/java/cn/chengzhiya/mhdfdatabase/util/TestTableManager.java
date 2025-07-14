package cn.chengzhiya.mhdfdatabase.util;

import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdfdatabase.table.TestTable;

public final class TestTableManager extends AbstractDaoManager<TestTable, String> {
    public TestTableManager(MHDFDatabase instance) {
        super(instance);
    }
}
