package com.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.meidebi.app.XApplication;
import com.orm.dsl.Table;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;
import com.orm.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SugarRecord {

	private String id = null;

	public static <T> void deleteAll(Class<T> type) {
		if(tabbleIsExist(type)){
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		sqLiteDatabase.delete(NamingHelper.toSQLName(type), null, null);
		}
	}
	
	public static <T> void dropTable(Class<T> type) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NamingHelper.toSQLName(type));
	}

	public static <T> void deleteAll(Class<T> type, String whereClause,
			String... whereArgs) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		sqLiteDatabase.delete(NamingHelper.toSQLName(type), whereClause,
				whereArgs);
	}

	@SuppressWarnings("deprecation")
	public static <T> void saveInTx(T... objects) {
		saveInTx(Arrays.asList(objects));
	}

	@SuppressWarnings("deprecation")
	public static <T> void saveInTx(Collection<T> objects) {
		SQLiteDatabase sqLiteDatabase = getSugarContext().getSugarDb().getDB();
		try {
			sqLiteDatabase.beginTransaction();
			sqLiteDatabase.setLockingEnabled(false);
			for (T object : objects) {
				SugarRecord.save(object);
			}
			sqLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			Log.i("Sugar", "Error in saving in transaction " + e.getMessage());
		} finally {
			sqLiteDatabase.endTransaction();
			sqLiteDatabase.setLockingEnabled(true);
		}
	}

	public static <T> List<T> listAll(Class<T> type) {
		return find(type, null, null, null, null, null);
	}

	public static <T> T findById(Class<T> type, Long id) {
		List<T> list = find(type, "id=?", new String[] { String.valueOf(id) },
				null, null, "1");
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public static <T> T findById(Class<T> type, String id) {
		List<T> list = find(type, "id=?", new String[] { id }, null, null, "1");
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public static <T> T findById(Class<T> type, Integer id) {
		return findById(type, Long.valueOf(id));
	}

	public static <T> Iterator<T> findAll(Class<T> type) {
		return findAsIterator(type, null, null, null, null, null);
	}

	public static <T> Iterator<T> findAsIterator(Class<T> type,
			String whereClause, String... whereArgs) {
		return findAsIterator(type, whereClause, whereArgs, null, null, null);
	}

	public static <T> Iterator<T> findWithQueryAsIterator(Class<T> type,
			String query, String... arguments) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		Cursor c = sqLiteDatabase.rawQuery(query, arguments);
		return new CursorIterator<T>(type, c);
	}

	public static <T> Iterator<T> findAsIterator(Class<T> type,
			String whereClause, String[] whereArgs, String groupBy,
			String orderBy, String limit) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		Cursor c = sqLiteDatabase.query(NamingHelper.toSQLName(type), null,
				whereClause, whereArgs, groupBy, null, orderBy, limit);
		return new CursorIterator<T>(type, c);
	}

	public static <T> List<T> find(Class<T> type, String whereClause,
			String... whereArgs) {
		return find(type, whereClause, whereArgs, null, null, null);
	}

	public static <T> List<T> findWithQuery(Class<T> type, String query,
			String... arguments) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		T entity;
		List<T> toRet = new ArrayList<T>();
		Cursor c = sqLiteDatabase.rawQuery(query, arguments);
		try {
			while (c.moveToNext()) {
				entity = type.getDeclaredConstructor().newInstance();
				SugarRecord.inflate(c, entity);
				toRet.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return toRet;
	}

	public static void executeQuery(String query, String... arguments) {
		getSugarContext().getSugarDb().getDB().execSQL(query, arguments);
	}

	public static <T> List<T> find(Class<T> type, String whereClause,
			String[] whereArgs, String groupBy, String orderBy, String limit) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		T entity;
		List<T> toRet = new ArrayList<T>();
		Cursor c = sqLiteDatabase.query(NamingHelper.toSQLName(type), null,
				whereClause, whereArgs, groupBy, null, orderBy, limit);
		try {
			while (c.moveToNext()) {
				entity = type.getDeclaredConstructor().newInstance();
				SugarRecord.inflate(c, entity);
				toRet.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return toRet;
	}

	public static XApplication getSugarContext() {
		return XApplication.getInstance();
	}

	public static <T> List<T> sql(String sql, Class<T> type) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();
		T entity;
		List<T> toRet = new ArrayList<T>();
		Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {});
		try {
			while (c.moveToNext()) {
				entity = type.getDeclaredConstructor().newInstance();
				SugarRecord.inflate(c, entity);
				toRet.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return toRet;
	}

	public static <T> long count(Class<?> type) {
		return count(type, null, null, null, null, null);
	}

	public static <T> long count(Class<?> type, String whereClause,
			String[] whereArgs) {
		return count(type, whereClause, whereArgs, null, null, null);
	}

	public static <T> long count(Class<?> type, String whereClause,
			String[] whereArgs, String groupBy, String orderBy, String limit) {
		SugarDb db = getSugarContext().getSugarDb();
		SQLiteDatabase sqLiteDatabase = db.getDB();

		long toRet = -1;
		String filter = (!TextUtils.isEmpty(whereClause)) ? " where "
				+ whereClause : "";
		SQLiteStatement sqLiteStatament = sqLiteDatabase
				.compileStatement("SELECT count(*) FROM "
						+ NamingHelper.toSQLName(type) + filter);

		if (whereArgs != null) {
			for (int i = whereArgs.length; i != 0; i--) {
				sqLiteStatament.bindString(i, whereArgs[i - 1]);
			}
		}

		try {
			toRet = sqLiteStatament.simpleQueryForLong();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqLiteStatament.close();
		}

		return toRet;
	}

	public static long save(Object object) {
		return save(getSugarContext().getSugarDb().getDB(), object);
	}

	static long save(SQLiteDatabase db, Object object) {
		List<Field> columns = ReflectionUtil.getTableFields(object.getClass());
		ContentValues values = new ContentValues(columns.size());
		Object oldobj = null;
		String Id = null;
		try {
			Id = String.valueOf(ReflectionUtil.getFieldValueInAllSuper(object,
					"id"));
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldobj = Select.from(object.getClass())
				.where(new Condition[] { new Condition("ID").eq(Id) }).first();
		for (Field column : columns) {
			column.setAccessible(true);
			Class<?> columnType = column.getType();
			try {
				String columnName = NamingHelper.toSQLName(column);
				Object columnValue = column.get(object);
				if (SugarRecord.class.isAssignableFrom(columnType)) {
					values.put(
							columnName,
							(columnValue != null) ? String
									.valueOf(((SugarRecord) columnValue)
											.getId()) : "0");
				} else {

					if (columnType.equals(Short.class)
							|| columnType.equals(short.class)) {
						values.put(columnName, (Short) columnValue);
					} else if (columnType.equals(Integer.class)
							|| columnType.equals(int.class)) {
						values.put(columnName, (Integer) columnValue);
					} else if (columnType.equals(Long.class)
							|| columnType.equals(long.class)) {
						values.put(columnName, (Long) columnValue);
					} else if (columnType.equals(Float.class)
							|| columnType.equals(float.class)) {
						values.put(columnName, (Float) columnValue);
					} else if (columnType.equals(Double.class)
							|| columnType.equals(double.class)) {
						values.put(columnName, (Double) columnValue);
					} else if (columnType.equals(Boolean.class)
							|| columnType.equals(boolean.class)) {
						values.put(columnName, (Boolean) columnValue);
					} else if (Date.class.equals(columnType)) {
						try {
							values.put(columnName,
									((Date) column.get(object)).getTime());
						} catch (NullPointerException e) {
							values.put(columnName, (Long) null);
						}
					} else if (Calendar.class.equals(columnType)) {
						try {
							values.put(columnName, ((Calendar) column
									.get(object)).getTimeInMillis());
						} catch (NullPointerException e) {
							values.put(columnName, (Long) null);
						}
					} else {
						if (columnValue == null) {
							if (oldobj != null) {
								Object OldcolumnValue = column.get(oldobj);
								if (OldcolumnValue != null) {
									values.put(columnName,
											String.valueOf(OldcolumnValue));
								} else {
									values.putNull(columnName);
								}
							} else {
								values.putNull(columnName);
							}
						} else {
							values.put(columnName, String.valueOf(columnValue));
						}
					}
				}

			} catch (IllegalAccessException e) {
				Log.e("Sugar", e.getMessage());
			}
		}
		long id = db.insertWithOnConflict(
				NamingHelper.toSQLName(object.getClass()), null, values,
				SQLiteDatabase.CONFLICT_REPLACE);

		if (SugarRecord.class.isAssignableFrom(object.getClass())) {
			ReflectionUtil.setFieldValueForId(object, id);
		}
		Log.i("Sugar", object.getClass().getSimpleName() + " saved : " + id);
		return id;
	}

	private static void inflate(Cursor cursor, Object object) {
		List<Field> columns = ReflectionUtil.getTableFields(object.getClass());

		for (Field field : columns) {
			if (field.getClass().isAnnotationPresent(Table.class)) {
				try {
					Long id = cursor.getLong(cursor.getColumnIndex(NamingHelper
							.toSQLName(field)));
					field.set(object,
							(id != null) ? findById(field.getType(), id) : null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				ReflectionUtil.setFieldValueFromCursor(cursor, field, object);
			}
		}
	}

	public void delete() {
		SQLiteDatabase db = getSugarContext().getSugarDb().getDB();
		db.delete(NamingHelper.toSQLName(getClass()), "Id=?",
				new String[] { getId().toString() });
	}

	public long save() {
		return save(getSugarContext().getSugarDb().getDB(), this);
	}

	@SuppressWarnings("unchecked")
	void inflate(Cursor cursor) {
		inflate(cursor, this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	static class CursorIterator<E> implements Iterator<E> {
		private Class<E> type;
		private Cursor cursor;

		public CursorIterator(Class<E> type, Cursor cursor) {
			this.type = type;
			this.cursor = cursor;
		}

		@Override
		public boolean hasNext() {
			return cursor != null && !cursor.isClosed()
					&& !cursor.isAfterLast();
		}

		@Override
		public E next() {
			E entity = null;
			if (cursor == null || cursor.isAfterLast()) {
				throw new NoSuchElementException();
			}

			if (cursor.isBeforeFirst()) {
				cursor.moveToFirst();
			}

			try {
				entity = type.getDeclaredConstructor().newInstance();
				SugarRecord.inflate(cursor, entity);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.moveToNext();
				if (cursor.isAfterLast()) {
					cursor.close();
				}
			}

			return entity;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	   public static boolean tabbleIsExist(Class<?> table){
           boolean result = false;
//           if(tableName == null){
//                   return false;
//           }
           String  tableName = NamingHelper.toSQLName(table);
           SQLiteDatabase db = null;
           Cursor cursor = null;
           try {
                   db = getSugarContext().getSugarDb().getDB();
                   String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='"+tableName+"'";
                   cursor = db.rawQuery(sql, null);
                   if(cursor.moveToNext()){
                           int count = cursor.getInt(0);
                           if(count>0){
                                   result = true;
                           }
                   }
                   
           } catch (Exception e) {
                   // TODO: handle exception
           }               
           return result;
   }

}
