package xyz.domknuddle.example;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonBean {

	@CSVReadable(headers = { "Einkommen" }, optional = true)
	private double income;

	@CSVReadable(headers = { "Personalnummer" })
	private int id;

	@CSVReadable(headers = { "Vorname" })
	private String firstName;

	@CSVReadable(headers = { "Nachname" })
	private String name;

	@CSVReadable(headers = { "TAG_DER_GEBURT" }, optional = true, formatter = "dd.MM.yyyy")
	private LocalDate birthdate;

}
