package com.ccloomi.web.dbManager.service.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ccloomi.core.common.service.GenericService;
import com.ccloomi.core.component.sql.imp.SQLMaker;
import com.ccloomi.core.util.StringUtil;
import com.ccloomi.web.dbManager.bean.VisNetworkBean;
import com.ccloomi.web.dbManager.entity.ColumnsEntity;
import com.ccloomi.web.dbManager.entity.InnodbSysForeignColsEntity;
import com.ccloomi.web.dbManager.entity.InnodbSysForeignEntity;
import com.ccloomi.web.dbManager.entity.SchemataEntity;
import com.ccloomi.web.dbManager.entity.TablesEntity;
import com.ccloomi.web.dbManager.service.SchemataService;

/**
 * 类    名：SchemataServiceImp
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年11月23日-上午11:06:24
 */
@Service("schemataService")
public class SchemataServiceImp extends GenericService<SchemataEntity> implements SchemataService{
	@Override
	public VisNetworkBean findAsVisNetworkBySchemaName(String schemaName) {
		VisNetworkBean vn=new VisNetworkBean();
		SQLMaker sm=new SQLMaker();
		sm.SELECT_AS("MAX(t.data_length)", "mx")
		.SELECT_AS("MIN(t.data_length)", "mn")
		.FROM(new TablesEntity(), "t")
		.WHERE("t.table_schema=?", schemaName);
		List<Map<String, Object>>ls=findBySQLGod(sm);
		BigInteger mx=BigInteger.valueOf(0);
		BigInteger mn=BigInteger.valueOf(0);
		for(Map<String, Object>m:ls){
			mx=(BigInteger) m.get("mx");
			mn=(BigInteger) m.get("mn");
			break;
		}
		BigInteger a=BigInteger.ONE.subtract(mn.divide(mx)).multiply(BigInteger.valueOf(100));
		BigInteger b=a.compareTo(BigInteger.valueOf(50))>0
				?BigInteger.valueOf(100)
				:(a.compareTo(BigInteger.valueOf(25))>0
						?BigInteger.valueOf(75)
						:BigInteger.valueOf(50)
				);
//		//查询数据库Nodes
//		sm.SELECT_AS("s.schema_name", "id")
//		.SELECT_AS("s.schema_name", "label")
//		.SELECT_AS("'database'", "group")
//		.FROM(new SchemataEntity(), "s")
//		.WHERE("s.schema_name=?", schemaName);
//		List<Map<String, Object>>schemata=findBySQLGod(sm);
//		vn.addNodes(schemata);
		//查询表Nodes
		sm.clean()
		.SELECT("t.table_name")
		.SELECT_AS("CONCAT(t.table_schema,'/',t.table_name)", "id")
		.SELECT_AS("CONCAT(t.table_name,'\n',t.table_comment)", "label")
		.SELECT_AS("CONCAT(t.table_schema,'\n',t.table_comment)", "title")
		.SELECT_AS(StringUtil.format("(t.data_length*?)/?", b,mx), "size")
		.SELECT_AS("t.table_name", "group")
		.FROM(new TablesEntity(), "t")
		.WHERE("t.table_schema=?", schemaName);
		List<Map<String, Object>>tables=findBySQLGod(sm);
		vn.addNodes(tables);
		
		List<Map<String, Object>>fromTableToDatabase=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>>fromColumnToTable=new ArrayList<Map<String,Object>>();
		ColumnsEntity c=new ColumnsEntity();
		for(Map<String, Object>table:tables){
			//查询字段Nodes
			sm.clean()
			.SELECT_AS("CONCAT(c.table_schema,'/',c.table_name,'/',c.column_name)", "id")
			.SELECT_AS("CONCAT(c.column_name,'\n',c.column_comment)", "label")
			.SELECT_AS("CONCAT(c.table_name,'::',c.column_name)", "title")
			.SELECT_AS("c.table_name", "group")
			.FROM(c, "c")
			.WHERE("c.table_schema=?", schemaName)
			.AND("c.table_name=?",table.get("table_name"));
			List<Map<String, Object>>columns=findBySQLGod(sm);
			vn.addNodes(columns);

			//查询表到数据库边
//			Map<String, Object>t2d=new HashMap<String, Object>();
//			t2d.put("from", table.get("id"));
//			t2d.put("to", schemaName);
//			fromTableToDatabase.add(t2d);
			//查询字段到表边
			for(Map<String, Object>column:columns){
				Map<String, Object>c2t=new HashMap<String, Object>();
				c2t.put("from", column.get("id"));
				c2t.put("to", table.get("id"));
				fromColumnToTable.add(c2t);
			}
		}
		vn.addEdges(fromTableToDatabase);
		vn.addEdges(fromColumnToTable);
		return vn;
	}

	@Override
	public List<Map<String, Object>> findColumn2ColumnAsVisNetworkEdgesBySchemaName(String schemaName) {
		SQLMaker sm=new SQLMaker();
		//查询字段到字段边
		sm.clean()
		.SELECT("f.id")
		.SELECT_AS("CONCAT(f.FOR_NAME,'/',fc.FOR_COL_NAME)", "from")
		.SELECT_AS("CONCAT(f.REF_NAME,'/',fc.REF_COL_NAME)", "to")
		.SELECT_AS("'true'", "dashes")
		.FROM(new InnodbSysForeignEntity(), "f")
		.LEFT_JOIN(new InnodbSysForeignColsEntity(), "fc", "f.id=fc.id")
		.WHERE("f.id LIKE ?",schemaName+"%");
		return findBySQLGod(sm);
	}
	
}