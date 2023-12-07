package com.example.ships.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Expose(serialize = true, deserialize = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("Kod portu")
    private String finalPortCode = "PLSWI";

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Data przybycia statku nie może być pusta")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @SerializedName("Data przybycia")
    public LocalDate arrivalDate;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Godzina nie może być pusta")
    @JsonFormat(pattern = "HH:mm:ss")
    @SerializedName("Godzina przybycia")
    public LocalTime arrivalTime;

    @Expose(serialize = true, deserialize = false)
    @Size(min = 5, max = 5, message = "Kod portu musi zawierać dokładnie 5 znaków")
    @Pattern(regexp = "^[A-Z]*$", message = "Kod portu zawiera nieprawidłowe znaki")
    @NotNull(message = "Kod portu nie może być pusty")
    @SerializedName("Kod portu, z którego jednostka przypłynęła")
    public String initialPortCode;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Nazwa statku może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Nazwa statku nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nazwa statku zawiera nieprawidłowe znaki")
    @SerializedName("Nazwa statku")
    public String shipName;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Bandera może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Bandera nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Bandera zawiera nieprawidłowe znaki")
    @SerializedName("Bandera")
    public String flag;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Pojemność brutto nie może być pusta")
    @SerializedName("Pojemność brutto")
    public double grossCapacity;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Dlugosc nie może być pusta")
    @SerializedName("Długość")
    public double length;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Nazwa agenta nie może byc pusta")
    @Size(max = 100, message = "Nazwa agenta nie może przekraczać 100 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Agent zawiera nieprawidłowe znaki")
    @SerializedName("Agent")
    public String agent;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Dlugosc nazwy ndabrzeza nie moze przekraczac 50 znakow")
    @NotNull(message = "Nazwa nadbrzeza nie moze byc pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nadbrzeze zawiera nieprawidłowe znaki")
    @SerializedName("Nabrzeże (miejsce zacumowania)")
    public String mooringPlace;

    @Expose(serialize = false, deserialize = false)
    @ManyToOne
    private User user;

    public Ship() {};
}

