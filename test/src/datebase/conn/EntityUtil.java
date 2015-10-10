package datebase.conn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 自动生成MyBatis的实体类、实体映射XML文件、Mapper
 *
 * @author WYS
 * @date 2014-11-8
 * @version v1.0
 */
public class EntityUtil {

	private final String type_char = "char";

	private final String type_date = "date";

	private final String type_timestamp = "timestamp";

	private final String type_int = "int";

	private final String type_bigint = "bigint";

	private final String type_text = "text";

	private final String type_bit = "bit";

	private final String type_decimal = "decimal";
	
	private final String type_double = "double";

	private final String type_blob = "blob";
	
	private final String moduleName = "crawler";// 对应模块名称

	private final String bean_path = "d:/entity_bean";

	private final String mapper_path = "d:/entity_mapper";

	private final String xml_path = "d:/entity_mapper/xml";

	private final String bean_package = "com.cmcc.crawler.entity.";

	private final String mapper_package = "com.cmcc.crawler.dao.";

	private final String driverName = "com.mysql.jdbc.Driver";

	private final String user = "crawler-user";

	private final String password = "1qaz2wsx";

	private final String url = "jdbc:mysql://192.168.17.213:3306/" + moduleName
			+ "?characterEncoding=utf8";

	private String tableName = null;
	
	private String beanName = null;

	private String mapperName = null;

	private Connection conn = null;

