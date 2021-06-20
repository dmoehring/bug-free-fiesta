package xyz.domknuddle.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.ParseLocalDate;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class TimothysReader {

	public static <T> List<T> readWithAnnotation(Class<T> clazz, File file) {
		List<T> readedList = new ArrayList<T>();
		try {
			ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(file),
					new CsvPreference.Builder(Character.MIN_VALUE, '\t', "\r\n").build());

			String[] header = beanReader.getHeader(true);

			String[] mappingHeaders = new String[header.length];
			CellProcessor[] processors = new CellProcessor[header.length];

			fillArrays(header, mappingHeaders, processors, clazz);

			T object;
			while ((object = beanReader.read(clazz, mappingHeaders, processors)) != null) {
				readedList.add(object);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return readedList;
	}

	private static <T> void fillArrays(String[] csvHeader, String[] mappingHeaders, CellProcessor[] processors,
			Class<T> clazz) {
		List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
		for (int i = 0; i < csvHeader.length; i++) {
			for (Field f : fields) {
				CSVReadable[] annotations = f.getDeclaredAnnotationsByType(CSVReadable.class);
				if (annotations.length > 0) {
					String csvHead = csvHeader[i];
					boolean contains = Arrays.stream(annotations[0].headers())
							.anyMatch((maybeCsvHeader) -> maybeCsvHeader.equals(csvHead));
					if (contains) {
						mappingHeaders[i] = f.getName();

						processors[i] = generateCellProcessor(f, annotations[0]);
					}
				}
			}
		}
	}

	// TODO more Class Typs
	private static CellProcessor generateCellProcessor(Field f, CSVReadable csvReadable) {
		Class<?> type = f.getType();
		CellProcessorAdaptor inner = null;
		if (type.equals(Integer.class) || type.equals(int.class)) {
			inner = new ParseInt();
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			inner = new ParseDouble();
		} else if (type.equals(LocalDate.class)) {
			inner = new ParseLocalDate(DateTimeFormatter.ofPattern(csvReadable.formatter()));
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			inner = new ParseBool();
		} else if (type.equals(LocalDateTime.class)) {
			inner = new ParseLocalDateTime(DateTimeFormatter.ofPattern(csvReadable.formatter()));
		}

		if (type.equals(String.class)) {
			return csvReadable.optional() ? new Optional() : new NotNull();
		}
		return csvReadable.optional() ? new Optional(inner) : new NotNull(inner);
	}

}
