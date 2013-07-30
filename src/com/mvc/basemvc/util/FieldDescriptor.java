/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.util;

import java.lang.reflect.Field;

/**
 * @Description 字段描述类
 * @ClassName FieldDescriptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午03:56:30
 */
public class FieldDescriptor {

    /** The field. */
    private Field field;

    /** The writeable. */
    private boolean writeable;

    /** The readable. */
    private boolean readable;

    /**
     * Instantiates a new field descriptor.
     */
    public FieldDescriptor() {

    }

    /**
     * Instantiates a new field descriptor.
     *
     * @param f
     *            the f
     * @param readable
     *            the readable
     * @param writeable
     *            the writeable
     */
    public FieldDescriptor(Field f, boolean readable, boolean writeable) {
	this.field = f;
	this.readable = readable;
	this.writeable = writeable;
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    public Field getField() {
	return field;
    }

    /**
     * Sets the field.
     *
     * @param field
     *            the new field
     */
    public void setField(Field field) {
	this.field = field;
    }

    /**
     * Checks if is readable.
     *
     * @return true, if is readable
     */
    public boolean isReadable() {
	return readable;
    }

    /**
     * Sets the readable.
     *
     * @param readable
     *            the new readable
     */
    public void setReadable(boolean readable) {
	this.readable = readable;
    }

    /**
     * Checks if is writeable.
     *
     * @return true, if is writeable
     */
    public boolean isWriteable() {
	return writeable;
    }

    /**
     * Sets the writeable.
     *
     * @param writeable
     *            the new writeable
     */
    public void setWriteable(boolean writeable) {
	this.writeable = writeable;
    }

}
