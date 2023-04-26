package com.example.autotests_docker.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "link")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "updatedat")
    private OffsetDateTime updatedAt;

    @Column(name = "ghforks")
    @ColumnDefault(value = "0")
    private Integer ghForksCount;

    @Column(name = "ghbranches")
    @ColumnDefault(value = "0")
    private Integer ghBranchesCount;

    @Column(name = "soanswers")
    @ColumnDefault(value = "0")
    private Integer soAnswersCount;

    @ManyToMany
    @JoinTable(name = "link_chat",
            joinColumns = @JoinColumn(name = "linkid"),
            inverseJoinColumns = @JoinColumn(name = "chatid"))
    private Set<Chat> chats = new HashSet<>();

    @PreUpdate
    @PrePersist
    public void preUpdate() {
        if (updatedAt == null) {
            updatedAt = OffsetDateTime.now();
        }
    }
}
