package xyz.domknuddle.example;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class Person implements Serializable {
    @Id
    private int id;
    private double income;
    private String firstName;
    private String name;
    private LocalDate birthdate;
}
