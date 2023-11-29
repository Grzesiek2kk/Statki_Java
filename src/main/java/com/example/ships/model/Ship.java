package com.example.ships.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="ships")
@Getter
@Setter
@Data
@AllArgsConstructor
@JsonIgnoreProperties
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @JsonProperty("Kod portu")
    private String finalPortCode = "PLSWI";

    @NotNull(message = "Data przybycia statku nie może być pusta")
    @JsonProperty("Data przybycia")
    public LocalDate arrivalDate;

    @NotNull(message = "Godzina nie może być pusta")
    @JsonProperty("Godzina przybycia")
    public LocalTime arrivalTime;

    @Size(min = 5, max = 5, message = "Kod portu musi zawierać dokładnie 5 znaków")
    @Pattern(regexp = "^[A-Z]*$", message = "Kod portu zawiera nieprawidłowe znaki")
    @NotNull(message = "Kod portu nie może być pusty")
    @JsonProperty("Kod portu, z którego jednostka przypłynęła")
    public String initialPortCode;

    @Size(max = 50, message = "Nazwa statku może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Nazwa statku nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nazwa statku zawiera nieprawidłowe znaki")
    @JsonProperty("Nazwa statku")
    public String shipName;

    @Size(max = 50, message = "Bandera może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Bandera nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Bandera zawiera nieprawidłowe znaki")
    @JsonProperty("Bandera")
    public String flag;

    @NotNull(message = "Pojemność brutto nie może być pusta")
    @JsonProperty("Pojemność brutto")
    public double grossCapacity;

    @NotNull(message = "Dlugosc nie może być pusta")
    @JsonProperty("Długość")
    public double length;

    @NotNull(message = "Nazwa agenta nie może byc pusta")
    @Size(max = 100, message = "Nazwa agenta nie może przekraczać 100 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Agent zawiera nieprawidłowe znaki")
    @JsonProperty("Agent")
    public String agent;

    @Size(max = 50, message = "Dlugosc nazwy ndabrzeza nie moze przekraczac 50 znakow")
    @NotNull(message = "Nazwa nadbrzeza nie moze byc pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nadbrzeze zawiera nieprawidłowe znaki")
    @JsonProperty("Nabrzeże (miejsce zacumowania)")
    public String mooringPlace;

    @ManyToOne
    private User user;

    public Ship() {};
}