	private void init() throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		conn = DriverManager.getConnection(url, user, password);
	}
	
	
	private static Map<String, String> TYPE_DB_BEAN = new HashMap<String, String>();
	private static Map<String, String> TYPE_DB_XML = new HashMap<String, String>();
	static{
		TYPE_DB_BEAN.put("text", "String");
		TYPE_DB_BEAN.put("varchar", "String");
		TYPE_DB_BEAN.put("decimal", "Double");
		TYPE_DB_BEAN.put("double", "Double");
		TYPE_DB_BEAN.put("int", "Integer");
		TYPE_DB_BEAN.put("tinyint", "Integer");
		TYPE_DB_BEAN.put("timestamp", "Date");
		
		TYPE_DB_XML.put("text", "VARCHAR");
		TYPE_DB_XML.put("varchar", "VARCHAR");
		TYPE_DB_XML.put("bigtext", "VARCHAR");
		TYPE_DB_XML.put("mediumtext", "VARCHAR");
		TYPE_DB_XML.put("decimal", "DECIMAL");
		TYPE_DB_XML.put("tinyint", "TINYINT");
		TYPE_DB_XML.put("double", "DOUBLE");
		TYPE_DB_XML.put("int", "INTEGER");
		TYPE_DB_XML.put("timestamp", "TIMESTAMP");
	}
	
	/**
	 * get all table
	 *
	 * @return
	 * @throws SQLException
	 */
	private List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();
		PreparedStatement pstate = conn.prepareStatement("show tables");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString(1);
			// if ( tableName.toLowerCase().startsWith("yy_") ) {
			tables.add(tableName);
			// }
		}
		return tables;
	}

	private void processTable(String table) {
		StringBuffer sb = new StringBuffer();
		String tableNames[] = null;
		if(tableName.startsWith("t_crawler")){
			tableNames = tableName.replaceFirst("t_crawler", "").split("_");
		}else{
			tableNames = tableName.split("_");
		}
		for (int i = tableNames.length; i > 0; i--) {
			if(!StringUtils.isBlank(tableNames[i-1])){
				sb.append(captureName(tableNames[i-1]));
			}
		}
		beanName = sb.toString();
		mapperName = beanName + "Dao";
	}

	private String processType(String type) {
		if (type.indexOf(type_char) > -1) {
			return "String";
		} else if (type.indexOf(type_bigint) > -1) {
			return "Long";
		} else if (type.indexOf(type_int) > -1) {
			return "Integer";
		} else if (type.indexOf(type_date) > -1) {
			return "Date";
		} else if (type.indexOf(type_text) > -1) {
			return "String";
		} else if (type.indexOf(type_timestamp) > -1) {
			return "Date";
		} else if (type.indexOf(type_bit) > -1) {
			return "Boolean";
		} else if (type.indexOf(type_decimal) > -1) {
			return "Double";
		} else if (type.indexOf(type_blob) > -1) {
			return "byte[]";
		} else if (type.indexOf(type_double) > -1) {
			return "Double";
		}
		return null;
	}

	private String processField(String field) {
		StringBuffer sb = new StringBuffer(field.length());
		// field = field.toLowerCase();
		String[] fields = field.split("_");
		String temp = null;
		sb.append(fields[0]);
		for (int i = 1; i < fields.length; i++) {
			temp = fields[i].trim();
			sb.append(temp.substring(0, 1).toUpperCase()).append(
					temp.substring(1));
		}
		return sb.toString();
	}

	/**
	 * 将实体类名首字母改为小写
	 *
	 * @param beanName
	 * @return
	 */
	private String processResultMapId(String beanName) {
		return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
	}

	/**
	 * 构建类上面的注释
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException
	 */
	private BufferedWriter buildClassComment(BufferedWriter bw, String text)
			throws IOException {
		bw.newLine();
		bw.newLine();
		bw.write("/**");
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" * " + text);
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" **/");
		return bw;
	}

	/**
	 * add notes
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException
	 */
	private BufferedWriter buildMethodComment(BufferedWriter bw, String text)
			throws IOException {
		bw.newLine();
		bw.write("\t/**");
		bw.newLine();
		bw.write("\t * ");
		bw.newLine();
		bw.write("\t * " + text);
		bw.newLine();
		bw.write("\t * ");
		bw.newLine();
		bw.write("\t **/");
		return bw;
	}

	/**
	 * generate entity class file
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 */
	private void buildEntityBean(List<String> columns, List<String> types,
			List<String> comments, String tableComment) throws IOException {
		File folder = new File(bean_path);
		if (!folder.exists()) {
			folder.mkdir();
		}

		File beanFile = new File(bean_path, beanName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(beanFile)));
		bw.write("package " + getBeanPackage() + ";");
		bw.newLine();
		bw.write("import java.io.Serializable;");
		bw.write("import java.util.Date;");
		bw.newLine();
		// bw.write("import lombok.Data;");
		// bw.write("import javax.persistence.Entity;");
		bw = buildClassComment(bw, tableComment);
		bw.newLine();
		bw.write("@SuppressWarnings(\"serial\")");
		bw.newLine();
		// bw.write("@Entity");
		// bw.write("@Data");
		// bw.newLine();
		bw.write("public class " + beanName + " implements Serializable {");
		bw.newLine();
		bw.newLine();
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			bw.write("\t/**" + comments.get(i) + "**/");
			bw.newLine();
			bw.write("\tprivate " + processType(types.get(i)) + " "
					+ processField(columns.get(i)) + ";");
			bw.newLine();
			bw.newLine();
		}
		bw.newLine();
		// generate get method and  set method
		String tempField = null;
		String _tempField = null;
		String tempType = null;
		for (int i = 0; i < size; i++) {
			tempType = processType(types.get(i));
			_tempField = processField(columns.get(i));
			tempField = _tempField.substring(0, 1).toUpperCase()
					+ _tempField.substring(1);
			bw.newLine();
			// bw.write("\tpublic void set" + tempField + "(" + tempType + " _"
			// + _tempField + "){");
			bw.write("\tpublic void set" + tempField + "(" + tempType + " "
					+ _tempField + "){");
			bw.newLine();
			// bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");
			bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
			bw.newLine();
			bw.write("\tpublic " + tempType + " get" + tempField + "(){");
			bw.newLine();
			bw.write("\t\treturn this." + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
		}
		bw.newLine();
		bw.write("}");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	/**
	 * generate Mapper file
	 *
	 * @throws IOException
	 */
	private void buildMapper() throws IOException {
		File folder = new File(mapper_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File mapperFile = new File(mapper_path, mapperName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(mapperFile), "utf-8"));
		bw.write("package " + getDaoPackage() + ";");
		bw.newLine();
		bw.newLine();
		bw.write("import " + getBeanPackage() + "." + beanName + ";");
		bw.newLine();
		bw.write("import org.apache.ibatis.annotations.Param;");
		bw = buildClassComment(bw, mapperName + "数据库操作接口类");
		bw.newLine();
		bw.newLine();
		// bw.write("public interface " + mapperName + " extends " +
		// mapper_extends + "<" + beanName + "> {");
		bw.write("public interface " + mapperName + "{");
		bw.newLine();
		bw.newLine();
		// ----------定义Mapper中的方法Begin----------
		bw = buildMethodComment(bw, "查询（根据主键ID查询�?");
		bw.newLine();
		bw.write("\t" + beanName
				+ "  selectByPrimaryKey ( @Param(\"id\") Long id );");
		bw.newLine();
		bw = buildMethodComment(bw, "删除（根据主键ID删除�?");
		bw.newLine();
		bw.write("\t" + "int deleteByPrimaryKey ( @Param(\"id\") Long id );");
		bw.newLine();
		bw = buildMethodComment(bw, "添加");
		bw.newLine();
		bw.write("\t" + "int insert( " + beanName + " record );");
		bw.newLine();
		bw = buildMethodComment(bw, "添加 （匹配有值的字段�?");
		bw.newLine();
		bw.write("\t" + "int insertSelective( " + beanName + " record );");
		bw.newLine();
		bw = buildMethodComment(bw, "修改 （匹配有值的字段�?");
		bw.newLine();
		bw.write("\t" + "int updateByPrimaryKeySelective( " + beanName
				+ " record );");
		bw.newLine();
		bw = buildMethodComment(bw, "修改（根据主键ID修改�?");
		bw.newLine();
		bw.write("\t" + "int updateByPrimaryKey ( " + beanName + " record );");
		bw.newLine();

		// ----------定义Mapper中的方法End----------
		bw.newLine();
		bw.write("}");
		bw.flush();
		bw.close();
	}

	/**
	 * 构建实体类映射XML文件
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 */
	private void buildMapperXml(List<String> columns, List<String> types,
			List<String> comments) throws IOException {
		File folder = new File(xml_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File mapperXmlFile = new File(xml_path, beanName + "Mapper.xml");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(mapperXmlFile)));
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.newLine();
		bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
		bw.newLine();
		bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		bw.newLine();
		bw.write("<mapper namespace=\"" + getDaoPackage() + "." + mapperName
				+ "\">");
		bw.newLine();
		bw.newLine();

		/*
		 * bw.write("\t<!--实体映射-->"); bw.newLine();
		 * bw.write("\t<resultMap id=\"" + this.processResultMapId(beanName) +
		 * "ResultMap\" type=\"" + beanName + "\">"); bw.newLine();
		 * bw.write("\t\t<!--" + comments.get(0) + "-->"); bw.newLine();
		 * bw.write("\t\t<id property=\"" + this.processField(columns.get(0)) +
		 * "\" column=\"" + columns.get(0) + "\" />"); bw.newLine(); int size =
		 * columns.size(); for ( int i = 1 ; i < size ; i++ ) {
		 * bw.write("\t\t<!--" + comments.get(i) + "-->"); bw.newLine();
		 * bw.write("\t\t<result property=\"" +
		 * this.processField(columns.get(i)) + "\" column=\"" + columns.get(i) +
		 * "\" />"); bw.newLine(); } bw.write("\t</resultMap>");
		 * 
		 * bw.newLine(); bw.newLine(); bw.newLine();
		 */

		// 下面�?始写SqlMapper中的方法
		// this.outputSqlMapperMethod(bw, columns, types);
		buildSQL(bw, columns, types);

		bw.write("</mapper>");
		bw.flush();
		bw.close();
	}

	private void buildSQL(BufferedWriter bw, List<String> columns,
			List<String> types) throws IOException {
		StringBuffer xml = new StringBuffer();
		//FIXME
		appendTableName(tableName, xml);
		xml.append("\n");
		appendColumnList(columns, xml);
		xml.append("\n");
		appendBaseResultMap(columns, types, xml);
		xml.append("\n");
		appendCreate(columns, xml);
		xml.append("\n");
		appendDelete(columns, types, xml);
		xml.append("\n");
		appendUpdate(columns, xml);
		xml.append("\n");
		appendCountTheDate(columns, types, xml);
		xml.append("\n");
		appendGetByid(columns, types, xml);
		xml.append("\n");
		appendGetList(columns, types, xml);
		xml.append("\n");
		appendGetListForCache(columns, types, xml);
		xml.append("\n");
		bw.write(xml.toString());
	}
	
	public static String captureName(String name) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// return name;
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}
	
	private String getEntityField(String field){
		StringBuffer sb = new StringBuffer();
		String fields[] = field.split("_");
		sb.append(fields[0]);
		for (int i = 1; i < fields.length; i++) {
			if(!StringUtils.isBlank(fields[i])){
				sb.append(captureName(fields[i]));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 通用结果�?
	 * @param columns
	 * @param bw
	 * @throws IOException 
	 */
	private void appendColumnList(List<String> columns, StringBuffer xml) throws IOException {
		int size = columns.size();
		xml.append("\n\t<sql id=\"Base_Column_List\">");
		xml.append("\n\t\tid,");
		for (int i = 1; i < size; i++) {
			xml.append("\n\t\t" + columns.get(i));
			if (i != size - 1) {
				xml.append(",");
			}
		}
		xml.append("\n\t</sql>");
	}
	/**
	 * 添加表名
	 * @param tableName
	 * @param xml
	 */
	private void appendTableName(String tableName, StringBuffer xml){
		xml.append("\n\t<sql id=\"tablename\">\n\t\t")
			.append(tableName)
			.append("\n\t</sql>");
	}
	
	private void appendBaseResultMap(List<String> columns, List<String> types, StringBuffer xml){
		xml.append("\n\t<resultMap id=\"BaseResultMap\" type=\"");
		apendEntityPackage(xml);
		xml.append("\">");
		xml.append("\n\t\t<id column=\"id\" property=\"id\" jdbcType=\"INTEGER\" />");
		for (int i = 1; i < columns.size(); i++) {
			xml.append("\n\t\t<result column=\"").append(columns.get(i))
			.append("\" property=\"").append(getEntityField(columns.get(i)))
			.append("\" jdbcType=\"").append(TYPE_DB_XML.get(getLetter(types.get(i)))).append("\" />");
			;
		}
		xml.append("\n\t</resultMap>");
	}
	
	
	private void appendCreate(List<String> columns, StringBuffer xml){
		xml.append("\n\t<insert id=\"create")
		.append(beanName)
		.append("\" useGeneratedKeys=\"true\" keyProperty=\"id\" parameterType=\"");
		apendEntityPackage(xml);
		xml.append("\">\n\t\tinsert ignore into\n\t\t\t<include refid=\"tablename\" />");
		
		xml.append("\n\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		String tempField = null;
		for (int i = 0; i < columns.size(); i++) {
			tempField = processField(columns.get(i));
			xml.append("\n\t\t\t<if test=\"" + tempField + " != null\">");
			xml.append("\n\t\t\t\t" + columns.get(i) + ",");
			xml.append("\n\t\t\t</if>");
		}

		xml.append("\n\t\t</trim>");
		xml.append("\n\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");

		tempField = null;
		for (int i = 0; i < columns.size(); i++) {
			tempField = processField(columns.get(i));
			xml.append("\n\t\t\t<if test=\"" + tempField + "!=null\">");
			xml.append("\n\t\t\t\t#{" + tempField + "},");
			xml.append("\n\t\t\t</if>");
		}

		xml.append("\n\t\t</trim>");
		xml.append("\n\t</insert>");
	}
	
	private void appendUpdate(List<String> columns,StringBuffer xml){
		// 修改update方法
		xml.append("\n\t<update id=\"update"+beanName+"\" parameterType=\"");
		apendEntityPackage(xml);
		xml.append("\">");
		
		xml.append("\n\t\tupdate \n\t\t\t<include refid=\"tablename\" />");
		xml.append("\n\t\t<set> ");

		String tempField = null;
		for (int i = 1; i < columns.size(); i++) {
			tempField = processField(columns.get(i));
			xml.append("\n\t\t\t<if test=\"" + tempField + " != null\">");
			xml.append("\n\t\t\t\t" + columns.get(i) + " = #{" + tempField + "},");
			xml.append("\n\t\t\t</if>");
		}

		xml.append("\n\t\t</set>");
		xml.append("\n\t\twhere\n\t\t\t" + columns.get(0) + " = #{"
				+ processField(columns.get(0)) + "}");
		xml.append("\n\t</update>\n");
		// update方法完毕
	}
	
	private void appendUpdateSelective(List<String> columns,StringBuffer xml){
		// ----- 修改（匹配有值的字段�?
				xml.append("\n\t<update id=\"update"+beanName+"\" parameterType=\""
						+ processResultMapId(beanName) + "\">");
				xml.append("\n\t\t UPDATE \n\t\t\t<include refid=\"tablename\" />");
				xml.append("\n\t\t SET ");

				String tempField = null;
				int size = columns.size();
				for (int i = 1; i < size; i++) {
					tempField = processField(columns.get(i));
					xml.append("\n\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
					if (i != size - 1) {
						xml.append(",");
					}
				}

				xml.append("\n\t\t where " + columns.get(0) + " = #{"
						+ processField(columns.get(0)) + "}");
				xml.append("\n\t</update>\n");
	}
	
	private void appendDelete(List<String> columns, List<String> types, StringBuffer xml){
		// 删除（根据主键ID删除�?
				xml.append("\n\t<delete id=\"deleteById\" parameterType=\"java.lang."
						+ processType(types.get(0)) + "\">");
				xml.append("\n\t\tdelete from \n\t\t\t<include refid=\"tablename\" />");
				xml.append("\n\t\twhere\n\t\t\t" + columns.get(0) + " = #{"
						+ processField(columns.get(0)) + "}");
				xml.append("\n\t</delete>\n");
				// 删除�?
	}
	
	private void appendGetByid(List<String> columns, List<String> types, StringBuffer xml){
		// 查询（根据主键ID查询�?
				xml.append("\n\t<select id=\"getById\" resultMap=\""
						+ getResultMap()
						+ "\" parameterType=\"java.lang." + TYPE_DB_BEAN.get(getLetter(types.get(0)))
						+ "\">");
				xml.append("\n\t\tselect");
				xml.append("\n\t\t\t<include refid=\"Base_Column_List\" />");
				xml.append("\n\t\tfrom\n\t\t\t<include refid=\"tablename\" />");
				xml.append("\n\t\twhere\n\t\t\t" + columns.get(0) + " = #{"
						+ processField(columns.get(0)) + "}");
				xml.append("\n\t</select>");
				// 查询�?
	}
	
	private void appendCountTheDate(List<String> columns, List<String> types, StringBuffer xml){
		xml.append("\n\t<select id=\"countTheData\" parameterType=\"java.util.Map\"	resultType=\"java.lang.Integer\">");
		xml.append("\n\t\t select\n\t\t\tcount(*)");
		xml.append("\n\t\t from \n\t\t\t<include refid=\"tablename\" />");
		xml.append("\n\t\t <where> ");
		for (int i = 0; i < columns.size(); i++) {
			xml.append(getColumnCondition(columns.get(i), types.get(i)));
		}
		xml.append("\n\t\t</where>");
		xml.append("\n\t</select>");
	}
	
	private void appendGetList(List<String> columns, List<String> types, StringBuffer xml){
		xml.append("\n\t<select id=\"get").append(beanName)
		.append("List\" parameterType=\"java.util.Map\"	resultMap=\"")
		.append(getResultMap())
		.append("\">");
		xml.append("\n\t\t select\n\t\t\t<include refid=\"Base_Column_List\" />");
		xml.append("\n\t\t from \n\t\t\t<include refid=\"tablename\" />");
		xml.append("\n\t\t <where> ");
		for (int i = 0; i < columns.size(); i++) {
			xml.append(getColumnCondition(columns.get(i), types.get(i)));
		}
		xml.append("\n\t\t</where>");
		xml.append("\n\t</select>");
	}
	
	private void appendGetListForCache(List<String> columns, List<String> types, StringBuffer xml){
		xml.append("\n\t<select id=\"get").append(beanName)
		.append("ListForCache\" parameterType=\"java.util.Map\"	resultType=\"java.lang.String\">");
		xml.append("\n\t\t select\n\t\t\t<include refid=\"Base_Column_List\" />");
		xml.append("\n\t\t from \n\t\t\t<include refid=\"tablename\" />");
		xml.append("\n\t\t <where> ");
		for (int i = 0; i < columns.size(); i++) {
			xml.append(getColumnCondition(columns.get(i), types.get(i)));
		}
		xml.append("\n\t\t</where>");
		xml.append("\n\t</select>");
	}
	
	private String getColumnCondition(String column, String type){
		StringBuffer temp = new StringBuffer();
		String field = getEntityField(column);
		temp.append("\n\t\t\t<if test=\"").append(field).append(" != null\">\n\t\t\t\tand ");
		if(type.contains("int")){
			temp.append(column).append("=#{").append(field).append("}\n\t\t\t</if>");
		}else if(type.contains("varchar") || type.contains("text") || type.contains("time")){
			temp.append(column).append(" like '%${").append(field).append("}%'\n\t\t\t</if>");
		}else {
			temp.append(column).append("=#{").append(field).append("}\n\t\t\t</if>");
		}
		return temp.toString();
	}
	
	private StringBuffer apendEntityPackage(StringBuffer xml){
		xml.append(getBeanPackage()).append(".").append(beanName);
		return xml;
	}
	
	private String getLetter(String str){
		char[] cs = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			if(Character.isLetter(cs[i])){
				sb.append(cs[i]);
			}
		}
		return sb.toString();
	}
	
	private String getResultMap(){
		return "BaseResultMap";
	}
	
	private String getBeanPackage(){
		String beanPackage = bean_package;
		if (tableName.indexOf("goods") > -1) {
			beanPackage += "goods";
		}else 
		if (tableName.indexOf("music") > -1) {
			beanPackage += "music";
		}else 
		if (tableName.indexOf("video") > -1) {
			beanPackage += "video";
		}else 
		if (tableName.indexOf("book") > -1) {
			beanPackage += "book";
		}else 
		if (tableName.indexOf("news") > -1) {
			beanPackage += "news";
		}else 
		if (tableName.indexOf("comics") > -1) {
			beanPackage += "comics";
		}else{
			beanPackage += "base";
		}
		return beanPackage;
	}
	
	private String getDaoPackage(){
		String beanPackage = mapper_package;
		if (tableName.indexOf("goods") > -1) {
			beanPackage += "goods";
		}else 
		if (tableName.indexOf("music") > -1) {
			beanPackage += "music";
		}else 
		if (tableName.indexOf("video") > -1) {
			beanPackage += "video";
		}else 
		if (tableName.indexOf("book") > -1) {
			beanPackage += "book";
		}else 
		if (tableName.indexOf("news") > -1) {
			beanPackage += "news";
		}else 
		if (tableName.indexOf("comics") > -1) {
			beanPackage += "comics";
		}else{
			beanPackage += "base";
		}
		return beanPackage;
	}
	
	/**
	 * 获取�?有的数据库表注释
	 *
	 * @return
	 * @throws SQLException
	 */
	private Map<String, String> getTableComment() throws SQLException {
		Map<String, String> maps = new HashMap<String, String>();
		PreparedStatement pstate = conn.prepareStatement("show table status");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString("NAME");
			String comment = results.getString("COMMENT");
			maps.put(tableName, comment);
		}
		return maps;
	}

	public void generate() throws ClassNotFoundException, SQLException,
			IOException {
		init();
		String prefix = "show full fields from ";
		List<String> columns = null;
		List<String> types = null;
		List<String> comments = null;
		PreparedStatement pstate = null;
		List<String> tables = getTables();
		Map<String, String> tableComments = getTableComment();
		for (String table : tables) {
			columns = new ArrayList<String>();
			types = new ArrayList<String>();
			comments = new ArrayList<String>();
			pstate = conn.prepareStatement(prefix + table);
			ResultSet results = pstate.executeQuery();
			while (results.next()) {
				columns.add(results.getString("FIELD").toLowerCase());
				types.add(results.getString("TYPE").toLowerCase());
				comments.add(results.getString("COMMENT"));
			}
			tableName = table;
			
			processTable(table);
			// this.outputBaseBean();
			String tableComment = tableComments.get(tableName);
			buildEntityBean(columns, types, comments, tableComment);
			buildMapper();
			buildMapperXml(columns, types, comments);
			tableName = null;
			beanName = null;
			mapperName = null;
		}
		conn.close();
	}
	
	public static void main(String[] args) {
		try {
			new EntityUtil().generate();
			// 自动打开生成文件的目�?
			Runtime.getRuntime().exec("cmd /c start explorer D:\\");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}