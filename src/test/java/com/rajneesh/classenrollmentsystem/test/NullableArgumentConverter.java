package com.rajneesh.classenrollmentsystem.test;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 
 * This converter converts the string "null" to null value. It is helpful at
 * {@link CsvSource}
 * 
 * 
 *
 */
public final class NullableArgumentConverter extends SimpleArgumentConverter {

	@Override
	protected Object convert(Object source, Class<?> targetType)
			throws ArgumentConversionException {
		if ("null".equals(source)) {
			return null;
		}
		return DefaultArgumentConverter.INSTANCE.convert(source, targetType);
	}

}