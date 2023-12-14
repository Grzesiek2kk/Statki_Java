package com.example.ships.model;

import com.example.ships.util.LocalDateDeserializer;
import com.example.ships.util.LocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JacksonXmlRootElement(localName = "ship")
public class Ship {
    @Id
    @Expose(serialize = true, deserialize = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public Long id;

    @Expose(serialize = true, deserialize = false)
    @JsonProperty("Kod portu")
    @JacksonXmlProperty(localName = "Kod_portu")
    @SerializedName("Kod portu")
    private String finalPortCode = "PLSWI";

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Data przybycia statku nie może być pusta")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("Data przybycia")
    @JacksonXmlProperty(localName = "Data_przybycia")
    @SerializedName("Data przybycia")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDate arrivalDate;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Godzina nie może być pusta")
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonProperty("Godzina przybycia")
    @JacksonXmlProperty(localName = "Godzina_przybycia")
    @SerializedName("Godzina przybycia")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    public LocalTime arrivalTime;

    @Expose(serialize = true, deserialize = false)
    @Size(min = 5, max = 5, message = "Kod portu musi zawierać dokładnie 5 znaków")
    @Pattern(regexp = "^[A-Z]*$", message = "Kod portu zawiera nieprawidłowe znaki")
    @NotNull(message = "Kod portu nie może być pusty")
    @JsonProperty("Kod portu, z którego jednostka przypłynęła")
    @JacksonXmlProperty(localName = "Kod_portu_przybycia")
    @SerializedName("Kod portu, z którego jednostka przypłynęła")
    public String initialPortCode;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Nazwa statku może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Nazwa statku nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nazwa statku zawiera nieprawidłowe znaki")
    @JsonProperty("Nazwa statku")
    @JacksonXmlProperty(localName = "Nazwa_statku")
    @SerializedName("Nazwa statku")
    public String shipName;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Bandera może zawierać maksymalnie 50 znaków")
    @NotNull(message = "Bandera nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Bandera zawiera nieprawidłowe znaki")
    @JsonProperty("Bandera")
    @JacksonXmlProperty(localName = "Bandera")
    @SerializedName("Bandera")
    public String flag;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Pojemność brutto nie może być pusta")
    @JsonProperty("Pojemność brutto")
    @JacksonXmlProperty(localName = "Pojemnosc_brutto")
    @SerializedName("Pojemność brutto")
    public double grossCapacity;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Dlugosc nie może być pusta")
    @JsonProperty("Długość")
    @JacksonXmlProperty(localName = "Dlugosc")
    @SerializedName("Długość")
    public double length;

    @Expose(serialize = true, deserialize = false)
    @NotNull(message = "Nazwa agenta nie może byc pusta")
    @Size(max = 100, message = "Nazwa agenta nie może przekraczać 100 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Agent zawiera nieprawidłowe znaki")
    @JsonProperty("Agent")
    @JacksonXmlProperty(localName = "Agent")
    @SerializedName("Agent")
    public String agent;

    @Expose(serialize = true, deserialize = false)
    @Size(max = 50, message = "Dlugosc nazwy ndabrzeza nie moze przekraczac 50 znakow")
    @NotNull(message = "Nazwa nadbrzeza nie moze byc pusta")
    @Pattern(regexp = "^[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ .-]*$", message = "Nadbrzeze zawiera nieprawidłowe znaki")
    @JsonProperty("Nabrzeże (miejsce zacumowania)")
    @JacksonXmlProperty(localName = "Nabrzeze")
    @SerializedName("Nabrzeże (miejsce zacumowania)")
    public String mooringPlace;

    @Expose(serialize = false, deserialize = false)
    @JsonIgnore
    @ManyToOne
    private User user;

    public Ship() {

    }
}

