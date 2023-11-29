package com.example.ships.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GeneratedValue
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
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Nazwa statku zawiera nieprawidłowe statki")
    @JsonProperty("Nazwa statku")
    public String shipName;

    @NotNull(message = "Pojemność brutto nie może być pusta")
    @JsonProperty("Pojemność brutto")
    public double grossCapacity;

    @NotNull(message = "Dlugosc nie może być pusta")
    @JsonProperty("Długość")
    public double length;

    @NotNull(message = "Nazwa agenta nie może byc pusta")
    @Size(max = 100, message = "Nazwa agenta nie może przekraczać 100 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9 .-]*$", message = "Nazwa statku zawiera nieprawidłowe statki")
    @JsonProperty("Agent")
    public String agent;

    @Size(max = 50, message = "Dlugosc nazwy ndabrzeza nie moze przekraczac 50 znakow")
    @NotNull(message = "Nazwa nadbrzeza nie moze byc pusta")
    @Pattern(regexp = "^[a-zA-Z0-9 .-]*$", message = "Nazwa statku zawiera nieprawidłowe statki")
    @JsonProperty("Nabrzeże (miejsce zacumowania)")
    public String mooringPlace;

    public Ship() {};
}

//dodac bandera
