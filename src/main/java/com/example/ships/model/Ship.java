package com.example.ships.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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
    public String final_port_code;

    @JsonProperty("Data przybycia")
    public LocalDate arrival_date;

    @JsonProperty("Godzina przybycia")
    public LocalTime arrival_time;

    @JsonProperty("Kod portu, z którego jednostka przypłynęła")
    public String initial_port_code;

    @JsonProperty("Nazwa statku")
    public String ship_name;

//    @JsonProperty("Bandera")
//    public String flag;

    @JsonProperty("Pojemność brutto")
    public double gross_capacity;

    @JsonProperty("Długość")
    public double length;

    @JsonProperty("Agent")
    public String agent;

    @JsonProperty("Nabrzeże (miejsce zacumowania")
    public String mooring_place;

    public Ship() {};
}

