package com.spring.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POKE_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {

    @Id
    @Column(name = "POKEMON_ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE1")
    private String type1;
    @Column(name = "TYPE2")
    private String type2;
    @Column(name = "TOTAL")
    private int total;
    @Column(name = "HP")
    private int hp;
    @Column(name = "ATTACK")
    private int attack;
    @Column(name = "DEFENSE")
    private int defense;
    @Column(name = "SP_ATK")
    private int spAtk;
    @Column(name = "SP_DEF")
    private int spDef;
    @Column(name = "SPEED")
    private int speed;
    @Column(name = "GENERATION")
    private int generation;


}
