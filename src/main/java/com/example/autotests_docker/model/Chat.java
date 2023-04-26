package com.example.autotests_docker.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @JoinTable(name = "link_chat",
            joinColumns = @JoinColumn(name = "chatid"),
            inverseJoinColumns = @JoinColumn(name = "linkid"))
    private Set<Link> links = new HashSet<>();
}
