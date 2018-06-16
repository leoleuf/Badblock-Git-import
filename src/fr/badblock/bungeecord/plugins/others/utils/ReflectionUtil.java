package fr.badblock.bungeecord.plugins.others.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static Class<?> getBungeeClass(String path, String clazz) throws Exception {
		return Class.forName("net.md_5.bungee." + path + "." + clazz);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
		Constructor<?> c = clazz.getConstructor(args);
		c.setAccessible(true);
		return c;
	}

	@SuppressWarnings("rawtypes")
	public static Enum<?> getEnum(Class<?> clazz, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName());
		Enum[] econstants = (Enum[]) c.getEnumConstants();
		Enum[] arrayOfEnum1;
		int j = (arrayOfEnum1 = econstants).length;
		for (int i = 0; i < j; i++) {
			Enum<?> e = arrayOfEnum1[i];
			if (e.name().equalsIgnoreCase(constant)) {
				return e;
			}
		}
		throw new Exception("Enum constant not found " + constant);
	}

	@SuppressWarnings("rawtypes")
	public static Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
		Enum[] econstants = (Enum[]) c.getEnumConstants();
		Enum[] arrayOfEnum1;
		int j = (arrayOfEnum1 = econstants).length;
		for (int i = 0; i < j; i++) {
			Enum<?> e = arrayOfEnum1[i];
			if (e.name().equalsIgnoreCase(constant)) {
				return e;
			}
		}
		throw new Exception("Enum constant not found " + constant);
	}

	public static Field getField(Class<?> clazz, String fname) throws Exception {
		Field f = null;
		try {
			f = clazz.getDeclaredField(fname);
		} catch (Exception e) {
			f = clazz.getField(fname);
		}
		setFieldAccessible(f);
		return f;
	}

	public static Object getFirstObject(Class<?> clazz, Class<?> objclass, Object instance) throws Exception {
		Field f = null;
		Field[] arrayOfField;
		int j = (arrayOfField = clazz.getDeclaredFields()).length;
		for (int i = 0; i < j; i++) {
			Field fi = arrayOfField[i];
			if (fi.getType().equals(objclass)) {
				f = fi;
				break;
			}
		}
		if (f == null) {
			j = (arrayOfField = clazz.getFields()).length;
			for (int i = 0; i < j; i++) {
				Field fi = arrayOfField[i];
				if (fi.getType().equals(objclass)) {
					f = fi;
					break;
				}
			}
		}
		setFieldAccessible(f);
		return f.get(instance);
	}

	public static Method getMethod(Class<?> clazz, String mname) throws Exception {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(mname, new Class[0]);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(mname, new Class[0]);
			} catch (Exception ex) {
				return m;
			}
		}
		m.setAccessible(true);
		return m;
	}

	public static <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
		Field[] arrayOfField;
		int j = (arrayOfField = target.getDeclaredFields()).length;
		for (int i = 0; i < j; i++) {
			Field field = arrayOfField[i];
			if (((name == null) || (field.getName().equals(name))) && (fieldType.isAssignableFrom(field.getType()))
					&& (index-- <= 0)) {
				field.setAccessible(true);
				return field;
			}
		}
		if (target.getSuperclass() != null) {
			return getField(target.getSuperclass(), name, fieldType, index);
		}
		throw new IllegalArgumentException("Cannot find field with type " + fieldType);
	}

	public static Method getMethod(Class<?> clazz, String mname, Class<?>... args) throws Exception {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(mname, args);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(mname, args);
			} catch (Exception ex) {
				return m;
			}
		}
		m.setAccessible(true);
		return m;
	}

	public static Object getObject(Class<?> clazz, Object obj, String fname) throws Exception {
		return getField(clazz, fname).get(obj);
	}

	public static Object getObject(Object obj, String fname) throws Exception {
		return getField(obj.getClass(), fname).get(obj);
	}

	public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
		return getConstructor(clazz, args).newInstance(initargs);
	}

	public static Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception {
		return getMethod(clazz, method).invoke(obj, new Object[0]);
	}

	public static Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs)
			throws Exception {
		return getMethod(clazz, method, args).invoke(obj, initargs);
	}

	public static Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception {
		return getMethod(clazz, method).invoke(obj, initargs);
	}

	public static Object invokeMethod(Object obj, String method) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, new Object[0]);
	}

	public static Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, initargs);
	}

	public static void setFieldAccessible(Field f) throws Exception {
		f.setAccessible(true);
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(f, f.getModifiers() & 0xFFFFFFEF);
	}

	public static void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception {
		getField(clazz, fname).set(obj, value);
	}

	public static void setObject(Object obj, String fname, Object value) throws Exception {
		getField(obj.getClass(), fname).set(obj, value);
	}
}
